package fr.enssat.BoulderDash.controllers;

import edu.ufp.sd.boulderdash.client.BoulderDashClientImpl;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * GameKeyController
 *
 * Manages the key events controller.
 *
 * @author Colin Leverger <me@colinleverger.fr>
 * @since 2015-06-19
 */
public class GameKeyController implements KeyListener {

    private BoulderDashClientImpl bdc;
    private GameController gameController;
    private int roomID;

    /**
     * Class constructor
     *
     * @param levelModel Level model
     */
    public GameKeyController(GameController gameController, BoulderDashClientImpl bdc, int roomID) {
        this.gameController = gameController;
        this.bdc = bdc;
        if (this.bdc == null) {
            System.out.println("FOUND FUCKING NULL!");
        }
        this.roomID = roomID;
    }

    /**
     * Handles the 'key pressed' event
     *
     * @param e Key event
     */
    public void keyPressed(KeyEvent e) {
        if(gameController.isGameEnded()) {
            return;
        }
        
        int keyCode = e.getKeyCode();

        //System.out.println("[DEBUG]: Pressed keyCode " + keyCode);
        switch (keyCode) {
            // Direction Rockford 1: UP
            case KeyEvent.VK_UP: {
                try {
                    this.bdc.getBdsRI().sendKeys(bdc, roomID, "UP");
                } catch (RemoteException ex) {
                    Logger.getLogger(GameKeyController.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
            }

            // Direction Rockford 1: DOWN
            case KeyEvent.VK_DOWN: {
                try {
                    this.bdc.getBdsRI().sendKeys(bdc, roomID, "DOWN");
                } catch (RemoteException ex) {
                    Logger.getLogger(GameKeyController.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
            }

            // Direction Rockford 1: LEFT
            case KeyEvent.VK_LEFT: {
                try {
                    this.bdc.getBdsRI().sendKeys(bdc, roomID, "LEFT");
                } catch (RemoteException ex) {
                    Logger.getLogger(GameKeyController.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

            }

            // Direction Rockford 1: RIGHT
            case KeyEvent.VK_RIGHT: {
                try {
                    this.bdc.getBdsRI().sendKeys(bdc, roomID, "RIGHT");
                } catch (RemoteException ex) {
                    Logger.getLogger(GameKeyController.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
            }

        }
    }

    /**
     * Handles the 'key released' event
     *
     * @param e Key event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_LEFT: {
                try {
                    this.bdc.getBdsRI().sendKeys(bdc, roomID, "STAYING");
                } catch (RemoteException ex) {
                    Logger.getLogger(GameKeyController.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }

    }

    /**
     * Handles the 'key typed' event
     *
     * @param e Key event
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // Do nothing.
    }
}
