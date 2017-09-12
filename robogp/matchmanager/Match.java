package robogp.matchmanager;

import Match.Card;
import connection.Connection;
import connection.Message;
import connection.MessageObserver;
import connection.PartnerShutDownException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import robogp.robodrome.Robodrome;

/**
 *
 * @author claudia
 */
public class Match implements MessageObserver {

    public static final int ROBOTSINGAME = 8;
    public static final String MatchJoinRequestMsg = "joinMatchRequest";
    public static final String MatchJoinReplyMsg = "joinMatchReply";
    public static final String MatchStartMsg = "startMatch";
    public static final String MatchCancelMsg = "cancelMatch";
    public static final String MatchCommands = "commands";
    public static final String AvailableMessage = "available";
    public static final String MatchView = "MatchView";
    private Card[][] instructions;

    public enum EndGame {
        First, First3, AllButLast
    };

    public enum State {
        Created, Started, Canceled
    };

    private static final String[] ROBOT_COLORS = {"blue", "red", "yellow", "emerald", "violet", "orange", "turquoise", "green"};
    public static final String[] ROBOT_NAMES = {"robot-blue", "robot-red", "robot-yellow", "robot-emerald", "robot-violet", "robot-orange", "robot-turquoise", "robot-green"};
    private final Robodrome theRobodrome;
    private final RobotMarker[] robots;
    private final EndGame endGameCondition;
    private final int nMaxPlayers;
    private final int nRobotsXPlayer;
    private final boolean initUpgrades;
    private State status;
    int idToAssign = 0;

    private final HashMap<String, Connection> waiting;
    private final HashMap<String, Connection> players;

    /* Gestione pattern singleton */
    private static Match singleInstance;

    private Match(String rbdName, int nMaxPlayers, int nRobotsXPlayer, EndGame endGameCond, boolean initUpg) {
        this.nMaxPlayers = nMaxPlayers;
        this.nRobotsXPlayer = nRobotsXPlayer;
        this.endGameCondition = endGameCond;
        this.initUpgrades = initUpg;
        String rbdFileName = "robodromes/" + rbdName + ".txt";
        this.robots = new RobotMarker[Match.ROBOT_NAMES.length];
        this.theRobodrome = new Robodrome(rbdFileName);
        for (int i = 0; i < Match.ROBOT_NAMES.length; i++) {
            this.robots[i] = new RobotMarker(Match.ROBOT_NAMES[i], Match.ROBOT_COLORS[i]);
        }
        waiting = new HashMap<>();
        players = new HashMap<>();
        this.status = State.Created;
        this.instructions = new Card[8][5];
    }

    public Robodrome getTheRobodrome() {
        return theRobodrome;
    }

    public static Match getInstance(String rbdName, int nMaxPlayers,
            int nRobotsXPlayer, EndGame endGameCond, boolean initUpg) {
        if (Match.singleInstance == null || Match.singleInstance.status == Match.State.Canceled) {
            Match.singleInstance = new Match(rbdName, nMaxPlayers, nRobotsXPlayer, endGameCond, initUpg);
        }
        return Match.singleInstance;
    }

    public static Match getInstance() {
        if (Match.singleInstance == null || Match.singleInstance.status == Match.State.Canceled) {
            return null;
        }
        return Match.singleInstance;
    }

    @Override
    public void notifyMessageReceived(Message msg) {
        if (msg.getName().equals(Match.MatchJoinRequestMsg)) {
            String nickName = (String) msg.getParameter(0);
            this.waiting.put(nickName, msg.getSenderConnection());
            MatchManagerApp.getAppInstance().getIniziarePartitaController().matchJoinRequestArrived(msg);
        } else if (msg.getName().equals(Match.MatchCommands)) {
            Card[] istr = (Card[]) msg.getParameter(0);
            instructions[istr[0].getIdOwner()] = istr;
            boolean ok = true;
            for (int i = 0; i < getPlayerCount(); i++) {
                if (instructions[i][0] == null) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                for (int i = 0; i < 5; i++) {
                    try {
                        MatchManagerApp.getAppInstance().executeCommands(instructions, i);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                informRoundEnded();
            }
        }
    }

    public State getStatus() {
        return this.status;
    }

    public void cancel() {
        this.status = State.Canceled;

        Message msg = new Message(Match.MatchCancelMsg);
        waiting.keySet().forEach((nickname) -> {
            this.refusePlayer(nickname);
        });

        players.values().stream().forEach((conn) -> {
            try {
                conn.sendMessage(msg);
            } catch (PartnerShutDownException ex) {
                Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void start() {
        this.status = State.Started;

        Message msg = new Message(Match.MatchStartMsg);

        players.values().stream().forEach((conn) -> {
            try {
                conn.sendMessage(msg);
            } catch (PartnerShutDownException ex) {
                Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void stop() {
        // PROBABILMENTE NON IMPLEMENTATO NEL CORSO DI QUESTO PROGETTO
    }

    public ArrayList<RobotMarker> getAvailableRobots() {
        ArrayList<RobotMarker> ret = new ArrayList<>();
        for (RobotMarker m : this.robots) {
            if (!m.isAssigned()) {
                ret.add(m);
            }
        }
        return ret;
    }

    public ArrayList<RobotMarker> getAllRobots() {
        ArrayList<RobotMarker> ret = new ArrayList<>();
        ret.addAll(Arrays.asList(this.robots));
        return ret;
    }

    public int getRobotsPerPlayer() {
        return this.nRobotsXPlayer;
    }

    public void refusePlayer(String nickname) {
        try {

            Connection conn = this.waiting.get(nickname);

            Message reply = new Message(Match.MatchJoinReplyMsg);
            Object[] parameters = new Object[1];
            parameters[0] = false;
            reply.setParameters(parameters);

            conn.sendMessage(reply);
        } catch (PartnerShutDownException ex) {
            Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            waiting.remove(nickname);
        }
    }

    public boolean addPlayer(String nickname, List<RobotMarker> selection) {
        boolean added = false;
        try {
            selection.forEach((rob) -> {
                int dock = this.getFreeDock();
                rob.assign(nickname, dock);
            });

            Connection conn = this.waiting.get(nickname);
            Message reply = new Message(Match.MatchJoinReplyMsg);
            Object[] parameters = new Object[3];
            parameters[0] = true;
            parameters[1] = selection.toArray(new RobotMarker[selection.size()]);
            parameters[2] = idToAssign++;
            reply.setParameters(parameters);

            conn.sendMessage(reply);
            this.players.put(nickname, conn);
            added = true;
        } catch (PartnerShutDownException ex) {
            Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            waiting.remove(nickname);
        }
        return added;

    }

    private int getFreeDock() {
        boolean[] docks = new boolean[this.theRobodrome.getDocksCount()];
        for (RobotMarker rob : this.robots) {
            if (rob.isAssigned()) {
                docks[rob.getDock() - 1] = true;
            }
        }
        int count = 0;
        while (docks[count]) {
            count++;
        }
        if (count < docks.length) {
            return count + 1;
        }
        return -1;
    }

    public int getPlayerCount() {
        return this.players.size();
    }

    public int getMaxPlayers() {
        return this.nMaxPlayers;
    }
    
    private void informRoundEnded(){
     players.values().stream().forEach((conn) -> {
            try {
                Message msg = new Message(AvailableMessage);
                conn.sendMessage(msg);
                this.instructions = new Card[8][5];
            } catch (PartnerShutDownException ex) {
                Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
