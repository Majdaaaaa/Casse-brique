package entity.ball;

import java.util.Random;

import entity.racket.Racket;
import geometry.Coordinates;
import gui.GameRoot;
import javafx.scene.input.KeyCode;
import utils.GameConstants;

public class HyperBall extends Ball {
    private double speedBoost = 0.1;

    public HyperBall(int d) {
        super(d);
    }

    public HyperBall() {
        super(GameConstants.DEFAULT_BALL_START_COORDINATES, GameConstants.DEFAULT_BALL_START_DIRECTION,
                GameConstants.DEFAULT_BALL_SPEED, GameConstants.DEFAULT_BALL_RADIUS);
    }

    public boolean movement() {
        boolean lost = true;
        Random rand = new Random();
        double h = GameConstants.DEFAULT_WINDOW_HEIGHT;
        double w = GameConstants.DEFAULT_WINDOW_WIDTH;
        if(this.getDirection().getY()>1){
            this.getDirection().setY(1);
        }
        double newX = this.getC().getX() + this.getDirection().getX() * this.getSpeed();
        double newY = this.getC().getY() + this.getDirection().getY() * this.getSpeed();

        // Gestion des collisions avec la raquettes
        if (CollisionR) { 
            if (GameRoot.BougePColision) {
                this.getDirection().setY(-this.getDirection().getY());
                double d=rand.nextDouble()-0.5;
                this.getDirection().setY(-(this.getDirection().getY())+d);
                newX = this.getC().getX() + this.getDirection().getX() * this.getSpeed();
                newY = this.getC().getY() + this.getDirection().getY() * this.getSpeed();
                CollisionR = false;
            }
            if (!GameRoot.BougePColision) {
                for (KeyCode key : GameRoot.direction) {
                    switch (key) {
                        case RIGHT:
                        case D:
                            System.out.println("droite");
                            this.getDirection().setX(1);
                            this.getDirection().setY(-1);
                            newX = this.getC().getX() + this.getDirection().getX() * this.getSpeed();
                            newY = this.getC().getY() + this.getDirection().getY() * this.getSpeed();
                            CollisionR = false;

                            break;
                        case LEFT:
                        case Q:
                            System.out.println("gauche");
                            this.getDirection().setX(-1);
                            this.getDirection().setY(-1);
                            newX = this.getC().getX() + this.getDirection().getX() * this.getSpeed();
                            newY = this.getC().getY() + this.getDirection().getY() * this.getSpeed();
                            CollisionR = false;
                            break;
                    }
                }
            }
        }
        if (newX < 0 || newX > w - this.getRadius()) {
            this.getDirection().setX(-this.getDirection().getX());
            newX = this.getC().getX() + this.getDirection().getX() * this.getSpeed();
            setBoost();
        }
        if (newY < 0 || CollisionR) {
            if (newY < 0 || CollisionR) {
                this.getDirection().setY(-this.getDirection().getY());
                newY = this.getC().getY() + this.getDirection().getY() * this.getSpeed();
                CollisionR = false;
                setBoost();
            }
            if (newY > h - this.getRadius()) {
                lost = false;
            }
        }
        this.setC(new Coordinates(newX, newY));
        return lost;
    }

    public void setBoost() {
        this.setSpeed(this.getSpeed() + this.speedBoost);
        System.out.println(this.getSpeed());
    }

}
