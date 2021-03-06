package edu.ufp.sd.boulderdash.server.game;

import edu.ufp.sd.boulderdash.client.BoulderDashClientRI;
import edu.ufp.sd.boulderdash.server.BoulderDashServerImpl;
import edu.ufp.sd.boulderdash.server.State;
import edu.ufp.sd.boulderdash.server.game.helpers.LevelLoadHelperServer;
import edu.ufp.sd.boulderdash.server.game.helpers.controllers.BoulderAndDiamondControllerServer;
import edu.ufp.sd.boulderdash.server.game.helpers.controllers.RockfordUpdateControllerServer;
import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * LevelModel
 *
 * Levels are loaded from XML file. The view knows the model, the controller is
 * going to modify the model in function of the game panel. The model notifies
 * the view when there are changes on it.
 *
 * @author Colin Leverger <me@colinleverger.fr>
 * @since 2015-06-19
 */
public class LevelModelRoom implements Runnable {

    private BoulderDashServerImpl server;
    
    private ThreadPool threadPool;
    private ArrayList<BoulderDashClientRI> clients = new ArrayList<>(2);
    private ArrayList<RockfordModel> rockfords = new ArrayList<>(2);

    private LevelLoadHelperServer levelLoadHelperServer;
    private DisplayableElementModel[][] groundGrid;
    private GameInformationModel gameInformationModel;
    private String roomName;
    private int roomID;
    private String levelName;
    private int sizeWidth = 0;
    private int sizeHeight = 0;
    private boolean gameRunning;
    private boolean gameStarted;
    private boolean gamePaused;
    private boolean gameHasEnded;

    private RockfordUpdateControllerServer updatePosRockford;

    /**
     * Sprite animation thread
     */
    private Thread spriteAnimator;

    /**
     * Animation speed
     */
    private final int DELAY = 15;

    /**
     * Class constructor
     *
     * @param levelName Level name
     */
    public LevelModelRoom(BoulderDashServerImpl server, String levelName) {
        System.out.println("LevelModelServer() - constructor()");
        this.server = server;
        this.levelName = levelName;
        this.gameRunning = true;
        this.gameStarted = false;
        this.gameHasEnded = false;
        this.gamePaused = false;

        this.levelLoadHelperServer = new LevelLoadHelperServer(this.levelName);

        this.groundGrid = this.levelLoadHelperServer.getGroundGrid();
        this.sizeWidth = this.levelLoadHelperServer.getWidthSizeValue();
        this.sizeHeight = this.levelLoadHelperServer.getHeightSizeValue();

        this.gameInformationModel = new GameInformationModel(this, this.levelLoadHelperServer.getDiamondsToCatch());

        this.createLimits();

        this.initRockford();
        this.initThreadAnimator();
        clients.add(null);
        clients.add(null);

        this.updatePosRockford = new RockfordUpdateControllerServer(this);
        new BoulderAndDiamondControllerServer(this);
        this.threadPool = new ThreadPool(2);
    }

    /**
     * Initializes the animator thread
     */
    private void initThreadAnimator() {
        this.spriteAnimator = new Thread(this);
        this.spriteAnimator.start();
    }

    /**
     * Initializes the Rockfords position attributes
     */
    private void initRockford() {
        this.rockfords = this.levelLoadHelperServer.getRockfordIntances();
    }

    /**
     * Creates the limits Puts steel walls all around the game panel
     */
    private void createLimits() {
        int maxWidth = this.sizeWidth - 1;
        int maxHeight = this.sizeHeight - 1;

        for (int x = 0; x < this.sizeWidth; x++) {
            this.groundGrid[x][0] = new SteelWallModel();
            this.groundGrid[x][maxHeight] = new SteelWallModel();
        }
        for (int y = 0; y < this.sizeHeight; y++) {
            this.groundGrid[0][y] = new SteelWallModel();
            this.groundGrid[maxWidth][y] = new SteelWallModel();
        }
    }

    public void resetLevelModel() {
        this.groundGrid = this.levelLoadHelperServer.getGroundGrid();
        this.setGameRunning(true);
        this.gameInformationModel.resetInformations();
    }

    /**
     * Updates the horizontal & vertical positions of a Rockford in the model
     *
     * @param posX Horizontal position of Rockford
     * @param posY Vertical position of Rockford
     */
    public void updateRockfordPosition(int index, int posX, int posY) {
        this.rockfords.get(index).setPositionX(posX);
        this.rockfords.get(index).setPositionY(posY);
    }

    /**
     * Checks whether position is out-of-bounds or not
     *
     * @param posX Horizontal position
     * @param posY Vertical position
     */
    private boolean isOutOfBounds(int posX, int posY) {
        if (posX > 0 && posY > 0 && posX < this.getLevelLoadHelper().getHeightSizeValue() && posY < this.getLevelLoadHelper().getWidthSizeValue()) {
            return false;
        }

        return true;
    }

    /**
     * Plays collision sound
     */
    private void playCollisionSound(int index, int posX, int posY) {
        String collisionSound = null;

        if (this.getRockford(index).isCollisionDone() == false) {
            // Out of bounds?
            if (this.isOutOfBounds(posX, posY) == true) {
                collisionSound = "touch";
            } else {
                DisplayableElementModel nextElement = this.groundGrid[posX][posY];
                collisionSound = nextElement.getCollideSound();
            }

            this.getRockford(index).setCollisionDone(true);
        }

        if (collisionSound != null) {
            try {
                // Play sound to player
                this.clients.get(index).playAudio(true, collisionSound);
                //this.audioLoadHelper.playSound(collisionSound);
            } catch (RemoteException ex) {
                Logger.getLogger(LevelModelRoom.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Gets the horizontal position of Rockford from the model
     *
     * @return Horizontal position of Rockford
     */
    public int getRockfordPositionX(int index) {
        return this.rockfords.get(index).getPositionX();
    }

    /**
     * Sets the new Rockford position
     *
     * @param posX Next horizontal position on the grid
     * @param posY Next vertical position on the grid
     */
    public void setPositionOfRockford(int index, int posX, int posY) {
        int oldX = this.getRockfordPositionX(index);
        int oldY = this.getRockfordPositionY(index);

        if (this.groundGrid[posX][posY].getSpriteName().compareTo("diamond") == 0) {
            /*try {
                System.out.println(clients.get(index).getClientUsername() + " found a diamond!");
            } catch (RemoteException ex) {
                Logger.getLogger(LevelModelRoom.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            this.gameInformationModel.incrementScore(index);
            this.gameInformationModel.decrementRemainingsDiamonds();

            if (this.gameInformationModel.getRemainingsDiamonds() == 0) {
                System.out.println("All diamonds found!");
                try {
                    BoulderDashClientRI client = this.clients.get(index);
                    client.playAudio(true, "win");
                    client.sendWinner(true, client.getClientUsername());
                    
                    BoulderDashClientRI second = this.clients.get(index == 0 ? 1 : 0);
                    second.playAudio(true, "loose");
                    second.sendWinner(false, client.getClientUsername());
                    
                } catch (RemoteException ex) {
                    Logger.getLogger(LevelModelRoom.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                this.setGameRunning(false);

            }
        }

        this.playCollisionSound(index, posX, posY);

        // Check that we are not out of bound...
        if (this.isOutOfBounds(posX, posY) == false) {
            // Create a new empty model in the old pos of Rockford
            System.out.println("setPositionOfRockford(" + index + "," + posX + "," + posY + ")");
            this.groundGrid[oldX][oldY] = new EmptyModel();

            // Save the x / y pos of Rockford in the levelModel only
            this.updateRockfordPosition(index, posX, posY);

            this.groundGrid[posX][posY] = this.getRockford(index);
        }

        this.notifyClients();
    }

    /**
     * Gets the vertical position of Rockford from the model
     *
     * @param rockford instances index
     *
     * @return Vertical position of Rockford
     */
    public int getRockfordPositionY(int index) {
        return this.rockfords.get(index).getPositionY();
    }

    /**
     * Gets the Rockford object instance
     *
     * @return Rockford object
     */
    public RockfordModel getRockford(int index) {
        return this.rockfords.get(index);
    }

    public ArrayList<RockfordModel> getRockfords() {
        return rockfords;
    }

    public void setRockfords(ArrayList<RockfordModel> rockfords) {
        this.rockfords = rockfords;
    }

    /**
     * Gets the displayable element at given positions
     *
     * @param x Block horizontal position
     * @param y Block vertical position
     * @return Displayable element at given positions
     */
    public DisplayableElementModel getDisplayableElement(int x, int y) {
        return this.groundGrid[x][y];
    }

    /**
     * Returns number of diamonds
     *
     * @return Number of diamonds
     */
    public int countDiamonds() {
        int numberOfDiamonds = 0;

        // Iterate and catch it!
        for (int x = 0; x < this.getSizeWidth(); x++) {
            for (int y = 0; y < this.getSizeHeight(); y++) {
                if (this.groundGrid[x][y] != null && this.groundGrid[x][y].getSpriteName().compareTo("diamond") == 0) {
                    numberOfDiamonds += 1;
                }
            }
        }

        return numberOfDiamonds;
    }

    /**
     * Gets the level horizontal size
     *
     * @return Horizontal size
     */
    public int getSizeWidth() {
        return this.sizeWidth;
    }

    /**
     * Sets the level horizontal size
     *
     * @param sizeWidth Horizontal size
     */
    public void setSizeWidth(int sizeWidth) {
        this.sizeWidth = sizeWidth;
    }

    /**
     * Gets the level vertical size
     *
     * @return Vertical size
     */
    public int getSizeHeight() {
        return this.sizeHeight;
    }

    /**
     * Sets the level vertical size
     *
     * @return sizeHeight Vertical size
     */
    public void setSizeHeight(int sizeHeight) {
        this.sizeHeight = sizeHeight;
    }

    /**
     * Gets the ground level model
     *
     * @return Ground level model
     */
    public DisplayableElementModel[][] getGroundLevelModel() {
        return groundGrid;
    }

    /**
     * Notify the clients and update the sprites array
     */
    private void notifyClients() {
        if (this.clients.get(0) != null || this.clients.get(1) != null) {
            for (BoulderDashClientRI client : clients) {
                if (client != null) {
                    threadPool.execute(new UpdateSprites(client, this.getLevelSprites()));
                }
            }
        }
    }

    /**
     * Update the current sprite Notifies the observers
     *
     * @param x Sprite block horizontal position
     * @param y Sprite block vertical position
     */
    public void updateSprites(int x, int y) {
        if (groundGrid[x][y] == null) {
            groundGrid[x][y] = new DirtModel();
        }

        groundGrid[x][y].update(System.currentTimeMillis());
        //this.notifyClients();
    }

    /**
     * Update all the sprites So that they can be animated
     */
    @Override
    public void run() {
        System.out.println("LevelModelServer() - ThreadID: " + Thread.currentThread().getId());
        while (gameRunning) {
//            if (!gamePaused) {
//                for (int x = 0; x < this.getSizeWidth(); x++) {
//                    for (int y = 0; y < this.getSizeHeight(); y++) {
//                        this.updateSprites(x, y);
//                    }
//                }
//            }

            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }
        }
    }

    /**
     * Increments the user score
     */
    public void incrementScore(int index) {
        this.gameInformationModel.incrementScore(index);
    }

    /**
     * Gets the associated level load helper
     *
     * @return Level load helper
     */
    public LevelLoadHelperServer getLevelLoadHelper() {
        return this.levelLoadHelperServer;
    }

    /**
     * sets the game to a defined state
     *
     * @param gameRunning Whether game is running or not
     */
    public void setGameRunning(boolean gameRunning) {
        if(!gameRunning) {
            // Game has finished
            this.server.getRooms().remove(this.roomID);
            try {
                this.server.setState(new State().new GenericState("RoomsUpdate"));
            } catch (RemoteException ex) {
                Logger.getLogger(LevelModelRoom.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.gameRunning = gameRunning;
    }

    /**
     * tells if the game is running
     *
     * @return whether the game is running or not
     */
    public boolean isGameRunning() {
        return gameRunning;
    }

    /**
     * When a boulder is falling on Rockford there is an explosion around him
     *
     * @param x Object horizontal position
     * @param y Object vertical position
     */
    public void exploseGround(int index, int x, int y) {
        System.out.println("exploseGround");
        this.groundGrid[x][y] = new EmptyModel();
        this.groundGrid[x + 1][y] = new EmptyModel();
        this.groundGrid[x - 1][y] = new EmptyModel();
        this.groundGrid[x][y + 1] = new EmptyModel();
        this.groundGrid[x + 1][y + 1] = new EmptyModel();
        this.groundGrid[x - 1][y + 1] = new EmptyModel();
        this.groundGrid[x][y - 1] = new EmptyModel();
        this.groundGrid[x + 1][y - 1] = new EmptyModel();
        this.groundGrid[x - 1][y - 1] = new EmptyModel();
        this.getRockford(index).setHasExplosed(true);

        try {
            BoulderDashClientRI client = this.clients.get(index);
            client.playAudio(true, "die");
            client.sendWinner(false, client.getClientUsername());

            BoulderDashClientRI second = this.clients.get(index == 0 ? 1 : 0);
            second.playAudio(true, "win");
            second.sendWinner(true, client.getClientUsername());
            
        } catch (RemoteException ex) {
            Logger.getLogger(LevelModelRoom.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Again a sleep to notify the observers properly
        /*try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(LevelModelRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.notifyClients(); */
    }

    /**
     * Makes the DisplayableElement[x][y] fall one box down
     *
     * @param x Object horizontal position
     * @param y Object vertical position
     */
    public void makeThisDisplayableElementFall(int x, int y) {
        this.groundGrid[x][y].setFalling(true);
        this.groundGrid[x][y + 1] = this.groundGrid[x][y];
        this.groundGrid[x][y] = new EmptyModel();
        this.notifyClients();
    }

    /**
     * Makes the BoulderModel[x][y] slide left
     *
     * @param x Object horizontal position
     * @param y Object vertical position
     */
    public void makeThisBoulderSlideLeft(int x, int y) {
        this.groundGrid[x][y].setFalling(true);
        this.groundGrid[x - 1][y + 1] = this.groundGrid[x][y];
        this.groundGrid[x][y] = new EmptyModel();
    }

    /**
     * Makes the BoulderModel[x][y] slide right
     *
     * @param x Object horizontal position
     * @param y Object vertical position
     */
    public void makeThisBoulderSlideRight(int x, int y) {
        this.groundGrid[x][y].setFalling(true);
        this.groundGrid[x + 1][y + 1] = this.groundGrid[x][y];
        this.groundGrid[x][y] = new EmptyModel();
    }

    /**
     * Makes the BoulderModel[x][y] transform into a diamond
     *
     * @param x Object horizontal position
     * @param y Object vertical position
     */
    public void transformThisBoulderIntoADiamond(int x, int y) {
        this.groundGrid[x][y + 2] = new DiamondModel();
        this.groundGrid[x][y] = new EmptyModel();
    }

    /**
     * Makes the BoulderModel[x][y] moving to right
     *
     * @param x Object horizontal position
     * @param y Object vertical position
     */
    public void moveThisBoulderToRight(int x, int y) {
        this.groundGrid[x + 1][y] = this.groundGrid[x][y];
        this.groundGrid[x][y] = new EmptyModel();
    }

    /**
     * Makes the BoulderModel[x][y] moving to left
     *
     * @param x Object horizontal position
     * @param y Object vertical position
     */
    public void moveThisBoulderToLeft(int x, int y) {
        this.groundGrid[x - 1][y] = this.groundGrid[x][y];
        this.groundGrid[x][y] = new EmptyModel();
    }

    /**
     * Deletes the BoulderModel[x][y]
     *
     * @param x Object horizontal position
     * @param y Object vertical position
     */
    public void deleteThisBoulder(int x, int y) {
        this.groundGrid[x][y] = new EmptyModel();
    }

    /**
     * Explose the brick wall
     *
     * @param x
     * @param y
     */
    public void exploseThisBrickWall(int x, int y) {
        this.groundGrid[x][y] = new EmptyModel();
        this.groundGrid[x][y + 1] = new EmptyModel();
    }

    /**
     * Expand the ExpandingWallModel to left
     *
     * @param x
     * @param y
     */
    public void expandThisWallToLeft(int x, int y) {
        this.groundGrid[x - 1][y] = new ExpandingWallModel();
    }

    /**
     * Expand the ExpandingWallModel to right
     *
     * @param x
     * @param y
     */
    public void expandThisWallToRight(int x, int y) {
        this.groundGrid[x + 1][y] = new ExpandingWallModel();
    }

    /**
     * Gets gameInformationModel
     *
     * @return gameInfos like score, remainings Diamonds etc
     */
    public GameInformationModel getGameInformationModel() {
        return this.gameInformationModel;
    }

    /**
     * Set the gamePaused variable
     *
     * @param gamePaused
     */
    public void setGamePaused(boolean gamePaused) {
        this.gamePaused = gamePaused;
    }

    /**
     * Get the gamePaused variable
     *
     * @return gamePaused
     */
    public boolean getGamePaused() {
        return this.gamePaused;
    }

    public boolean isGameHasEnded() {
        return gameHasEnded;
    }

    public void setGameHasEnded(boolean gameHasEnded) {
        this.gameHasEnded = gameHasEnded;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ArrayList<BoulderDashClientRI> getClients() {
        return clients;
    }

    public void setClients(ArrayList<BoulderDashClientRI> clients) {
        this.clients = clients;
    }

    public int getConnectedClients() {
        int sum = 0;
        for (BoulderDashClientRI client : this.clients) {
            if (client != null) {
                sum++;
            }
        }
        System.out.println("Connected clients in Room # " + this.roomID + " - " + sum);

        return sum;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void moveRockford(BoulderDashClientRI client, String movement) {
        try {
            System.out.println(client.getClientUsername() + " moved up in room " + roomID);
        } catch (RemoteException ex) {
            Logger.getLogger(LevelModelRoom.class.getName()).log(Level.SEVERE, null, ex);
        }

        int index = 0;
        if (client.equals(this.clients.get(0))) {
            index = 0;
        } else {
            index = 1;
        }

        switch (movement) {
            case "UP": {
                DisplayableElementModel upElement = this.getGroundLevelModel()[this.getRockford(index).getPositionX()][this.getRockford(index).getPositionY() - 1];

                if (upElement.getPriority() < this.getRockford(index).getPriority()) {
                    this.updatePosRockford.moveRockford(index, this.getRockford(index).getPositionX(), this.getRockford(index).getPositionY() - 1);
                    this.getRockford(index).startRunningUp();
                }

                break;
            }
            case "DOWN": {
                DisplayableElementModel downElement = this.getGroundLevelModel()[this.getRockford(index).getPositionX()][this.getRockford(index).getPositionY() + 1];

                if (downElement.getPriority() < this.getRockford(index).getPriority()) {
                    this.updatePosRockford.moveRockford(index, this.getRockford(index).getPositionX(), this.getRockford(index).getPositionY() + 1);
                    this.getRockford(index).startRunningDown();
                }
                break;
            }
            case "RIGHT": {
                DisplayableElementModel rightElement = this.getGroundLevelModel()[this.getRockford(index).getPositionX() + 1][this.getRockford(index).getPositionY()];

                if (rightElement.getPriority() < this.getRockford(index).getPriority()) {
                    this.updatePosRockford.moveRockford(index, this.getRockford(index).getPositionX() + 1, this.getRockford(index).getPositionY());
                    this.getRockford(index).startRunningRight();
                }
                break;
            }
            case "LEFT": {

                DisplayableElementModel leftElement = this.getGroundLevelModel()[this.getRockford(index).getPositionX() - 1][this.getRockford(index).getPositionY()];

                if (leftElement.getPriority() < this.getRockford(index).getPriority()) {
                    this.updatePosRockford.moveRockford(index, this.getRockford(index).getPositionX() - 1, this.getRockford(index).getPositionY());
                    this.getRockford(index).startRunningLeft();
                }
                break;
            }
            case "STAYING": {
                this.getRockford(index).isStaying();
                break;
            }
        }

    }

    public String[][] getLevelSprites() {
        String[][] levelSprites = new String[this.getSizeWidth()][this.getSizeHeight()];
        for (int i = 0; i < this.getSizeWidth(); i++) {
            for (int j = 0; j < this.getSizeHeight(); j++) {
                levelSprites[i][j] = this.getDisplayableElement(i, j).getSpriteName();
            }
        }
        return levelSprites;
    }

    public void addClient(BoulderDashClientRI client) {
        try {
            
            System.out.println(client.getClientUsername() + " joined room " + this.roomID);

            if (this.clients.get(0) == null) {
                clients.set(0, client);
            } else {
                clients.set(1, client);
            }
            
            // All clients connected!
            if (this.clients.get(0) != null && this.clients.get(1) != null) {
                System.out.println("Room " + this.roomID + " is now full.");
                StartGameThread gameStartThread = new StartGameThread(this);
                gameStartThread.start();
            }
            
            updateRoomName();

        } catch (RemoteException ex) {
            Logger.getLogger(LevelModelRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeClient(BoulderDashClientRI client) {
        int index = clients.indexOf(client);
        clients.set(index, null);
        this.gameStarted = false;
        try {
            System.out.println(client.getClientUsername() + " left server " + this.roomID);
        } catch (RemoteException ex) {
            Logger.getLogger(LevelModelRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        updateRoomName();
    }
    
    private void updateRoomName() {
        this.setRoomName(this.roomID + "# BoulderDash Room"
                    + " - Level: " + this.levelName
                    + " - Players: " + this.getConnectedClients() + "/2");
        
        try {
            this.server.setState(new State().new GenericState("RoomsUpdate"));
        } catch (RemoteException ex) {
            Logger.getLogger(LevelModelRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class UpdateSprites implements Runnable {

    private BoulderDashClientRI client;
    private String[][] sprites;

    public UpdateSprites(BoulderDashClientRI client, String[][] sprites) {
        this.client = client;
        this.sprites = sprites;
    }

    @Override
    public void run() {
        System.out.println("Sending Sprites using ThreadID " + Thread.currentThread().getId());
        try {
            client.updateGroundView(sprites);
        } catch (RemoteException ex) {
            Logger.getLogger(LevelModelRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Sent sprites");
    }
}

class StartGameThread extends Thread {

    private LevelModelRoom room;
    private int times = 5;

    public StartGameThread(LevelModelRoom room) {
        this.room = room;
    }

    @Override
    public void run() {
        try {
            while (times != 0) {
                System.out.println("StartGameThread() executed in room " + room.getRoomID());
                
                switch(times) {
                    case 3: {
                        for (BoulderDashClientRI client : room.getClients()) {
                            client.updateInformationPanel();
                            client.playAudio(true, "new");                            
                        }
                        break;
                    }
                    case 2: {
                        for (BoulderDashClientRI client : room.getClients()) {
                            client.updateGroundView(room.getLevelSprites());
                        }
                        break;
                    }
                    case 1: {
                        room.setGameStarted(true);
                        break;
                    }
                }
                
                sleep(1000);
                times--;
            }
        } catch (InterruptedException | RemoteException ex) {
            Logger.getLogger(StartGameThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
