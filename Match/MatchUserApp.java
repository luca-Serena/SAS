package Match;

import static Match.Card.getRandomPool;
import connection.Connection;
import connection.Message;
import connection.MessageObserver;
import connection.PartnerShutDownException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import robogp.matchmanager.Match;
import robogp.matchmanager.MatchManagerApp;
import robogp.matchmanager.RobotMarker;
import robogp.robodrome.view.RobodromeView;

public class MatchUserApp extends javax.swing.JFrame implements MessageObserver {

    Card[] instructions = new Card[5];
    Card[] cardpool;
    int contcard = 0;
    private static MatchUserApp singleInstance;
    JTable table;
    DefaultTableModel model;
    JButton prova = new JButton("prova");
    Connection serverConn;
    boolean available;
    RobodromeView view;
    int idUser;

    public MatchUserApp() {
        initComponents();
        tabellone.setVisible(true);
        getContentPane().setBackground(new Color(204, 255, 209));
        controlActionPanel.setBackground(new Color(255, 204, 229));
        leftPanel.setBackground(new Color(204, 255, 209));

        //inserimento immagini e pulsanti relativi alle schede base
        String[] columnNames = {" Set Card"};
        Object[][] data = {};

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

        table.setPreferredScrollableViewportSize(table.getPreferredSize());

        //inserisco la tabella nello scrollpanel
        scrollCardPanel.setViewportView(table);

        //setto altezza righe tabella
        table.setRowHeight(60);
    }

    @Override
    public void notifyMessageReceived(Message msg) {
        System.out.println("***NEW Messaggio ricevuto:" + msg.getName());
        if (msg.getName().equals(Match.MatchJoinReplyMsg)) {
            boolean reply = (Boolean) msg.getParameter(0);
            System.out.println("\tRisposta: " + (reply ? "Accettato" : "Rifiutato"));
            if (reply) {
                RobotMarker[] robots = (RobotMarker[]) msg.getParameter(1);
                for (RobotMarker r : robots) {
                    System.out.println("\tRobot assegnato: " + r.getName() + " al dock " + r.getDock());
                }
                ((CardLayout) this.getContentPane().getLayout()).show(this.getContentPane(), "matchpanel");
                this.view=getRobodromeView();
                tabellone.setRightComponent(view);
                tabellone.setVisible(true);
                this.idUser = (Integer) msg.getParameter(2);

                setCards();
            }
        } else if (msg.getName().equals(Match.AvailableMessage)) {
            setCards();
            this.view=getRobodromeView();
            tabellone.setRightComponent(view);
        }
        else if (msg.getName().equals(Match.MatchView)){
       //     this.rdView = (RobodromeView) msg.getParameter(0);
        }
    }

    private void setCards() {
        cardpool = getRandomPool(9);
        card1.setIcon(cardpool[0].getImage());
        card1.setEnabled(true);
        card2.setIcon(cardpool[1].getImage());
        card2.setEnabled(true);
        card3.setIcon(cardpool[2].getImage());
        card3.setEnabled(true);
        card4.setIcon(cardpool[3].getImage());
        card4.setEnabled(true);
        card5.setIcon(cardpool[4].getImage());
        card5.setEnabled(true);
        card6.setIcon(cardpool[5].getImage());
        card6.setEnabled(true);
        card7.setIcon(cardpool[6].getImage());
        card7.setEnabled(true);
        card8.setIcon(cardpool[7].getImage());
        card8.setEnabled(true);
        card9.setIcon(cardpool[8].getImage());
        card9.setEnabled(true);
        model.setRowCount(0);
    }

    private void sendIstr() {
        if (this.instructions.length > 0) {
            Message msg = new Message(Match.MatchCommands);
            for (Card c : this.instructions) {
                if (c != null) {
                    c.setIdOwner(this.idUser);
                }
            }
            Object[] pars = new Object[1];
            pars[0] = instructions;
            msg.setParameters(pars);
            try {
                serverConn.sendMessage(msg);
            } catch (PartnerShutDownException ex) {
                Logger.getLogger(MatchUserApp.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        return MatchManagerApp.rdView;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        RequestSender = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        nameArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        passwordText = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        MatchPanel = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        titolo = new javax.swing.JLabel();
        leftPanel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        card2 = new javax.swing.JButton();
        card1 = new javax.swing.JButton();
        card3 = new javax.swing.JButton();
        card4 = new javax.swing.JButton();
        card5 = new javax.swing.JButton();
        card6 = new javax.swing.JButton();
        card7 = new javax.swing.JButton();
        card8 = new javax.swing.JButton();
        card9 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        scrollCardPanel = new javax.swing.JScrollPane();
        tabellone = new javax.swing.JSplitPane();
        controlActionPanel = new javax.swing.JPanel();
        play = new java.awt.Button();
        checkPoint = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TrainingApp");
        setBackground(new java.awt.Color(204, 255, 204));
        setName("mainFrame"); // NOI18N
        getContentPane().setLayout(new java.awt.CardLayout());

        nameArea.setColumns(20);
        nameArea.setRows(5);
        jScrollPane1.setViewportView(nameArea);

        jLabel1.setText("Inserisci il nome dell'utente");

        jLabel2.setText("Inserisci la password per accedere");

        passwordText.setColumns(20);
        passwordText.setRows(5);
        jScrollPane2.setViewportView(passwordText);

        jButton1.setText("Invia richiesta");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout RequestSenderLayout = new javax.swing.GroupLayout(RequestSender);
        RequestSender.setLayout(RequestSenderLayout);
        RequestSenderLayout.setHorizontalGroup(
            RequestSenderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RequestSenderLayout.createSequentialGroup()
                .addGap(0, 522, Short.MAX_VALUE)
                .addGroup(RequestSenderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RequestSenderLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(504, 504, 504))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RequestSenderLayout.createSequentialGroup()
                        .addGroup(RequestSenderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(481, 481, 481))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RequestSenderLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(541, 541, 541))
            .addGroup(RequestSenderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(46, 46, 46))
        );
        RequestSenderLayout.setVerticalGroup(
            RequestSenderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RequestSenderLayout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 272, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(165, 165, 165))
        );

        getContentPane().add(RequestSender, "card2");
        RequestSender.getAccessibleContext().setAccessibleName("RequestSender");

        jLabel16.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jLabel16.setText("Istruzioni in coda:");

        titolo.setFont(new java.awt.Font("Tempus Sans ITC", 3, 48)); // NOI18N
        titolo.setForeground(new java.awt.Color(0, 153, 51));
        titolo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titolo.setText("Match RoboGP");

        jLabel15.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jLabel15.setText("Schede:");

        card2.setText("Card2");
        card2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card2ActionPerformed(evt);
            }
        });

        card1.setText("Card1");
        card1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card1ActionPerformed(evt);
            }
        });

        card3.setText("Card3");
        card3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card3ActionPerformed(evt);
            }
        });

        card4.setText("Card4");
        card4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card4ActionPerformed(evt);
            }
        });

        card5.setText("Card5");
        card5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card5ActionPerformed(evt);
            }
        });

        card6.setText("Card6");
        card6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card6ActionPerformed(evt);
            }
        });

        card7.setText("Card7");
        card7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card7ActionPerformed(evt);
            }
        });

        card8.setText("Card8");
        card8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card8ActionPerformed(evt);
            }
        });

        card9.setText("Card9");
        card9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(leftPanelLayout.createSequentialGroup()
                                .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(leftPanelLayout.createSequentialGroup()
                                .addComponent(card3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(card4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                                .addComponent(card5, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(card6, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(leftPanelLayout.createSequentialGroup()
                                .addComponent(card7, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(card8, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(74, 74, 74))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                                .addComponent(card9, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(65, 65, 65))))))
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addComponent(jLabel15)
                .addGap(29, 29, 29)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(card3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(card5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(card7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(card9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        card2.getAccessibleContext().setAccessibleName("2");
        card1.getAccessibleContext().setAccessibleName("card1");
        card3.getAccessibleContext().setAccessibleName("3");
        card4.getAccessibleContext().setAccessibleName("4");
        card5.getAccessibleContext().setAccessibleName("5");
        card6.getAccessibleContext().setAccessibleName("6");
        card7.getAccessibleContext().setAccessibleName("7");
        card8.getAccessibleContext().setAccessibleName("8");
        card9.getAccessibleContext().setAccessibleName("9");

        jScrollPane4.setViewportView(scrollCardPanel);

        play.setFont(new java.awt.Font("Segoe Script", 0, 18)); // NOI18N
        play.setLabel("RUN");
        play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout controlActionPanelLayout = new javax.swing.GroupLayout(controlActionPanel);
        controlActionPanel.setLayout(controlActionPanelLayout);
        controlActionPanelLayout.setHorizontalGroup(
            controlActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlActionPanelLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(play, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(69, Short.MAX_VALUE))
        );
        controlActionPanelLayout.setVerticalGroup(
            controlActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlActionPanelLayout.createSequentialGroup()
                .addGap(249, 249, 249)
                .addComponent(play, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(287, Short.MAX_VALUE))
        );

        tabellone.setLeftComponent(controlActionPanel);

        checkPoint.setText("checkpoint : 0");

        javax.swing.GroupLayout MatchPanelLayout = new javax.swing.GroupLayout(MatchPanel);
        MatchPanel.setLayout(MatchPanelLayout);
        MatchPanelLayout.setHorizontalGroup(
            MatchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MatchPanelLayout.createSequentialGroup()
                .addGroup(MatchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MatchPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(tabellone, javax.swing.GroupLayout.PREFERRED_SIZE, 812, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(leftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(MatchPanelLayout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(checkPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(167, 167, 167)
                        .addComponent(titolo, javax.swing.GroupLayout.PREFERRED_SIZE, 697, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(MatchPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MatchPanelLayout.setVerticalGroup(
            MatchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MatchPanelLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(MatchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titolo)
                    .addComponent(checkPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(69, 69, 69)
                .addComponent(jLabel16)
                .addGroup(MatchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MatchPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(MatchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tabellone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MatchPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(leftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22))))
        );

        tabellone.getAccessibleContext().setAccessibleName("tabellone");

        getContentPane().add(MatchPanel, "matchpanel");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void move3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_move3ActionPerformed
        //   sendIstr("move3");
    }//GEN-LAST:event_move3ActionPerformed

    private void backupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backupActionPerformed
        // sendIstr("backup");
    }//GEN-LAST:event_backupActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            InetAddress address = InetAddress.getByName("localhost");
            Connection conn = Connection.connectToHost(address, 2222);
            conn.addMessageObserver(this);
            this.serverConn = conn;
            Message msg = new Message(Match.MatchJoinRequestMsg);
            Object[] pars = new Object[2];
            pars[0] = nameArea.getText();
            pars[1] = passwordText.getText();
            msg.setParameters(pars);
            conn.sendMessage(msg);
        } catch (PartnerShutDownException | IOException e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void playActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playActionPerformed
        sendIstr();
    }//GEN-LAST:event_playActionPerformed

    private void card1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card1ActionPerformed
        if (contcard < 5) {
            instructions[contcard] = cardpool[0];
            contcard++;
            model.addRow(new Object[]{cardpool[0].getImage()});
            card1.setEnabled(false);
        }
    }//GEN-LAST:event_card1ActionPerformed

    private void card2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card2ActionPerformed
        if (contcard < 5) {
            instructions[contcard] = cardpool[1];
            contcard++;
            model.addRow(new Object[]{cardpool[1].getImage()});
            card2.setEnabled(false);
        }
    }//GEN-LAST:event_card2ActionPerformed

    private void card3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card3ActionPerformed
        if (contcard < 5) {
            instructions[contcard] = cardpool[2];
            contcard++;
            model.addRow(new Object[]{cardpool[2].getImage()});
            card3.setEnabled(false);
        }
    }//GEN-LAST:event_card3ActionPerformed

    private void card4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card4ActionPerformed
        if (contcard < 5) {
            instructions[contcard] = cardpool[3];
            contcard++;
            model.addRow(new Object[]{cardpool[3].getImage()});
            card4.setEnabled(false);
        }
    }//GEN-LAST:event_card4ActionPerformed

    private void card5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card5ActionPerformed
        if (contcard < 5) {
            instructions[contcard] = cardpool[4];
            contcard++;
            model.addRow(new Object[]{cardpool[4].getImage()});
            card5.setEnabled(false);
        }
    }//GEN-LAST:event_card5ActionPerformed

    private void card6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card6ActionPerformed
        if (contcard < 5) {
            instructions[contcard] = cardpool[5];
            contcard++;
            model.addRow(new Object[]{cardpool[5].getImage()});
            card6.setEnabled(false);
        }
    }//GEN-LAST:event_card6ActionPerformed

    private void card7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card7ActionPerformed
        if (contcard < 5) {
            instructions[contcard] = cardpool[6];
            contcard++;
            model.addRow(new Object[]{cardpool[6].getImage()});
            card7.setEnabled(false);
        }
    }//GEN-LAST:event_card7ActionPerformed

    private void card8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card8ActionPerformed
        if (contcard < 5) {
            instructions[contcard] = cardpool[7];
            contcard++;
            model.addRow(new Object[]{cardpool[7].getImage()});
            card8.setEnabled(false);
        }
    }//GEN-LAST:event_card8ActionPerformed

    private void card9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card9ActionPerformed
        if (contcard < 5) {
            instructions[contcard] = cardpool[8];
            contcard++;
            model.addRow(new Object[]{cardpool[8].getImage()});
            card9.setEnabled(false);
        }
    }//GEN-LAST:event_card9ActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
        * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */

        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            MatchUserApp.singleInstance = new MatchUserApp();
            MatchUserApp.singleInstance.setVisible(true);

        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MatchPanel;
    private javax.swing.JPanel RequestSender;
    private javax.swing.JButton card1;
    private javax.swing.JButton card2;
    private javax.swing.JButton card3;
    private javax.swing.JButton card4;
    private javax.swing.JButton card5;
    private javax.swing.JButton card6;
    private javax.swing.JButton card7;
    private javax.swing.JButton card8;
    private javax.swing.JButton card9;
    private javax.swing.JLabel checkPoint;
    private javax.swing.JPanel controlActionPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JTextArea nameArea;
    private javax.swing.JTextArea passwordText;
    private java.awt.Button play;
    private javax.swing.JScrollPane scrollCardPanel;
    private javax.swing.JSplitPane tabellone;
    private javax.swing.JLabel titolo;
    // End of variables declaration//GEN-END:variables
}
