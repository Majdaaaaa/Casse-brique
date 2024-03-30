package physics.config;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import physics.AppPhysic;
import physics.entity.Ball;
import physics.entity.Racket;
import physics.geometry.Coordinates;
import physics.geometry.Vector;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Shape;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

import java.util.Map;

import gui.GraphicsFactory.BallGraphics;
import gui.GraphicsFactory.RacketGraphics;
import physics.gui.OptionBar;
import physics.gui.Preview;
import utils.GameConstants;
import utils.Key;

/***************************************************************************
 * Explication de classe Simulation
 * 
 * @var physics contient les informations de la simulation
 * @var Ball ball : La balle de la simulation
 * @var Racket racket : La raquette de la simulation
 * 
 * @param primaryStage : la fenetre de la simulation
 * @param physics      : les informations de la simulation
 * 
 * @fonction update : permet de mettre à jour la position de la balle et de la
 *           raquette
 * 
 * @author Ilias Bencheikh
 **************************************************************************/

public class PhysicEngine {

    //Initialisation des objets
    public static double f_WIDTH = GameConstants.DEFAULT_WINDOW_WIDTH;
    public static double d_WIDTH = 0;
    public static boolean PATH=true; 
    public static boolean RACKET=true;
    
    private Stage primaryStage;
    private Pane root;
    private OptionBar optionBar;

    PhysicSetting physics;

    Map<Ball,BallGraphics> listball=new java.util.HashMap<>();
    BallGraphics graphBall;

    Racket racket;
    Shape graphRacket;
    Key key = new Key();

    public static Set<KeyCode> direction = new HashSet<KeyCode>();
    public static boolean BougePColision;


    public PhysicEngine(Stage pStage, AppPhysic appPhysic) {

        // Initialisation de la fenetre

        this.primaryStage = pStage;
        root = new Pane();
        optionBar = new OptionBar(root,listball);
        primaryStage.setScene(new javafx.scene.Scene(root, GameConstants.DEFAULT_WINDOW_WIDTH, GameConstants.DEFAULT_WINDOW_HEIGHT));

        // Initialisation de la simulation

        this.physics = AppPhysic.physics;

        Ball ball = init_ball();
        graphBall = new BallGraphics(null,ball);
        root.getChildren().add(graphBall);
        listball.put(ball, graphBall);
    

        if(RACKET){
            racket=init_racket();
            graphRacket = (new RacketGraphics(racket, racket.getShapeType())).getShape();            graphRacket.setFill(Color.BLACK);
            root.getChildren().add(graphRacket);
        }
        primaryStage.show();

        // Initialisation de la trajectoire

        ball.setPreview(new Preview(ball, physics, root));
        ball.getPreview().init_path();

        //Initialisation des evenements de la souris

        setTakeBall(graphBall,ball,optionBar,root);

        // Initialisation de l'animation

        AnimationTimer animationTimer = new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                KeyPressed();
                if (RACKET) {
                    racket.handleKeyPress(key.getKeysPressed());
                }
                if (last == 0) {
                    last = now;
                    return;
                }
                var deltaT = now - last;
                if (deltaT > 1000000000 / 120) {
                    update();
                }
                last = now;
            }

            public void KeyPressed() {
                if (key.getKeysPressed().contains(KeyCode.ESCAPE)) {
                    stop();
                    primaryStage.setScene(appPhysic.getScene());
                    appPhysic.menu();
                }
                else if(key.getKeysPressed().contains(KeyCode.M)){
                    optionBar.update(ball);
                    key.getKeysPressed().remove(KeyCode.M);
                }
            }
        };

        animationTimer.start();
    }

    public void update() {
        if(!optionBar.isBar()){
            // Mise à jour de la position de la balle et de la trajectoire
            for(Ball b : listball.keySet()){
                if(!listball.get(b).IsMouseDraggingBall()){
                    for(Ball b2 : listball.keySet()){
                        if(b!=b2){
                            b.checkCollisionOtherBall(b2);
                        }
                    }
                    b.movement();
                    if(b.getPreview()!=null){
                        b.updatePreview();
                    }
                }
            }
        }

        for(Ball b : listball.keySet()){
            listball.get(b).update();
            b.CollisionB=false;
        }

        // Mise à jour de la position de la raquette
        if(RACKET){
            update_racket();
        }
    }

    public static Ball init_ball(){
        PhysicSetting physics = AppPhysic.physics;
        return new Ball(GameConstants.DEFAULT_BALL_START_COORDINATES.clone(),
        GameConstants.DEFAULT_BALL_START_DIRECTION.clone(),1, physics.getRadius()){
            @Override
            public boolean movement() {
                double h = GameConstants.DEFAULT_WINDOW_HEIGHT;
                double w = f_WIDTH;
                double newX = this.getC().getX() + this.getDirection().getX() * this.getSpeed() ;
                double newY = this.getC().getY() + this.getDirection().getY() * this.getSpeed() ;
                if (CollisionR) {
                    if (BougePColision || CollisionR_Side) {
                        this.getDirection().setY(-this.getDirection().getY()
                                + (this.getRotation().getAngle()) / 90 * this.getDirection().getY());
                        this.getRotation().Collision();
                        newY = this.getC().getY() + this.getDirection().getY() * this.getSpeed();
                        CollisionR = false;
                        CollisionR_Side = false;
                    }
                    else {
                        for (KeyCode key : direction) {
                            switch (key) {
                                case RIGHT:
                                case D:
                                    this.getDirection().setY(-this.getDirection().getY()
                                            + (this.getRotation().getAngle()) / 90 * this.getDirection().getY());
                                    this.getRotation().addEffect('d');
                                    newX = this.getC().getX() + this.getDirection().getX() * this.getSpeed();
                                    newY = this.getC().getY() + this.getDirection().getY() * this.getSpeed();
                                    CollisionR = false;
                                    break;
                                case LEFT:
                                case Q:
                                    this.getDirection().setY(-this.getDirection().getY()
                                            + (this.getRotation().getAngle()) / 90 * this.getDirection().getY());
                                    this.getRotation().addEffect('g');
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
                if (newX < d_WIDTH || newX > w - this.getRadius()) {
                    this.getDirection().setX(-this.getDirection().getX());
                    this.getDirection().setY(this.getDirection().getY()+(this.getRotation().getAngle())/90*this.getDirection().getY());
                    this.getRotation().Collision();
                    newX = this.getC().getX() + this.getDirection().getX() * this.getSpeed();
                    this.getDirection().setX(this.getDirection().getX() * physics.getRetention());
                }
                if (newY < 0 || newY > h - this.getRadius()) {
                    this.getDirection().setY(-this.getDirection().getY()
                            + (this.getRotation().getAngle()) / 90 * this.getDirection().getY());
                    this.getRotation().Collision();
                    newY = this.getC().getY() + this.getDirection().getY() * this.getSpeed();
                    this.getDirection().setY(this.getDirection().getY() * physics.getRetention());
                }
                this.setC(new Coordinates(newX, newY));
                this.getDirection().add(physics.getWind());
                physics.checkGravity(this.getC(), this.getDirection());
                return true;
            }
        };
    }

    public Racket init_racket() {
        return new Racket(200, 20, "rectangle", 8, false, true) {
            @Override
            public void handleKeyPress(Set<KeyCode> keysPressed) {
                for (KeyCode key : keysPressed) {
                    if(key==GameConstants.LEFT){
                        if (this.mX() > -largeur / 2 + d_WIDTH)
                            this.mX(this.mX() - speed);
                    }
                    if(key==GameConstants.RIGHT){
                        if (this.mX() < f_WIDTH - longueur - 70)
                            this.mX(this.mX() + speed);
                    }
                    if (key == GameConstants.SPACE) {
                        setlargeurP(true);
                        setVitesseP(true);
                    }
                }
            }

            @Override
            public void handleKeyRelease(KeyCode event) {
                // Fonction non utilisée
            }
        };
    }

    public void update_racket() {
        primaryStage.getScene().setOnKeyReleased(eWind -> {
            key.getKeysPressed().remove(eWind.getCode());
        });
        BougePColision = key.isEmpty();
        direction = key.getKeysPressed();
        for(Ball ball : listball.keySet()){
            if (physics.checkCollisionRacket(racket, ball)) {
                ball.setCollisionR(true);
            }
            if(ball.getPreview()!=null){
                ball.getPreview().update(racket);
            }
        }
        primaryStage.getScene().setOnKeyPressed(eWind -> {
            key.getKeysPressed().add(eWind.getCode());
        });
        ((Rectangle)graphRacket).setX(racket.mX());
        ((Rectangle)graphRacket).setY(racket.mY());
    }

    public static void setTakeBall(BallGraphics g,Ball b,OptionBar optionBar,Pane root){
        g.setOnMousePressed(event -> {
            if(!optionBar.isBar()){
                g.setMouseXY(event.getSceneX(),event.getSceneY());
                double ballX = b.getC().getX();
                double ballY = b.getC().getY();
                double distance = Math.sqrt(Math.pow(g.getMouseX() - ballX, 2) + Math.pow(g.getMouseY() - ballY, 2));
                if (distance <= b.getRadius()+10) {
                    g.setMouseDraggingBall(true);
                    if(b.getPreview()!=null){
                        b.getPreview().clear_path(root);
                    }
                }
            }
        });

        g.setOnMouseDragged(event -> {
            if (g.IsMouseDraggingBall() && !optionBar.isBar()) {
                if(event.getSceneX() > d_WIDTH+b.getRadius() && event.getSceneX() < f_WIDTH-b.getRadius() && event.getSceneY() > b.getRadius() && event.getSceneY() < GameConstants.DEFAULT_WINDOW_HEIGHT-b.getRadius()){
                    b.getC().setX(event.getSceneX());
                    b.getC().setY(event.getSceneY());
                }
            }
        });

        g.setOnMouseReleased(event -> {
            if (g.IsMouseDraggingBall()&& !optionBar.isBar()) {
                g.setMouseDraggingBall(false);
                double dx = (event.getSceneX() - g.getMouseX()) / 40;
                double dy = (event.getSceneY() - g.getMouseY()) / 40;
                if (dx > 7) {
                    dx = 7;
                }
                if (dy > 7) {
                    dy = 7;
                }
                b.getRotation().stopRotation();
                b.setDirection(new Vector(new Coordinates(dx, dy)));
                if(b.getPreview()!=null){
                    b.getPreview().init_path();
                }
            }
        });
    }
}