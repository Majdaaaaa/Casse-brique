package config;

import java.util.List;
import java.util.ArrayList;

import entity.Boost;
import physics.entity.Ball;
import physics.entity.Racket;
import physics.geometry.Coordinates;
import entity.ball.MagnetBall;
import java.util.Timer;
import java.util.TimerTask;

public class Game {

    private Ball ball;
    private Racket racket;
    private Map map;
    private boolean lost;
    private boolean win;
    public static int score = 0;
    private int life = 3;
    private boolean collide;
    private GameRules rules;
    private Timer inGameTimer;
    private int timeElapsed = 0; // en secondes
    private List<Boost> boosts = new ArrayList<>();

    public Game(Ball ball, Racket racket, GameRules rules) {
        this.ball = ball;
        this.racket = racket;
        this.rules = rules;
        this.map = new Map(rules);
        rules.initRules(this);
    }

    public Game(Ball ball, Racket racket, int mapWidth, int mapHeight, int life, GameRules rules) {
        this.ball = ball;
        this.racket = racket;
        // this.map = new Map(rules, mapWidth, mapHeight);
        this.life = life;
        this.rules = rules;
        rules.initRules(this);
    }

    public void start() {
        if (inGameTimer == null) {
            inGameTimer = new Timer();
            inGameTimer.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() { // chaque seconde
                    timeElapsed++;
                    rules.updateRemainingTime();
                }
            }, 0, 1000);
        }
    }

    public void stop() {
        inGameTimer = null;
    }

    public void update(long deltaT) {

        start();
        //Vérifie si la balle touche une brique
        map.handleCollisionBricks(ball, rules); //gérer la collision des briques
        if (map.updateBricksStatus()) {  
            //si la briques est cassée, chance d'avoir un boost
            Boost boost = Boost.createBoost(ball.getC());
            if (boost != null) {
                boosts.add(boost);
            }
        }
        // Si la balle touche la raquette
        if (racket.CollisionRacket2(ball)) {
            ball.setCollisionR(true);
            rules.updateRemainingBounces();
            rules.updateBricksTransparency(map);
            rules.updateBricksUnbreakability(map);
            rules.shuffleBricks(map.getBricks());
        }
        // Gere les conditions de perte
        if (!ball.movement()) {
            life--;
            ball.reset();
        }
        if (life == 0 || !rules.check()) {
            lost = true;
            inGameTimer.cancel();
        }
        if (verifyWin()) {
            win = true;
        }

        if (ball instanceof MagnetBall) {
            // donne les coordonnées de la raquette a la MagnetBall
            setRa();
            // actualise l'etat de la raquette
            if (BallFrontRacket()) {
                ((MagnetBall) ball).setFront(true);
            } else {
                ((MagnetBall) ball).setFront(false);
            }
        }
    }

    public boolean collisionRacket(Coordinates c) {
        return c.getX() >= racket.getC().getX() && c.getX() <= racket.getC().getX() + racket.getLongueur()
                && c.getY() >= racket.getC().getY() && c.getY() <= racket.getC().getY() + racket.getLargeur();
    }

    // Vérifie si la balle est devant la raquette
    public boolean BallFrontRacket() {
        if (racket.getC().getX() - ball.getC().getX() < 0
                && (racket.getC().getX() + racket.getLargeur()) - ball.getC().getX() > 0) {
            return true;
        }
        return false;
    }

    private boolean verifyWin() {
        return map.countBricks() == 0;
    }

    // Setters/getters
    public Ball getBall() {
        return ball;
    }

    public boolean getCollide() {
        return collide;
    }

    public Racket getRacket() {
        return racket;
    }

    public boolean isLost() {
        return lost;
    }

    public boolean isWin() {
        return win;
    }

    public int getScore() {
        return score;
    }

    public int getLife() {
        return life;
    }

    public Timer getInGameTimer() {
        return inGameTimer;
    }

    public Map getMap() {
        return map;
    }

    public void setRa() {
        MagnetBall.getRa = racket.getC();
    }

    public List<Boost> getBoosts() {
        return boosts;
    }
}
