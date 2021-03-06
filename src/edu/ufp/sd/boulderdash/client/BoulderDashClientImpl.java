package edu.ufp.sd.boulderdash.client;

import edu.ufp.sd.boulderdash.server.BoulderDashServerRI;
import edu.ufp.sd.boulderdash.server.State;
import fr.enssat.BoulderDash.controllers.GameController;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * <p>
 * Title: Distributed Systems Project - BoulderDash</p>
 * <p>
 * Description: BoulderDash Game Multiplayer - Distributed using RMI</p>
 * <p>
 * Copyright: Copyright (c) 2017</p>
 * <p>
 * Company: UFP </p>
 *
 * @author Tiago Cardoso <tiagocardosoweb@gmail.com>
 * @author Miguel Ferreira <migueelfsf@gmail.com>
 * @version 0.1
 */
public class BoulderDashClientImpl implements BoulderDashClientRI {

    private Object lastState;

    protected BoulderDashServerRI bdsRI;

    private BoulderDashClientUserGUI bdcLoginUI = new BoulderDashClientUserGUI(this);
    private BoulderDashClientHallGUI bdcHallUI = null;

    private String username;
    private String password;
    private boolean loggedin;
    private boolean playing = false;

    private GameController gameController = null;

    public BoulderDashClientImpl(BoulderDashServerRI bdsRI) throws RemoteException {
        exportObjectMethod();
        this.bdsRI = bdsRI;
    }

    private void exportObjectMethod() {
        try {
            UnicastRemoteObject.exportObject(this, 0);
        } catch (RemoteException e) {
            System.out.println("BoulderDashClientImpl: " + e.getMessage());
        }
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedin() {
        return loggedin;
    }

    public void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }

    public Object getLastState() {
        return lastState;
    }

    public void setLastState(Object lastState) {
        this.lastState = lastState;
    }

    public BoulderDashServerRI getBdsRI() {
        return bdsRI;
    }

    public void setBdsRI(BoulderDashServerRI bdsRI) {
        this.bdsRI = bdsRI;
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        System.out.println("BoulderDashClientImpl - sendMessage(): " + message);
    }

    @Override
    public String getClientUsername() throws RemoteException {
        return this.username;
    }

    @Override
    public void update() throws RemoteException {
        System.out.println("BoulderDashClientImpl - update(): ");
        this.lastState = this.bdsRI.getState();
        if (lastState instanceof State.Message) {
            System.out.println("BoulderDashClientImpl - update(): State = MessageState ");
            if (bdcHallUI != null) {
                bdcHallUI.updateMesssages();
            }
        } else if (lastState instanceof State.ConnectedClients) {
            System.out.println("BoulderDashClientImpl - update(): State = ConnectedClients ");
            if (bdcHallUI != null) {
                bdcHallUI.updateClients();
            }
        } else if (lastState instanceof State.NewRoom) {
            System.out.println("BoulderDashClientImpl - update(): State = NewRoom ");
            State.NewRoom nr = (State.NewRoom) lastState;
            if (nr.isRemoveAll()) {
                bdcHallUI.removeAllRooms();
            } else {
                bdcHallUI.addNewRoom(nr);
            }

        } else if (lastState instanceof State.GenericState) {
            State.GenericState state = (State.GenericState) lastState;
            String type = state.getType();
            System.out.println("BoulderDashClientImpl - update(): State = GenericState(" + type + ")");
            switch (type) {
                case "RoomsUpdate": {
                    bdcHallUI.updateAllRooms();
                    break;
                }
            }
        } else if (lastState instanceof State.Disconnect) {
            System.out.println("BoulderDashClientImpl - update(): State = Disconnect ");
            disconnect();
        }
    }

    public void disconnect() throws RemoteException {
        System.exit(0);
    }

    public void triggeredLogin(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
        System.out.println("triggeredLogin");
        try {
            int login = bdsRI.login(BoulderDashClientImpl.this, username, password);
            System.out.println("int login " + login);
            if (login != 0) {
                System.out.println("You are now logged in!");
                this.setLoggedin(true);
                bdcLoginUI.setVisible(false);
                bdcHallUI = new BoulderDashClientHallGUI(this);
            }

        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    public void triggeredRegister(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
        System.out.println("triggeredRegister");
        try {
            int register = bdsRI.register(BoulderDashClientImpl.this, username, password);
            System.out.println("int register " + register);
            if (register != 0) {
                System.out.println("You are now registed, please login!");
            }

        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    public void triggeredLogout(String username, String password) {
        System.out.println("triggeredLogout");
        try {
            this.setLoggedin(false);
            bdsRI.logout(username);
        } catch (RemoteException e) {
        }
    }

    public BoulderDashClientUserGUI getBdcLoginUI() {
        return bdcLoginUI;
    }

    public void setBdcLoginUI(BoulderDashClientUserGUI bdcLoginUI) {
        this.bdcLoginUI = bdcLoginUI;
    }

    public BoulderDashClientHallGUI getBdcHallUI() {
        return bdcHallUI;
    }

    public void setBdcHallUI(BoulderDashClientHallGUI bdcHallUI) {
        this.bdcHallUI = bdcHallUI;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void updateGroundView(String[][] levelSprites) throws RemoteException {
        if (this.gameController.getGameView().getGameFieldView() != null) {
            this.gameController.getGameView().getGameFieldView().sendRefresh(levelSprites);
        }
    }

    @Override
    public void stopAudio() throws RemoteException {
        System.out.println("BoulderDashClientImpl - stopAudio():");
        if (gameController.getAudioLoadHelper() != null) {
            gameController.getAudioLoadHelper().stopMusic();
        } else {
            System.out.println("BoulderDashClientImpl - stopAudio(): is null");
        }
    }

    @Override
    public void playAudio(boolean song, String name) throws RemoteException {
        System.out.println("BoulderDashClientImpl - playAudio(" + song + "," + name + "):");
        if (gameController.getAudioLoadHelper() != null) {
            if (song) {
                gameController.getAudioLoadHelper().playSound(name);
            } else {
                System.out.println("startMusic(name);");
                gameController.getAudioLoadHelper().startMusic(name);
            }
        } else {
            System.out.println("BoulderDashClientImpl - playAudio(): is null");
        }
    }

    @Override
    public void updateInformationPanel() throws RemoteException {
        if (this.gameController != null) {
            this.gameController.getGameView().getInformationPanel().updateInformation();
        } else {
            System.out.println("BoulderDashClientImpl - updateInformationPanel(): is null");
        }
    }

    @Override
    public void sendWinner(boolean winner, String name) throws RemoteException {
        if (this.gameController != null) {
            this.gameController.setGameEnded(true);
            if(winner) {
                this.gameController.displayWin(name);
            } else {
                this.gameController.displayLose(name);
            }
            
        } else {
            System.out.println("BoulderDashClientImpl - updateInformationPanel(): is null");
        }
    }
    
    

}
