package fr.enssat.BoulderDash.views;

import edu.ufp.sd.boulderdash.client.BoulderDashClientImpl;
import fr.enssat.BoulderDash.controllers.GameController;
import fr.enssat.BoulderDash.controllers.GameKeyController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

/**
 * GameFieldView
 *
 * Game field view for the game itself.
 *
 * @author Valerian Saliou <valerian@valeriansaliou.name>
 * @since 2015-06-21
 */
public class GameGroundView extends GroundView {

    private BoulderDashClientImpl bdc;
    private GameController gameController;
    private int serverID;

    /**
     * Class constructor
     *
     * @param gameController Game controller
     * @param levelModel Level model
     */
    public GameGroundView(BoulderDashClientImpl bdc, GameController gameController, int serverID) {
        super(bdc,serverID);

        this.bdc = bdc;

        this.gameController = gameController;
        this.serverID = serverID;

        Runnable r = new GameKeyController(this.bdc, this.gameController.getAudioLoadHelper(), serverID);
        new Thread(r).start();
        this.addKeyListener((KeyListener)r);

        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setFocusable(true);
    }
}
