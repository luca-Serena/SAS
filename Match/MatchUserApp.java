package Match;

import Training.ButtonColumn;
import connection.Connection;
import connection.Message;
import connection.PartnerShutDownException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import robogp.matchmanager.Match;
import robogp.matchmanager.RobotMarker;
import robogp.robodrome.*;
import robogp.robodrome.view.RobodromeView;

public class MatchUserApp extends javax.swing.JFrame {

    JTable table;
    DefaultTableModel model;
    JButton prova = new JButton("prova");
    static RobodromeView rdview;
    RobotMarker robmar;
    Robodrome robodrome;
    Connection serverConn;
    boolean available;

    //immagini caricate e scalate
    ImageIcon backupimg = new ImageIcon(new ImageIcon("tiles/card-backup.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon move1img = new ImageIcon(new ImageIcon("tiles/card-move1.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon move2img = new ImageIcon(new ImageIcon("tiles/card-move2.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon move3img = new ImageIcon(new ImageIcon("tiles/card-move3.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon turnlimg = new ImageIcon(new ImageIcon("tiles/card-turnL.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon turnrimg = new ImageIcon(new ImageIcon("tiles/card-turnR.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon uturnimg = new ImageIcon(new ImageIcon("tiles/card-uturn.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    ImageIcon rotate90img = new ImageIcon(new ImageIcon("tiles/rotate90.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));

    public MatchUserApp(Robodrome rd, RobotMarker rm, int numcell, RobodromeView rdview) {
        initComponents();
        tabellone.setRightComponent(rdview);
        tabellone.setVisible(true);
        this.robodrome = rd;
        robmar = rm;

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

        backup.addActionListener(new addButtonBackupListener());
        move1.addActionListener(new addButtonMove1Listener());
        move2.addActionListener(new addButtonMove2Listener());
        move3.addActionListener(new addButtonMove3Listener());
        turnl.addActionListener(new addButtonTurnlListener());
        turnr.addActionListener(new addButtonTurnrListener());

        String[] columnNames = {"       Set Card", "        Delete"};
        Object[][] data
                = {};

        model = new DefaultTableModel(data, columnNames);
        table = new JTable(model) {
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
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

    private void sendIstr(String istr) {
        Message msg = new Message(Match.MatchCommands);
        Object[] pars = new Object[1];
        pars[0] = istr;
        msg.setParameters(pars);
        try {
            serverConn.sendMessage(msg);
        } catch (PartnerShutDownException ex) {
            Logger.getLogger(MatchUserApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class addButtonPlayListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            rdview.play();
        }
    }

    class addButtonPauseListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            rdview.addPause(3000);
        }
    }

    class addButtonRotateButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            rdview.addRobotMove(robmar, 0, Direction.E, Rotation.CW90);
            sendIstr("rotate");
        }
    }

    //action listener dei pulsanti che compongono il set base di carte permette l'aggiunta di tale carta alla tabella
    class addButtonBackupListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            rdview.addRobotMove(robmar, 1, robmar.getDir(), Rotation.NO);
            model.addRow(new Object[]{backupimg, "Elimina"});
        }
    }

    class addButtonMove1Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            sendIstr("move1");
        }
    }

    class addButtonMove2Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            sendIstr("move2");
        }
    }

    class addButtonMove3Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            sendIstr("move3");
        }
    }

    class addButtonTurnlListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            sendIstr("turnl");
        }
    }

    class addButtonTurnrListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            sendIstr("turnr");
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
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TrainingApp");
        setBackground(new java.awt.Color(204, 255, 204));
        setName("mainFrame"); // NOI18N

        titolo.setFont(new java.awt.Font("Tempus Sans ITC", 3, 48)); // NOI18N
        titolo.setForeground(new java.awt.Color(0, 153, 51));
        titolo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titolo.setText("Partita RoboGP");

        jScrollPane1.setViewportView(scrollCardPanel);

        backup.setMaximumSize(null);
        backup.setMinimumSize(null);
        backup.setName(""); // NOI18N
        backup.setPreferredSize(null);

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
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(move2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(turnl, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(backup, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(27, 27, 27)))
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(move3, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(turnr, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(move1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(53, 53, 53))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(75, 75, 75))))
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(uturn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(77, 77, 77))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(115, 115, 115)
                .addComponent(jLabel4)
                .addGap(75, 75, 75))
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGap(147, 147, 147)
                        .addComponent(jLabel7))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(jLabel8)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(171, 171, 171)
                .addComponent(jLabel8)
                .addGap(31, 31, 31)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(move1, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(backup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(move3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(turnr, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(move2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(turnl, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(uturn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        uturn.getAccessibleContext().setAccessibleName("uturn");
        jLabel5.getAccessibleContext().setAccessibleName("turnl");
        jLabel6.getAccessibleContext().setAccessibleName("turnr");

        javax.swing.GroupLayout controlActionPanelLayout = new javax.swing.GroupLayout(controlActionPanel);
        controlActionPanel.setLayout(controlActionPanelLayout);
        controlActionPanelLayout.setHorizontalGroup(
            controlActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        controlActionPanelLayout.setVerticalGroup(
            controlActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 594, Short.MAX_VALUE)
        );

        tabellone.setLeftComponent(controlActionPanel);

        jLabel9.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jLabel9.setText("Istruzioni in coda:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabellone, javax.swing.GroupLayout.PREFERRED_SIZE, 812, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(leftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(titolo, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(406, 406, 406))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jLabel9)
                        .addGap(16, 16, 16))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(titolo, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(leftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(tabellone, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void turnrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_turnrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_turnrActionPerformed

    private void turnlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_turnlActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_turnlActionPerformed

    private void move1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_move1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_move1ActionPerformed

    private void move2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_move2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_move2ActionPerformed

    private void uturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uturnActionPerformed

    }//GEN-LAST:event_uturnActionPerformed

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
            java.util.logging.Logger.getLogger(MatchUserApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MatchUserApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MatchUserApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MatchUserApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backup;
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
    private javax.swing.JPanel leftPanel;
    private javax.swing.JButton move1;
    private javax.swing.JButton move2;
    private javax.swing.JButton move3;
    private javax.swing.JScrollPane scrollCardPanel;
    private javax.swing.JSplitPane tabellone;
    private javax.swing.JLabel titolo;
    private javax.swing.JButton turnl;
    private javax.swing.JButton turnr;
    private javax.swing.JButton uturn;
    // End of variables declaration//GEN-END:variables
}
