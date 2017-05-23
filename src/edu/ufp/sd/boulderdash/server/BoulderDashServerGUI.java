package edu.ufp.sd.boulderdash.server;

import edu.ufp.sd.boulderdash.server.game.LevelModelRoom;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;

/**
 * <p>
 * Title: Projecto SD - BoulderDash - BoulderDash</p>
 * <p>
 * Description: Jogo BoulderDash destribuido</p>
 * <p>
 * Copyright: Copyright (c) 2017</p>
 * <p>
 * Company: UFP </p>
 *
 * @author Tiago Cardoso <tiagocardosoweb@gmail.com>
 * @author Miguel Ferreira <migueelfsf@gmail.com>
 * @version 1.0
 */
public class BoulderDashServerGUI extends javax.swing.JFrame implements WindowListener {

    private BoulderDashServerImpl bds;
    private DefaultListModel<String> playerList = new DefaultListModel<>();
    private DefaultListModel<String> roomsList = new DefaultListModel<>();

    /**
     * Creates new form BouderDashServerGUI
     */
    public BoulderDashServerGUI(BoulderDashServerImpl bds) {
        this.bds = bds;
        initComponents();

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelGeral = new javax.swing.JPanel();
        jpInformation = new javax.swing.JPanel();
        lblConnectedClients = new javax.swing.JLabel();
        lblConnectedClientsValue = new javax.swing.JLabel();
        lblLobbys = new javax.swing.JLabel();
        lblLobbysValue = new javax.swing.JLabel();
        jPanelLobbys = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jlRooms = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        btnKillAll = new javax.swing.JButton();
        btnNewRoom = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanelPlayers = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jlPlayers = new javax.swing.JList<>();
        btnKickAll = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnShutdown = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jpInformation.setBorder(javax.swing.BorderFactory.createTitledBorder("Information"));
        jpInformation.setToolTipText("Information");

        lblConnectedClients.setText("Connected Clients:");

        lblConnectedClientsValue.setText("0");

        lblLobbys.setText("Lobbys:");

        lblLobbysValue.setText("0");

        javax.swing.GroupLayout jpInformationLayout = new javax.swing.GroupLayout(jpInformation);
        jpInformation.setLayout(jpInformationLayout);
        jpInformationLayout.setHorizontalGroup(
            jpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpInformationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpInformationLayout.createSequentialGroup()
                        .addComponent(lblConnectedClients)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblConnectedClientsValue))
                    .addGroup(jpInformationLayout.createSequentialGroup()
                        .addComponent(lblLobbys)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblLobbysValue)))
                .addContainerGap(393, Short.MAX_VALUE))
        );
        jpInformationLayout.setVerticalGroup(
            jpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpInformationLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblConnectedClients)
                    .addComponent(lblConnectedClientsValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLobbys)
                    .addComponent(lblLobbysValue))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelGeralLayout = new javax.swing.GroupLayout(jPanelGeral);
        jPanelGeral.setLayout(jPanelGeralLayout);
        jPanelGeralLayout.setHorizontalGroup(
            jPanelGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGeralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpInformation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelGeralLayout.setVerticalGroup(
            jPanelGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGeralLayout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(jpInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Geral", jPanelGeral);

        jlRooms.setModel(roomsList);
        jlRooms.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jlRooms.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlRoomsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jlRooms);

        jLabel1.setText("Options");

        btnKillAll.setText("Kill All");
        btnKillAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKillAllActionPerformed(evt);
            }
        });

        btnNewRoom.setText("New");
        btnNewRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewRoomActionPerformed(evt);
            }
        });

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelLobbysLayout = new javax.swing.GroupLayout(jPanelLobbys);
        jPanelLobbys.setLayout(jPanelLobbysLayout);
        jPanelLobbysLayout.setHorizontalGroup(
            jPanelLobbysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLobbysLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLobbysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLobbysLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(30, 30, 30))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLobbysLayout.createSequentialGroup()
                        .addGroup(jPanelLobbysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnKillAll, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                            .addComponent(btnNewRoom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanelLobbysLayout.setVerticalGroup(
            jPanelLobbysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
            .addGroup(jPanelLobbysLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNewRoom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnKillAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Rooms", jPanelLobbys);

        jlPlayers.setModel(playerList);
        jlPlayers.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jlPlayers.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        jlPlayers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlPlayersMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jlPlayers);

        btnKickAll.setText("Kick All");

        jLabel2.setText("Options");

        javax.swing.GroupLayout jPanelPlayersLayout = new javax.swing.GroupLayout(jPanelPlayers);
        jPanelPlayers.setLayout(jPanelPlayersLayout);
        jPanelPlayersLayout.setHorizontalGroup(
            jPanelPlayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPlayersLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPlayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPlayersLayout.createSequentialGroup()
                        .addComponent(btnKickAll, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanelPlayersLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(29, 29, 29))))
        );
        jPanelPlayersLayout.setVerticalGroup(
            jPanelPlayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPlayersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnKickAll)
                .addGap(0, 243, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jTabbedPane.addTab("Players", jPanelPlayers);

        btnShutdown.setText("Shutdown Server");
        btnShutdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShutdownActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnShutdown, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnShutdown)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnShutdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShutdownActionPerformed
        this.bds.shutdown();
    }//GEN-LAST:event_btnShutdownActionPerformed

    private void btnKillAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKillAllActionPerformed
        try {
            for (LevelModelRoom room: this.bds.rooms) {
                room.setGameRunning(false);
            }
            this.bds.rooms.clear();
            this.roomsList.removeAllElements();
            this.bds.setState(new State().new NewRoom(true, null));
        } catch (RemoteException ex) {
            Logger.getLogger(BoulderDashServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnKillAllActionPerformed

    private void jlRoomsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlRoomsMouseClicked
        JList theList = (JList) evt.getSource();
        if (evt.getClickCount() == 2) {
            int index = theList.locationToIndex(evt.getPoint());
            if (index >= 0) {
                Object o = theList.getModel().getElementAt(index);
                String roomName = o.toString();
                System.out.println(roomName);
                int roomID = this.bds.getRoomIndexByName(roomName);
                this.roomsList.removeElement(roomName);
                this.bds.rooms.get(roomID).setGameRunning(false);
                this.bds.rooms.remove(roomID);
            }
        }
    }//GEN-LAST:event_jlRoomsMouseClicked

    private void jlPlayersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlPlayersMouseClicked
        JList theList = (JList) evt.getSource();
        if (evt.getClickCount() == 2) {
            int index = theList.locationToIndex(evt.getPoint());
            if (index >= 0) {
                Object o = theList.getModel().getElementAt(index);
                String username = o.toString();
                try {
                    this.bds.setState(new State().new Disconnect(1, "Your were kicked!"));
                    this.bds.clients.remove(this.bds.clientFromUsername(username));
                } catch (RemoteException ex) {
                    Logger.getLogger(BoulderDashServerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }//GEN-LAST:event_jlPlayersMouseClicked

    private void btnNewRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewRoomActionPerformed
        try {
            this.bds.createGameRoom();
        } catch (RemoteException ex) {
            Logger.getLogger(BoulderDashServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnNewRoomActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
    }//GEN-LAST:event_jButton1ActionPerformed

    protected void addConnectedClient(String username, int value) {
        this.playerList.addElement(username);
        this.lblConnectedClientsValue.setText(String.valueOf(value));
        try {
            this.bds.setState(new State().new ConnectedClients(value));
        } catch (RemoteException ex) {
            Logger.getLogger(BoulderDashServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void removeConnectedClient(String username, int value) {
        this.playerList.removeElement(username);
        this.lblConnectedClientsValue.setText(String.valueOf(value));
        try {
            this.bds.setState(new State().new ConnectedClients(value));
        } catch (RemoteException ex) {
            Logger.getLogger(BoulderDashServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void addRoomToList(String name) {
        this.roomsList.addElement(name);
    }

    protected void removeRoomFromList(String name) {
        this.roomsList.removeElement(name);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKickAll;
    private javax.swing.JButton btnKillAll;
    private javax.swing.JButton btnNewRoom;
    private javax.swing.JButton btnShutdown;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelGeral;
    private javax.swing.JPanel jPanelLobbys;
    private javax.swing.JPanel jPanelPlayers;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JList<String> jlPlayers;
    private javax.swing.JList<String> jlRooms;
    private javax.swing.JPanel jpInformation;
    private javax.swing.JLabel lblConnectedClients;
    private javax.swing.JLabel lblConnectedClientsValue;
    private javax.swing.JLabel lblLobbys;
    private javax.swing.JLabel lblLobbysValue;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            try {
                this.bds.setState(new State().new Disconnect(0, "Server is closing..."));
            } catch (RemoteException ex) {
                Logger.getLogger(BoulderDashServerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(BoulderDashServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
