package Training;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import robogp.matchmanager.RobotMarker;
import robogp.robodrome.*;
import robogp.robodrome.view.RobodromeView;

public class TrainingApp extends javax.swing.JFrame {

    String nomrob = "robot1";
    String colrob = "green";
    JTable table;
    DefaultTableModel model;
    JButton prova = new JButton("prova");
    static RobodromeView rdview;
    RobotMarker robmar;
    Robodrome robodrome;

    //immagini caricate e scalate
    ImageIcon backupimg = new ImageIcon(new ImageIcon("tiles/card-backup.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon move1img = new ImageIcon(new ImageIcon("tiles/card-move1.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon move2img = new ImageIcon(new ImageIcon("tiles/card-move2.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon move3img = new ImageIcon(new ImageIcon("tiles/card-move3.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon turnlimg = new ImageIcon(new ImageIcon("tiles/card-turnL.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon turnrimg = new ImageIcon(new ImageIcon("tiles/card-turnR.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon uturnimg = new ImageIcon(new ImageIcon("tiles/card-uturn.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon rotate90img = new ImageIcon(new ImageIcon("tiles/rotate90.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));

    public TrainingApp(Robodrome rd, RobotMarker rm, int numcell) {

        initComponents();
        rdview = new RobodromeView(rd, numcell);
        tabellone.setRightComponent(rdview);
        tabellone.setVisible(true);
        this.robodrome = rd;
        robmar = rm;

        rdview.placeRobot(rm, Direction.E, 5, 0, true);
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
                    realY = getRightY (robmar.getY(), false, 1);
                    if (!checkWalls(robmar.getX(), realY, Direction.S)) {
                        return 1;
                    }
                    break;
                case N:
                    realY = getRightY (robmar.getY(), true, 1);
                    if (!checkWalls(robmar.getX(), realY, Direction.N)) {
                        return 1;
                    }
                    break;
                case E:
                    realX = getRightX (robmar.getX(), false, 1);
                    if (!checkWalls(realX, robmar.getY(), Direction.E)) {
                        return 1;
                    }
                    break;
                case W:
                    realX = getRightX (robmar.getX(), true, 1);
                    if (!checkWalls(realX, robmar.getY(), Direction.W)) {
                        return 1;
                    }
                    break;
            }
        }
        if (moveThree) {
            switch (robmar.getDir()) {
                case S:
                    realY = getRightY (robmar.getY(), false, 2);
                    if (!checkWalls(robmar.getX(), realY, Direction.S)) {
                        return 2;
                    }
                    break;
                case N:
                    realY = getRightY (robmar.getY(), true, 2);
                    if (!checkWalls(robmar.getX(), realY, Direction.N)) {
                        return 2;
                    }
                    break;
                case E:
                      realX = getRightX (robmar.getX(), false, 2);
                    if (!checkWalls(realX, robmar.getY(), Direction.E)) {
                        return 2;
                    }
                    break;
                case W:
                     realX = getRightX (robmar.getX(), true, 2);
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        titolo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        scrollCardPanel = new javax.swing.JScrollPane();
        leftPanel = new javax.swing.JPanel();
        backup = new javax.swing.JButton();
        move1 = new javax.swing.JButton();
        move2 = new javax.swing.JButton();
        move3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        turnl = new javax.swing.JButton();
        turnr = new javax.swing.JButton();
        uturn = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tabellone = new javax.swing.JSplitPane();
        controlActionPanel = new javax.swing.JPanel();
        play = new java.awt.Button();
        pause = new java.awt.Button();
        button3 = new java.awt.Button();
        labRuotaRobot = new javax.swing.JLabel();
        rotateButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TrainingApp");
        setBackground(new java.awt.Color(204, 255, 204));
        setName("mainFrame"); // NOI18N

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

        jLabel1.setText("Backup");

        jLabel2.setText("Move1");

        jLabel3.setText("Move2");

        jLabel4.setText("Move3");

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

        jLabel5.setText("TurnL");

        jLabel6.setText("TurnR");

        jLabel7.setText("Uturn");

        jLabel8.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jLabel8.setText("Schede Base:");

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(63, 63, 63))
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(jLabel8))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(leftPanelLayout.createSequentialGroup()
                                .addComponent(turnl, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(turnr, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, leftPanelLayout.createSequentialGroup()
                                .addComponent(move2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)
                                .addComponent(move3, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(backup, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(uturn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(leftPanelLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jLabel7)))))
                .addContainerGap(42, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(66, 66, 66))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(move1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)))
                .addGap(65, 65, 65))
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backup, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(move1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(move2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(move3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(23, 23, 23)
                        .addComponent(turnr, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(turnl, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addGap(29, 29, 29)
                .addComponent(uturn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addContainerGap(158, Short.MAX_VALUE))
        );

        uturn.getAccessibleContext().setAccessibleName("uturn");
        jLabel5.getAccessibleContext().setAccessibleName("turnl");
        jLabel6.getAccessibleContext().setAccessibleName("turnr");

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

        jLabel9.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jLabel9.setText("Istruzioni in coda:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(labRuotaRobot)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rotateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel9))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tabellone, javax.swing.GroupLayout.PREFERRED_SIZE, 812, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(leftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(184, 184, 184)
                        .addComponent(titolo, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 42, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(labRuotaRobot))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(rotateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(leftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(titolo, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(tabellone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void turnrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_turnrActionPerformed
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

    private void turnlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_turnlActionPerformed
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

    private void move1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_move1ActionPerformed
       if (checkWalls(robmar.getX(), robmar.getY(), robmar.getDir())) {    //se la strada è libera
                moveAndSetPosition(1);
                checkCell(robmar.getX(), robmar.getY());
            }
            rdview.addPause(500);
            model.addRow(new Object[]{move1img, "Elimina"});
    }//GEN-LAST:event_move1ActionPerformed

    private void move2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_move2ActionPerformed
        int movement = checkMoreWalls(robmar.getX(), robmar.getY(), robmar.getDir(), false);
            moveAndSetPosition(movement);
            checkCell(robmar.getX(), robmar.getY());
            rdview.addPause(500);
            model.addRow(new Object[]{move2img, "Elimina"});
    }//GEN-LAST:event_move2ActionPerformed

    private void uturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uturnActionPerformed
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
         rdview.play();
    }//GEN-LAST:event_playActionPerformed

    private void move3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_move3ActionPerformed
         int movement = checkMoreWalls(robmar.getX(), robmar.getY(), robmar.getDir(), true);
            moveAndSetPosition(movement);
            checkCell(robmar.getX(), robmar.getY());
            rdview.addPause(500);
            model.addRow(new Object[]{move3img, "Elimina"});
    }//GEN-LAST:event_move3ActionPerformed

    private void backupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backupActionPerformed
        rdview.addRobotMove(robmar, 1, robmar.getDir(), Rotation.NO);
            model.addRow(new Object[]{backupimg, "Elimina"});
    }//GEN-LAST:event_backupActionPerformed

    private void pauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseActionPerformed
        rdview.addPause(3000);
    }//GEN-LAST:event_pauseActionPerformed

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
            java.util.logging.Logger.getLogger(TrainingApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TrainingApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TrainingApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TrainingApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            if (args.length > 0) {
                System.out.println(args[1]);
                Robodrome r = new Robodrome(args[0]);
                RobotMarker robotMarker = new RobotMarker(args[1], args[2]);
                robotMarker.setDir(Direction.E);
                new TrainingApp(r, robotMarker, 45).setVisible(true);
            } else {
                Robodrome r = new Robodrome("C:\\Users\\utente\\Documents\\NetBeansProjects\\RoboGP\\robodromes\\riskyexchange.txt");
                RobotMarker robotMarker = new RobotMarker("robot-red", "red");
                robotMarker.setDir(Direction.E);
                new TrainingApp(r, robotMarker, 35).setVisible(true);
            }
        });

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backup;
    private java.awt.Button button3;
    private javax.swing.JPanel controlActionPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labRuotaRobot;
    private javax.swing.JPanel leftPanel;
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
