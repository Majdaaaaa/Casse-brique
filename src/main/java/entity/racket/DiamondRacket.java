package entity.racket;

import javafx.scene.input.KeyCode;
import physics.entity.Racket;
import utils.GameConstants;

import java.util.Set;

/**
 * Raquette en forme de le losange
 *
 * @see RaketGraphics
 */
public class DiamondRacket extends Racket {

    public DiamondRacket() {
        super(200, 40, "losange", 8, false, true);
    }

    public void handleKeyPress(Set<KeyCode> keysPressed) {
        for (KeyCode key : keysPressed) {
            if (key == GameConstants.LEFT) {
                if (this.mX() > -largeur / 2)
                    this.mX(this.mX() - speed);
            }
            if (key == GameConstants.RIGHT) {
                if (this.mX() < GameConstants.DEFAULT_GAME_ROOT_WIDTH - longueur - 70)
                    this.mX(this.mX() + speed);
            }
            if (key == GameConstants.SPACE) {
                setlargeurP(true);
                setVitesseP(true);
            }
        }
    }

    public void handleKeyRelease(KeyCode event) {
    }
}