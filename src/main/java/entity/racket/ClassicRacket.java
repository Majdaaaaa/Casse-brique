package entity.racket;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.scene.input.KeyCode;
import physics.entity.Ball;
import physics.entity.Racket;
import physics.geometry.PhysicTools;
import utils.GameConstants;

/**
 * raquette qui peut aller a droite et a gauche
 * 
 * @author Belhassen rayan
 */

public class ClassicRacket extends Racket {
    private boolean BallFrontRacket = false;

    // creation de la raquette
    public ClassicRacket() {
        super(200, 20, "rectangle", 8, false, true);
    }

    // Mouvement a l'appui des touches
    public void handleKeyPress(Set<KeyCode> keysPressed,List<Ball> balls) {
        for (KeyCode key : keysPressed) {
            if (key == GameConstants.LEFT) {
                if (this.mX() > -largeur / 2)
                    this.deplaceX(-speed);
                    this.getDirection().setX(-1);
                    for (Ball ball : balls) {
                        if(PhysicTools.checkCollision(ball,this.getSegments().get(3))){
                            ball.getDirection().setX(ball.getDirection().getX()+this.getDirection().getX());
                            ball.getC().setX(ball.getC().getX()-speed);
                        }
                    }
            }
            if (key == GameConstants.RIGHT) {
                if (this.mX() < super.getWidth() - longueur - 70)
                    this.deplaceX(speed);
                    this.getDirection().setX(1);
                    for (Ball ball : balls) {
                        if(PhysicTools.checkCollision(ball,this.getSegments().get(1))){
                            ball.getDirection().setX(ball.getDirection().getX()+this.getDirection().getX());
                            ball.getC().setX(ball.getC().getX()+speed);
                        }
                    }
            }
            if (key == GameConstants.SPACE) {
                setJump(jump);

            }
        }
    }

    // Mouvement au relachement des touches
    public void handleKeyRelease(KeyCode event) {
        /*
         * switch (event) {
         * }
         */
    }

    public boolean getBallFrontRacket() {
        return BallFrontRacket;
    }

    public void setBallFrontRacket(boolean ballFrontRacket) {
        BallFrontRacket = ballFrontRacket;
    }

}
