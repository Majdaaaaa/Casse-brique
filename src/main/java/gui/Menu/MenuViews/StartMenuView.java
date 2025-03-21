package gui.Menu.MenuViews;

import gui.ViewPosition;
import gui.GraphicsFactory.ConsoleView;
import gui.GraphicsFactory.ProfileView;
import gui.Menu.BaseView;
import gui.Menu.Menu;
import gui.Menu.MenuControllers.StartMenuController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import utils.GameConstants;

/**
 * Classe StartMenuView qui implémente l'interface Menu pour représenter la vue
 * du menu de démarrage.
 * Elle contient les éléments graphiques du menu de démarrage.
 * 
 * @author Benmalek Majda
 */
public class StartMenuView implements Menu, ViewPosition {

    private Stage primaryStage;
    private BorderPane root = new BorderPane();

    private HBox topBox = new HBox();
    private VBox centerBox = new VBox();
    private HBox bottomBox = new HBox();
    private Scene scene = new Scene(root, GameConstants.DEFAULT_WINDOW_WIDTH,
            GameConstants.DEFAULT_WINDOW_HEIGHT);

    private ProfileView profileView;
    private Button btnPlay;
    private Button btnOptions;
    private Button btnQuit;
    private Button btnBoutique;
    private Button btnSave;
    private Button btnTuto;
    private Label title;
    private ConsoleView consoleView;
    private BaseView baseView;

    /**
     * Constructeur de StartMenuView.
     * 
     * @param p Le stage principal sur lequel le menu de démarrage est affiché.
     */
    public StartMenuView(Stage p) {
        this.primaryStage = p;

        createTop();
        createCenter();
        createBottom();

        root.setTop(topBox);
        root.setCenter(centerBox);
        root.setBottom(bottomBox);

        baseView = new BaseView(root, topBox, centerBox, bottomBox);

        new StartMenuController(p, this);
    }

    private void createTop() {
        profileView = new ProfileView();
        topBox.getChildren().addAll(profileView);
    }

    private void createCenter() {

        title = createLabel("Casse Brique", 0, 0);
        btnPlay = createButton("Jouer", 0, 0);
        btnOptions = createButton("Options", 0, 0);
        btnBoutique = createButton("Boutique", 0, 0);
        btnSave = createButton("Sauvegarder", 0, 0);
        btnTuto = createButton("Tutoriel", 0, 0);
        btnQuit = createButton("Quitter", 0, 0);
        setCenterBoxStyle();
        centerBox.getChildren().addAll(title, btnPlay, btnOptions, btnBoutique, btnSave, btnTuto, btnQuit);
    }

    private void setCenterBoxStyle() {
        title.getStyleClass().add("title-style");
        centerBox.getStyleClass().add("root");
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);
    }

    private void createBottom() {
        consoleView = ConsoleView.getInstance();
        consoleView.setDynamicFocus(scene);
    }

    public void applyGradientBackground() {
        LinearGradient linearGradient = null;
        switch (GameConstants.CSS) {
            case ACHROMATOPSIE:
                break;
            case BLACK:
                break;
            case CLASSIC:
                break;
            case DEUTERANOPIE:
                break;
            case LIGHT:
                break;
            case PINK:
                linearGradient = new LinearGradient(1, 1, 1, 0, true, CycleMethod.NO_CYCLE, new Stop[] {
                        new Stop(0, Color.web("#FEE5F2")),
                        new Stop(1, Color.web("#FFB6C1"))
                });
                break;
            case PROTANOPIE:
                break;
            case TRITANOPIE:
                break;
            default:
                break;
        }

        // Appliquer le LinearGradient à la scène
        root.setBackground(new Background(new BackgroundFill(linearGradient, CornerRadii.EMPTY, Insets.EMPTY)));
        topBox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        centerBox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        bottomBox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        profileView.getRoot()
                .setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    @Override
    public void update() {
        profileView.update();
        baseView.update();
    }

    @Override
    public void moveConsoleView() {
        if (!bottomBox.getChildren().contains(consoleView)) {
            bottomBox.getChildren().add(consoleView);
        }
    }

    @Override
    public void handleDynamicAction() {
        consoleView.setDynamicFocus(scene);
    }

    // getters pour les boutons et autres éléments de la vue

    /**
     * Méthode pour obtenir la racine de la vue.
     * 
     * @return La racine de la vue.
     */
    public BorderPane getRoot() {
        return root;
    }

    /**
     * Méthode pour obtenir le stage principal.
     * 
     * @return Le stage principal.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Méthode pour obtenir le bouton Jouer.
     * 
     * @return Le bouton Jouer.
     */
    public Button getBtnPlay() {
        return btnPlay;
    }

    /**
     * Méthode pour obtenir le bouton Options.
     * 
     * @return Le bouton Options.
     */
    public Button getBtnOptions() {
        return btnOptions;
    }

    /**
     * Méthode pour obtenir le bouton Quitter.
     * 
     * @return Le bouton Quitter.
     */
    public Button getBtnQuit() {
        return btnQuit;
    }

    /**
     * Méthode pour obtenir le bouton Sauvegarder.
     * 
     * @return Le bouton Sauvegarder.
     */
    public Button getBtnSave() {
        return btnSave;
    }

    /**
     * Méthode pour obtenir le bouton Boutique.
     * 
     * @return Le bouton Boutique.
     */
    public Button getBtnBoutique() {
        return btnBoutique;
    }

    /**
     * Méthode pour obtenir le bouton Tutoriel.
     * 
     * @return Le bouton Tutoriel.
     */
    public Button getBtnTuto() {
        return btnTuto;
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}