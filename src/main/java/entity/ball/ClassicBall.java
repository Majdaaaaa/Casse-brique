package entity.ball;

import javafx.scene.input.KeyCode;
import physics.entity.Ball;
import physics.geometry.Coordinates;
import utils.GameConstants;
import physics.config.PhysicSetting;
import physics.geometry.Vector;

import utils.Key;
import gui.GameRoot;

public class ClassicBall extends Ball {

    public Key key = new Key();

    public ClassicBall() {
        super(GameConstants.DEFAULT_BALL_START_COORDINATES, GameConstants.DEFAULT_BALL_START_DIRECTION,
        GameConstants.DEFAULT_BALL_SPEED, GameConstants.DEFAULT_BALL_RADIUS);
        super.setPhysicSetting(new PhysicSetting());
    }

    public ClassicBall(int d) {
        super(d);
        super.setPhysicSetting(new PhysicSetting(d,0, new Vector(new Coordinates(0, 0)), 1));
    }

    @Override

    /**
     * Cette méthode gère le mouvement de la balle dans le jeu.
     *
     * @return un booléen indiquant si la balle est perdue ou non.
     *         Retourne `true` si la balle est toujours en jeu, `false` sinon.
     */
    public boolean movement(){
        boolean lost = true;
        double w = GameConstants.DEFAULT_GAME_ROOT_WIDTH;
        double h = GameConstants.DEFAULT_WINDOW_HEIGHT;
        double newX = this.getC().getX() + this.getDirection().getX() * this.getSpeed() ;
        double newY = this.getC().getY() + this.getDirection().getY() * this.getSpeed() ;
        if (CollisionR) {
            if (GameRoot.BougePColision || CollisionR_Side) {
                this.getDirection().setY(-this.getDirection().getY());
                this.getDirection().add(super.getPhysicSetting().getFrictionRacket());
                newY = this.getC().getY() + this.getDirection().getY();
                CollisionR = false;
                CollisionR_Side = false;
                super.getPhysicSetting().UpdateFrictionRacket();
            }
            else {
                for (KeyCode key : GameRoot.direction) {
                    switch (key) {
                        case RIGHT:
                        case D:
                            this.getDirection().setY(-this.getDirection().getY());
                            super.getPhysicSetting().getFrictionRacket().setX(super.getPhysicSetting().getFrictionRacket().getX() + 1);
                            this.getDirection().add(super.getPhysicSetting().getFrictionRacket());
                            newX = this.getC().getX() + this.getDirection().getX() * this.getSpeed();
                            newY = this.getC().getY() + this.getDirection().getY() * this.getSpeed();
                            CollisionR = false;
                            break;
                        case LEFT:
                        case Q:
                            this.getDirection().setY(-this.getDirection().getY());
                            super.getPhysicSetting().getFrictionRacket().setX(super.getPhysicSetting().getFrictionRacket().getX()-1);
                            this.getDirection().add(super.getPhysicSetting().getFrictionRacket());
                            newX = this.getC().getX() + this.getDirection().getX() * this.getSpeed();
                            newY = this.getC().getY() + this.getDirection().getY() * this.getSpeed();
                            CollisionR = false;
                            break;
                        default:
                            break;
                    }
                }
            }
        }   
        if (newX < 0 || newX > w - this.getRadius()) {
            this.getDirection().setX(-this.getDirection().getX());
            this.getDirection().add(super.getPhysicSetting().getFrictionRacket());
            super.getPhysicSetting().UpdateFrictionRacket();
            newX = this.getC().getX() + this.getDirection().getX()*this.getSpeed();
            this.getDirection().setX(this.getDirection().getX()*super.getPhysicSetting().getRetention());
        }
        if (newY < 0) {
            this.getDirection().add(super.getPhysicSetting().getFrictionRacket());
            this.getDirection().setY(-this.getDirection().getY());
            super.getPhysicSetting().UpdateFrictionRacket();
            newY = this.getC().getY() + this.getDirection().getY()* this.getSpeed();
            this.getDirection().setY(this.getDirection().getY()*super.getPhysicSetting().getRetention());
        } 
        if (newY > h - this.getRadius()) {
            lost = false;
        }
        this.setC(new Coordinates(newX, newY));
        this.getDirection().add(super.getPhysicSetting().getWind());
        super.getPhysicSetting().checkGravity(getC(), getDirection());
        return lost;
    }

}
