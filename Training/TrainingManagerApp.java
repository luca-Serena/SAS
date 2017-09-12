package Training;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import robogp.matchmanager.RobotChooser;
import robogp.matchmanager.RobotMarker;
import robogp.matchmanager.RobotMarker;
import robogp.robodrome.*;
import robogp.robodrome.view.RobodromeView;

/**
 *
 * @author utente
 */
public class TrainingManagerApp extends javax.swing.JFrame {

    JTable table;
    DefaultTableModel model;
    JButton prova = new JButton("prova");
    static RobodromeView rdview;
    public RobotMarker robmar;
    public Robodrome robodrome;
    public String rewindistr = "";
    public String lastistr = "";
    public boolean firstistr = false;

    private static TrainingManagerApp singleInstance;
    //  private RobotStatePanel[] robotPanel;
    private final RobotChooser robotChooser;
    private static final String[] ROBOT_COLORS = {"blue", "red", "yellow", "emerald", "violet", "orange", "turquoise", "green"};
    private static final String[] ROBOT_NAMES = {"robot-blue", "robot-red", "robot-yellow", "robot-emerald", "robot-violet", "robot-orange", "robot-turquoise", "robot-green"};
    private RobotMarker choosenRobot = null;
    int rewindX, rewindY;
    Direction rewindDir;

    //immagini caricate e scalate
    ImageIcon backupimg = new ImageIcon(new ImageIcon("tiles/card-backup.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon move1img = new ImageIcon(new ImageIcon("tiles/card-move1.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon move2img = new ImageIcon(new ImageIcon("tiles/card-move2.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon move3img = new ImageIcon(new ImageIcon("tiles/card-move3.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon turnlimg = new ImageIcon(new ImageIcon("tiles/card-turnL.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon turnrimg = new ImageIcon(new ImageIcon("tiles/card-turnR.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon uturnimg = new ImageIcon(new ImageIcon("tiles/card-uturn.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon rotate90img = new ImageIcon(new ImageIcon("tiles/rotate90.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));

    public TrainingManagerApp() {
        initComponents();
        setupMatchPanel();
        loadImagesOnButtons();
        this.robotChooser = new RobotChooser(this, true);
    }

    /*try {                                                                                                           //inserimento immagine nel pulsante "Scrivi mail"
    Image img = ImageIO.read(new File("tiles/card-backup.png"));
    Image newimg = img.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH ) ;
    backup.setIcon(new ImageIcon(newimg));
    } catch (IOException ex) {
    System.err.println(ex);
    }*/
    private void loadImagesOnButtons() {
        try {
            Image img1 = ImageIO.read(new File("robots/robot-blue.png"));
            Image sclImg1 = img1.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
            jButton2.setIcon(new ImageIcon(sclImg1));
            Image img2 = ImageIO.read(new File("robots/robot-red.png"));
            Image sclImg2 = img2.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
            jButton3.setIcon(new ImageIcon(sclImg2));
            Image img3 = ImageIO.read(new File("robots/robot-yellow.png"));
            Image sclImg3 = img3.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
            jButton4.setIcon(new ImageIcon(sclImg3));
            Image img4 = ImageIO.read(new File("robots/robot-emerald.png"));
            Image sclImg4 = img4.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
            jButton5.setIcon(new ImageIcon(sclImg4));
            Image img5 = ImageIO.read(new File("robots/robot-violet.png"));
            Image sclImg5 = img5.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
            jButton6.setIcon(new ImageIcon(sclImg5));
            Image img6 = ImageIO.read(new File("robots/robot-orange.png"));
            Image sclImg6 = img6.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
            jButton7.setIcon(new ImageIcon(sclImg6));
            Image img7 = ImageIO.read(new File("robots/robot-turquoise.png"));
            Image sclImg7 = img7.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
            jButton8.setIcon(new ImageIcon(sclImg7));
            Image img8 = ImageIO.read(new File("robots/robot-green.png"));
            Image sclImg8 = img8.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
            jButton9.setIcon(new ImageIcon(sclImg8));
        } catch (IOException ex) {
            Logger.getLogger(TrainingManagerApp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /* private void getAllRobots() {
    jPanel1.add(jButton1);
    jPanel1.add(jComboBox1);
    jPanel1.add(jPanel2);
    ArrayList<RobotMarker> allRobots = new ArrayList();
    for (int i = 0; i < ROBOT_NAMES.length; i++) {
    RobotMarker r = new RobotMarker(ROBOT_NAMES[i], ROBOT_COLORS[i]);
    allRobots.add(r);
    }
    
    this.robotPanel = new RobotStatePanel[allRobots.size()];
    this.jPanel2.removeAll();
    for (int i = 0; i < robotPanel.length; i++) {
    this.robotPanel[i] = new RobotStatePanel(allRobots.get(i));
    this.jPanel2.add(this.robotPanel[i]);
    }
    this.jPanel2.updateUI();
    } */
    private void setupMatchPanel() {

        File robodromeDir = new File("robodromes");
        File[] robodromeFiles = robodromeDir.listFiles();
        String[] opts = new String[robodromeFiles.length];
        for (int i = 0; i < opts.length; i++) {
            opts[i] = robodromeFiles[i].getName().split("\\.")[0];
        }
        this.jComboBox1.setModel(new DefaultComboBoxModel<>(opts));
    }

    //training app
    public void checkCell(int x, int y) {
        System.out.println("x: " + x + "   y: " + y + "  direzione " + robmar.getDir());
        BoardCell cell = robodrome.getCell(y, x);
        if (cell instanceof BeltCell) {
            BeltCell belt = (BeltCell) cell;
            rdview.addRobotMove(robmar, 1, belt.getOutputDirection(), Rotation.NO);
            if (belt.getOutputDirection() != null) {
                switch (belt.getOutputDirection()) {
                    case S:
                        robmar.setY(robmar.getY() + 1);
                        checkCell(x, y + 1);
                        break;
                    case N:
                        robmar.setY(robmar.getY() - 1);
                        checkCell(x, y - 1);
                        break;
                    case E:
                        robmar.setX(robmar.getX() + 1);
                        checkCell(x + 1, y);
                        break;
                    case W:
                        robmar.setX(robmar.getX() - 1);
                        checkCell(x - 1, y);
                        break;
                }
            }
        } else if (cell instanceof PitCell) {
            rdview.addRobotFall(robmar);
        }
    }

    private void moveAndSetPosition(int movement) {
        int x, y;
        if (null != robmar.getDir()) {
            switch (robmar.getDir()) {
                case S:
                    y = getRightY(robmar.getY(), false, movement);
                    //  change = (robmar.getY() + movement) >= robodrome.getRowCount();
                    robmar.setY(y);
                    rdview.addRobotMove(robmar, movement, robmar.getDir(), Rotation.NO);
                    break;

                case N:
                    y = getRightY(robmar.getY(), true, movement);
                    robmar.setY(y);
                    rdview.addRobotMove(robmar, movement, robmar.getDir(), Rotation.NO);
                    break;

                case E:
                    x = getRightX(robmar.getX(), false, movement);
                    robmar.setX(x);
                    rdview.addRobotMove(robmar, movement, robmar.getDir(), Rotation.NO);
                    break;

                case W:
                    x = getRightX(robmar.getX(), true, movement);
                    robmar.setX(x);
                    rdview.addRobotMove(robmar, movement, robmar.getDir(), Rotation.NO);
                    break;
            }
        }
    }

    /*restituisce il numero di celle che il robot può percorrere
    se moveThree è false portanno esserci solo 2 celle al massimo*/
    private int checkMoreWalls(int x, int y, Direction dir, boolean moveThree) {
        int realX, realY;
        if (!checkWalls(x, y, dir)) {
            return 0;
        } else if (dir != null) {
            switch (dir) {
                case S:
                    realY = getRightY(robmar.getY(), false, 1);
                    if (!checkWalls(robmar.getX(), realY, Direction.S)) {
                        return 1;
                    }
                    break;
                case N:
                    realY = getRightY(robmar.getY(), true, 1);
                    if (!checkWalls(robmar.getX(), realY, Direction.N)) {
                        return 1;
                    }
                    break;
                case E:
                    realX = getRightX(robmar.getX(), false, 1);
                    if (!checkWalls(realX, robmar.getY(), Direction.E)) {
                        return 1;
                    }
                    break;
                case W:
                    realX = getRightX(robmar.getX(), true, 1);
                    if (!checkWalls(realX, robmar.getY(), Direction.W)) {
                        return 1;
                    }
                    break;
            }
        }
        if (moveThree) {
            switch (robmar.getDir()) {
                case S:
                    realY = getRightY(robmar.getY(), false, 2);
                    if (!checkWalls(robmar.getX(), realY, Direction.S)) {
                        return 2;
                    }
                    break;
                case N:
                    realY = getRightY(robmar.getY(), true, 2);
                    if (!checkWalls(robmar.getX(), realY, Direction.N)) {
                        return 2;
                    }
                    break;
                case E:
                    realX = getRightX(robmar.getX(), false, 2);
                    if (!checkWalls(realX, robmar.getY(), Direction.E)) {
                        return 2;
                    }
                    break;
                case W:
                    realX = getRightX(robmar.getX(), true, 2);
                    if (!checkWalls(realX, robmar.getY(), Direction.W)) {
                        return 2;
                    }
            }
            return 3;
        } else {
            return 2;
        }

    }

    /*ritorna false se la cella ha un muro nella posixione indicata,
    true se non ci sono ostacoli               */
    private boolean checkWalls(int x, int y, Direction dir) {
        if (robodrome.getCell(y, x) instanceof FloorCell) {
            FloorCell floor = (FloorCell) robodrome.getCell(y, x);
            FloorCell nextFloor;
            int rightX, rightY;
            switch (dir) {
                case N:
                    rightY = getRightY(y, true, 1);
                    if (robodrome.getCell(rightY, x) instanceof FloorCell) {
                        nextFloor = (FloorCell) robodrome.getCell(rightY, x);
                        return !floor.hasWall(dir) && !nextFloor.hasWall(Direction.S);
                    }
                    break;
                case S:
                    rightY = getRightY(y, false, 1);
                    if (robodrome.getCell(rightY, x) instanceof FloorCell) {
                        nextFloor = (FloorCell) robodrome.getCell(rightY, x);
                        return !floor.hasWall(dir) && !nextFloor.hasWall(Direction.N);
                    }
                    break;
                case E:
                    rightX = getRightX(x, false, 1);
                    if (robodrome.getCell(y, rightX) instanceof FloorCell) {
                        nextFloor = (FloorCell) robodrome.getCell(y, rightX);
                        return !floor.hasWall(dir) && !nextFloor.hasWall(Direction.W);
                    }
                    break;
                case W:
                    rightX = getRightX(x, true, 1);
                    if (robodrome.getCell(y, rightX) instanceof FloorCell) {
                        nextFloor = (FloorCell) robodrome.getCell(y, rightX);
                        return !floor.hasWall(dir) && !nextFloor.hasWall(Direction.E);
                    }
                    break;
            }
            return !floor.hasWall(dir);
        } else {
            return true;
        }
    }

    //classe legata alla creazione della "cella pulsante" elimina
    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    /* restituisce il giusto posizionamento sull'asse X. Da usare per controllare
    che il robot si muova all'interno dei limiti del robodrommo */
    private int getRightX(int x, boolean westDir, int movement) {
        if (westDir) {
            if (x - movement < 0) {
                return x - movement + robodrome.getColumnCount();
            } else {
                return x - movement;
            }
        } else {
            if (x >= robodrome.getColumnCount()) {
                return (x + movement) % robodrome.getColumnCount();
            } else {
                return x + movement;
            }
        }
    }

    private int getRightY(int y, boolean northDir, int movement) {
        if (northDir) {
            if (y - movement < 0) {
                return y - movement + robodrome.getRowCount();
            } else {
                return y - movement;
            }
        } else {
            if (y + movement >= robodrome.getRowCount()) {
                return (y + movement) % robodrome.getRowCount();
            } else {
                return y + movement;
            }
        }
    }

    public static RobodromeView getRobodromeView() {
        return rdview;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        managerPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        gamePanel = new javax.swing.JPanel();
        titolo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        scrollCardPanel = new javax.swing.JScrollPane();
        leftPanel = new javax.swing.JPanel();
        backup = new javax.swing.JButton();
        move1 = new javax.swing.JButton();
        move2 = new javax.swing.JButton();
        move3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        turnl = new javax.swing.JButton();
        turnr = new javax.swing.JButton();
        uturn = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        tabellone = new javax.swing.JSplitPane();
        controlActionPanel = new javax.swing.JPanel();
        play = new java.awt.Button();
        pause = new java.awt.Button();
        button3 = new java.awt.Button();
        labRuotaRobot = new javax.swing.JLabel();
        rotateButton = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.CardLayout());

        managerPanel.setForeground(new java.awt.Color(204, 255, 204));
        managerPanel.setName("TrainingManagerApp"); // NOI18N

        jButton1.setText("Ora vai ad allenarti");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jLabel1.setText("Robodromo:");

        jComboBox1.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jLabel2.setText("Posizione Robot:");

        jComboBox2.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout managerPanelLayout = new javax.swing.GroupLayout(managerPanel);
        managerPanel.setLayout(managerPanelLayout);
        managerPanelLayout.setHorizontalGroup(
            managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(managerPanelLayout.createSequentialGroup()
                .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(managerPanelLayout.createSequentialGroup()
                        .addGap(171, 171, 171)
                        .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(managerPanelLayout.createSequentialGroup()
                                .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(managerPanelLayout.createSequentialGroup()
                                            .addGap(249, 249, 249)
                                            .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(managerPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(18, 18, 18)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(102, 102, 102)))
                                .addGap(227, 227, 227)
                                .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(186, 186, 186))
                            .addGroup(managerPanelLayout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(managerPanelLayout.createSequentialGroup()
                        .addGap(580, 580, 580)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(213, Short.MAX_VALUE))
        );
        managerPanelLayout.setVerticalGroup(
            managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(managerPanelLayout.createSequentialGroup()
                .addGap(130, 130, 130)
                .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(managerPanelLayout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(managerPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(110, 110, 110)
                .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(managerPanelLayout.createSequentialGroup()
                        .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75))))
        );

        getContentPane().add(managerPanel, "TrainingManager");
        managerPanel.getAccessibleContext().setAccessibleName("TrainingManager");

        titolo.setFont(new java.awt.Font("Tempus Sans ITC", 3, 48)); // NOI18N
        titolo.setForeground(new java.awt.Color(0, 153, 51));
        titolo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titolo.setText("Allenamento RoboGP");

        jScrollPane1.setViewportView(scrollCardPanel);

        backup.setMaximumSize(null);
        backup.setMinimumSize(null);
        backup.setName(""); // NOI18N
        backup.setPreferredSize(null);
        backup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backupActionPerformed(evt);
            }
        });

        move1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                move1ActionPerformed(evt);
            }
        });

        move2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                move2ActionPerformed(evt);
            }
        });

        move3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                move3ActionPerformed(evt);
            }
        });

        jLabel3.setText("Backup");

        jLabel4.setText("Move1");

        jLabel5.setText("Move2");

        jLabel6.setText("Move3");

        turnl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                turnlActionPerformed(evt);
            }
        });

        turnr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                turnrActionPerformed(evt);
            }
        });

        uturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uturnActionPerformed(evt);
            }
        });

        jLabel7.setText("TurnL");

        jLabel8.setText("TurnR");

        jLabel9.setText("Uturn");

        jLabel10.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jLabel10.setText("Schede Base:");

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(77, 77, 77))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(106, 106, 106))))
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(49, 49, 49))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(53, 53, 53))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(turnl, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(turnr, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addContainerGap(29, Short.MAX_VALUE)
                        .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(leftPanelLayout.createSequentialGroup()
                                .addComponent(backup, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(move1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(leftPanelLayout.createSequentialGroup()
                                .addComponent(move2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(move3, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, leftPanelLayout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(20, 20, 20)))
                .addGap(29, 29, 29))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(uturn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82))
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addComponent(jLabel10)
                .addGap(32, 32, 32)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(backup, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(jLabel3))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(move1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)))
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(move2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(move3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                        .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(28, 28, 28)
                        .addComponent(turnl, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(turnr, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(25, 25, 25)
                .addComponent(uturn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addGap(65, 65, 65))
        );

        play.setFont(new java.awt.Font("Segoe Script", 0, 18)); // NOI18N
        play.setLabel("Play");
        play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playActionPerformed(evt);
            }
        });

        pause.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        pause.setLabel("Pause");
        pause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseActionPerformed(evt);
            }
        });

        button3.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        button3.setLabel("Rewind");
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout controlActionPanelLayout = new javax.swing.GroupLayout(controlActionPanel);
        controlActionPanel.setLayout(controlActionPanelLayout);
        controlActionPanelLayout.setHorizontalGroup(
            controlActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlActionPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(controlActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pause, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(play, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        controlActionPanelLayout.setVerticalGroup(
            controlActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlActionPanelLayout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(play, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(144, 144, 144)
                .addComponent(pause, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103))
        );

        tabellone.setLeftComponent(controlActionPanel);

        labRuotaRobot.setText("Ruota Robot:");

        jLabel11.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jLabel11.setText("Istruzioni in coda:");

        javax.swing.GroupLayout gamePanelLayout = new javax.swing.GroupLayout(gamePanel);
        gamePanel.setLayout(gamePanelLayout);
        gamePanelLayout.setHorizontalGroup(
            gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamePanelLayout.createSequentialGroup()
                .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gamePanelLayout.createSequentialGroup()
                        .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(gamePanelLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(labRuotaRobot)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rotateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(gamePanelLayout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(jLabel11)))
                        .addGap(219, 219, 219)
                        .addComponent(titolo, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(gamePanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tabellone, javax.swing.GroupLayout.PREFERRED_SIZE, 812, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(leftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 41, Short.MAX_VALUE))
        );
        gamePanelLayout.setVerticalGroup(
            gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamePanelLayout.createSequentialGroup()
                .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gamePanelLayout.createSequentialGroup()
                        .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(gamePanelLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(labRuotaRobot))
                            .addGroup(gamePanelLayout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(rotateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(32, 32, 32)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1))
                    .addGroup(gamePanelLayout.createSequentialGroup()
                        .addComponent(titolo, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tabellone)
                            .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(gamePanel, "gamepanel");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (choosenRobot != null) {
            ((CardLayout) this.getContentPane().getLayout()).show(this.getContentPane(), "gamepanel");

            managerPanel.setBackground(new Color(204, 255, 209));

            //tabellone di gioco
            robodrome = new Robodrome("robodromes\\" + jComboBox1.getSelectedItem().toString() + ".txt");
            rdview = new RobodromeView(robodrome, 45);
            tabellone.setRightComponent(rdview);
            tabellone.setVisible(true);
            robmar = new RobotMarker(choosenRobot.getName(), choosenRobot.getColor());
            robmar.setDir(Direction.E);

            rdview.placeRobot(robmar, Direction.E, 5, 0, true);
            robmar.setX(0);
            robmar.setY(5);

            getContentPane().setBackground(new Color(204, 255, 209));
            controlActionPanel.setBackground(new Color(255, 204, 229));
            leftPanel.setBackground(new Color(204, 255, 209));

            //inserimento immagini e pulsanti relativi alle schede base
            backup.setIcon(backupimg);
            move1.setIcon(move1img);
            move2.setIcon(move2img);
            move3.setIcon(move3img);
            turnl.setIcon(turnlimg);
            turnr.setIcon(turnrimg);
            uturn.setIcon(uturnimg);
            rotateButton.setIcon(rotate90img);
            String[] columnNames = {"       Set Card", "        Delete"};
            Object[][] data
                    = {};

            model = new DefaultTableModel(data, columnNames);
            table = new JTable(model) {
                @Override
                public Class getColumnClass(int column) {
                    return getValueAt(0, column).getClass();
                }
            };

            //creazione del pulsante elimina nella seconda colonna della tabella
            Action delete = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JTable table = (JTable) e.getSource();
                    int modelRow = Integer.valueOf(e.getActionCommand());
                    ((DefaultTableModel) table.getModel()).removeRow(modelRow);
                }
            };

            ButtonColumn buttonColumn = new ButtonColumn(table, delete, 1);
            buttonColumn.setMnemonic(KeyEvent.VK_D);
            table.setPreferredScrollableViewportSize(table.getPreferredSize());

            //inserisco la tabella nello scrollpanel
            scrollCardPanel.setViewportView(table);

            //setto altezza righe tabella
            table.setRowHeight(60);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        choosenRobot = new RobotMarker("robot-red", "red");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        choosenRobot = new RobotMarker("robot-blue", "blue");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        choosenRobot = new RobotMarker("robot-emerald", "emerald");
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        choosenRobot = new RobotMarker("robot-turquoise", "turquoise");
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        choosenRobot = new RobotMarker("robot-yellow", "yellow");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        choosenRobot = new RobotMarker("robot-green", "green");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        choosenRobot = new RobotMarker("robot-orange", "orange");
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        choosenRobot = new RobotMarker("robot-violet", "violet");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void backupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backupActionPerformed
        if (firstistr == false) {
            rewindX = robmar.getX();
            rewindY = robmar.getY();
            rewindDir = robmar.getDir();
            firstistr = true;
        }
        System.out.println(robmar);
        System.out.println(robmar.getDir());
        rdview.addRobotMove(robmar, 1, robmar.getDir(), Rotation.NO);
        model.addRow(new Object[]{backupimg, "Elimina"});
    }//GEN-LAST:event_backupActionPerformed

    private void move1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_move1ActionPerformed
        if (firstistr == false) {
            rewindX = robmar.getX();
            rewindY = robmar.getY();
            rewindDir = robmar.getDir();
            firstistr = true;
        }
        if (checkWalls(robmar.getX(), robmar.getY(), robmar.getDir())) {    //se la strada è libera
            moveAndSetPosition(1);
            checkCell(robmar.getX(), robmar.getY());
        }
        rdview.addPause(500);
        model.addRow(new Object[]{move1img, "Elimina"});
    }//GEN-LAST:event_move1ActionPerformed

    private void move2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_move2ActionPerformed
        if (firstistr == false) {
            rewindX = robmar.getX();
            rewindY = robmar.getY();
            rewindDir = robmar.getDir();
            firstistr = true;
        }
        int movement = checkMoreWalls(robmar.getX(), robmar.getY(), robmar.getDir(), false);
        moveAndSetPosition(movement);
        checkCell(robmar.getX(), robmar.getY());
        rdview.addPause(500);
        model.addRow(new Object[]{move2img, "Elimina"});
    }//GEN-LAST:event_move2ActionPerformed

    private void move3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_move3ActionPerformed
        if (firstistr == false) {
            rewindX = robmar.getX();
            rewindY = robmar.getY();
            rewindDir = robmar.getDir();
            firstistr = true;
        }
        int movement = checkMoreWalls(robmar.getX(), robmar.getY(), robmar.getDir(), true);
        moveAndSetPosition(movement);
        checkCell(robmar.getX(), robmar.getY());
        rdview.addPause(500);
        model.addRow(new Object[]{move3img, "Elimina"});
    }//GEN-LAST:event_move3ActionPerformed

    private void turnlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_turnlActionPerformed
        if (firstistr == false) {
            rewindX = robmar.getX();
            rewindY = robmar.getY();
            rewindDir = robmar.getDir();
            firstistr = true;
        }
        rdview.addRobotMove(robmar, 0, robmar.getDir(), Rotation.CCW90);
        switch (robmar.getDir()) {
            case N:
                robmar.setDir(Direction.W);
                break;
            case S:
                robmar.setDir(Direction.E);
                break;
            case E:
                robmar.setDir(Direction.N);
                break;
            case W:
                robmar.setDir(Direction.S);
                break;
        }
        rdview.addPause(500);
        model.addRow(new Object[]{turnlimg, "Elimina"});
    }//GEN-LAST:event_turnlActionPerformed

    private void turnrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_turnrActionPerformed
        if (firstistr == false) {
            rewindX = robmar.getX();
            rewindY = robmar.getY();
            rewindDir = robmar.getDir();
            firstistr = true;
        }
        rdview.addRobotMove(robmar, 0, robmar.getDir(), Rotation.CW90);
        switch (robmar.getDir()) {
            case N:
                robmar.setDir(Direction.E);
                break;
            case S:
                robmar.setDir(Direction.W);
                break;
            case E:
                robmar.setDir(Direction.S);
                break;
            case W:
                robmar.setDir(Direction.N);
                break;
        }
        rdview.addPause(500);
        model.addRow(new Object[]{turnrimg, "Elimina"});
    }//GEN-LAST:event_turnrActionPerformed

    private void uturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uturnActionPerformed
        if (firstistr == false) {
            rewindX = robmar.getX();
            rewindY = robmar.getY();
            rewindDir = robmar.getDir();
            firstistr = true;
        }
        rdview.addRobotMove(robmar, 0, robmar.getDir(), Rotation.CCW180);
        switch (robmar.getDir()) {
            case N:
                robmar.setDir(Direction.S);
                break;
            case S:
                robmar.setDir(Direction.N);
                break;
            case E:
                robmar.setDir(Direction.W);
                break;
            case W:
                robmar.setDir(Direction.E);
                break;
        }
        rdview.addPause(500);
        model.addRow(new Object[]{uturnimg, "Elimina"});
    }//GEN-LAST:event_uturnActionPerformed

    private void playActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playActionPerformed
        firstistr = false;
        rdview.play();
    }//GEN-LAST:event_playActionPerformed

    private void pauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseActionPerformed
        rdview.finishAnimation(5, 5);
        rdview.removeAllAnimations();

    }//GEN-LAST:event_pauseActionPerformed

    private void button3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button3ActionPerformed
        robmar.setX(rewindX);
        robmar.setY(rewindY);
        robmar.setDir(rewindDir);
        rdview.placeRobot(robmar, rewindDir, rewindY, rewindX, true);
    }//GEN-LAST:event_button3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
        * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TrainingManagerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TrainingManagerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TrainingManagerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TrainingManagerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TrainingManagerApp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backup;
    private java.awt.Button button3;
    private javax.swing.JPanel controlActionPanel;
    private javax.swing.JPanel gamePanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labRuotaRobot;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel managerPanel;
    private javax.swing.JButton move1;
    private javax.swing.JButton move2;
    private javax.swing.JButton move3;
    private java.awt.Button pause;
    private java.awt.Button play;
    private javax.swing.JButton rotateButton;
    private javax.swing.JScrollPane scrollCardPanel;
    private javax.swing.JSplitPane tabellone;
    private javax.swing.JLabel titolo;
    private javax.swing.JButton turnl;
    private javax.swing.JButton turnr;
    private javax.swing.JButton uturn;
    // End of variables declaration//GEN-END:variables
}
