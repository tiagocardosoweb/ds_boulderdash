package fr.enssat.BoulderDash.views;

import fr.enssat.BoulderDash.models.LevelModel;

import javax.swing.*;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * FieldView
 *
 * FieldView, created by controller; we notice that we don't need to make
 * levelModel observable; Because of the sprites we have to refresh the game
 * windows very often so don't need of observers/observable mechanism
 *
 * @author Colin Leverger <me@colinleverger.fr>
 * @since 2015-06-19
 *
 * This view is basically drawing into the Frame the levelModel.
 *
 */
public abstract class GroundView extends JPanel implements Observer {

    protected LevelModel levelModel;

    /**
     * Class constructor
     *
     * @param levelModel Level model
     */
    public GroundView(LevelModel levelModel) {
        this.levelModel = levelModel;
        this.levelModel.addObserver(this);
    }

    /**
     * Draws the map
     *
     * @param width Map width
     * @param height Map height
     * @param g Map graphical object
     */
    public void drawTerrain(int width, int height, Graphics g) {
        //System.out.println("Working");
        // Draw items
        if (this.levelModel.getMode().compareTo("game") == 0) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    g.drawImage(this.levelModel.getImage(x, y), (x * 16), (y * 16), this);
                }
            }
            
            if (!this.levelModel.isGameRunning() && !this.levelModel.isGameHasEnded()) {
                System.out.println("teste");
                int diamonds = this.levelModel.getGameInformationModel().getRemainingsDiamonds();
                if(diamonds == 0) {
                    System.out.println("GAME HAS ENDED");
                    int winner = this.levelModel.getGameInformationModel().getRockfordMoreDiamonds();
                    System.out.println("Winner: " + winner);
                    this.displayWin(winner);
                } else {
                    for (int i = 0; i < 2; i++) {
                        if(this.levelModel.getRockford(i).getHasExplosed()) {
                            this.displayLose(i);
                            System.out.println("FOUND EXPLODED STATE! " + i);
                            break;
                        }
                    }
                }
                this.levelModel.setGameHasEnded(true);
            }
            
        } else {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    g.drawImage(this.levelModel.getImage(x, y), (x * 16), (y * 16), this);
                }
            }
            if (this.levelModel.getShowCursor()) {
                g.drawImage(
                        this.levelModel.getCursorImage(),
                        ((this.levelModel.getCursorXPosition() + 1) * 16),
                        ((this.levelModel.getCursorYPosition() + 1) * 16),
                        this
                );
            }
        }
    }

    /**
     * Set the view to inform the user that he won
     */
    private void displayWin(int index) {
       new WinLoseView(index,"win");
    }

    /**
     * Set the view to inform the user that he is not good at this game
     */
    private void displayLose(int index) {
        new WinLoseView(index,"loose");
    }

    /**
     * Paints the map
     *
     * @param g Map graphical object
     */
    public void paint(Graphics g) {
        this.drawTerrain(this.levelModel.getSizeWidth(), this.levelModel.getSizeHeight(), g);
    }

    /**
     * Updates the view
     *
     * @param obs Observable item
     * @param obj Object item
     */
    @Override
    public void update(Observable obs, Object obj) {
        repaint();
    }
}
