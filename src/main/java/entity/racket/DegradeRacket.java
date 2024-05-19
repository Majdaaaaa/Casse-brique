package entity.racket;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.scene.input.KeyCode;
import physics.entity.Ball;
import physics.entity.Racket;
import physics.geometry.PhysicTools;
import utils.GameConstants;

/**
 * Raquette en forme de triangle
 *
 * @see RaketGraphics
 */
public class DegradeRacket extends Racket {

    public DegradeRacket() {
        super(200, 20, "triangle", 8, false, true);
    }

    public void handleKeyPress(Set<KeyCode> keysPressed,List<Ball> balls) {
        for (KeyCode key : keysPressed) {
            if (key == GameConstants.LEFT) {
                if (this.mX() > -largeur / 2)
                    this.deplaceX(-speed);
                    this.getDirection().setX(-1);
                    for (Ball ball : balls) {
                        if(PhysicTools.checkCollision(ball,this.getSegments().get(2))){
                            ball.getDirection().setX(ball.getDirection().getX()-speed);
                            ball.getC().setX(ball.getC().getX()-speed);
                        }
                    }
            }
            if (key == GameConstants.RIGHT) {
                if (this.mX() < super.getWidth() - longueur - 70)
                    this.deplaceX(speed);
                    this.getDirection().setX(1);
                    for (Ball ball : balls) {
                        if(PhysicTools.checkCollision(ball,this.getSegments().get(0))){
                            ball.getDirection().setX(ball.getDirection().getX()+speed);
                            ball.getC().setX(ball.getC().getX()+speed);
                        }
                    }
            }
            if (key == GameConstants.SPACE) {
                setJump(jump);

            }
        }
    }

    public void handleKeyRelease(KeyCode event) {
    }
}
