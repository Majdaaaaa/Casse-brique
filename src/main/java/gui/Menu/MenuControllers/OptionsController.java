package gui.Menu.MenuControllers;

import gui.App;
import gui.Menu.Menu.Theme;
import gui.Menu.MenuViews.OptionsView;
import javafx.application.Platform;
import javafx.stage.Stage;
import utils.GameConstants;

/**
 * Classe OptionsController qui gère les interactions de l'utilisateur avec la
 * vue OptionsView.
 * Elle permet de modifier les elements modifiables du jeu.
 * 
 * @see OptionsView
 * @author Benmalek Majda
 */
public class OptionsController {
    private OptionsView view;

    /**
     * Constructeur de OptionsController.
     * 
     * @param p Le stage principal sur lequel la vue des options est affichée.
     */
    public OptionsController(Stage p, OptionsView view) {
        this.view = view;

        this.view.getBackButton().setOnAction(e -> back());
        this.view.getFps().getToggleButton().setOnAction(e -> fps());
        this.view.getPath().getToggleButton().setOnAction(e -> path());
        this.view.getParticles().getToggleButton().setOnAction(e -> particles());
        this.view.getVolumeMusic().getSlider().valueProperty().addListener((observable, oldValue, newValue) -> {
            GameConstants.MUSIC = newValue.intValue();
        });
        this.view.getVolumeSound().getSlider().valueProperty().addListener((observable, oldValue, newValue) -> {
            GameConstants.SOUND = newValue.intValue();
        });
        this.view.getTheme().getComboBox().getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    Theme();
                });
        this.view.getButtonleft().getButton().setOnAction(e -> left());
        this.view.getButtonpower().getButton().setOnAction(e -> power());
        this.view.getButtonright().getButton().setOnAction(e -> right());

    }

    /**
     * Méthode pour revenir à la vue du menu de démarrage.
     */
    private void back() {
        Platform.runLater(() -> {
            App.sceneManager.changeScene(view.getPrimaryStage(), "StartMenuView");
        });
    }

    /**
     * Méthode pour gérer l'activation/désactivation du FPS.
     */
    private void fps() {
        if (view.getFps().getToggleButton().isSelected()) {
            view.getFps().getToggleButton().setText("ON");
        } else {
            view.getFps().getToggleButton().setText("OFF");
        }
        GameConstants.FPS = this.view.getFps().getToggleButton().isSelected();
    }

    /**
     * Méthode pour gérer l'activation/désactivation du chemin.
     */
    private void path() {
        if (view.getPath().getToggleButton().isSelected()) {
            view.getPath().getToggleButton().setText("ON");
        } else {
            view.getPath().getToggleButton().setText("OFF");
        }
        GameConstants.PATH = this.view.getPath().getToggleButton().isSelected();
    }

    /**
     * Méthode pour gérer l'activation/désactivation des particules.
     */
    private void particles() {
        if (view.getParticles().getToggleButton().isSelected()) {
            view.getParticles().getToggleButton().setText("ON");
        } else {
            view.getParticles().getToggleButton().setText("OFF");
        }
        GameConstants.PARTICLES = this.view.getParticles().getToggleButton().isSelected();
    }

    /**
     * Méthode pour gérer l'activation de la touche gauche.
     */
    private void left() {
        view.getButtonleft().getButton().setText("Appuyez sur une touche...");
        view.getButtonleft().getButton().setOnKeyPressed(event -> {
            GameConstants.LEFT = event.getCode();
            view.getButtonleft().getButton().setText(GameConstants.LEFT.getName());
            view.getButtonleft().getButton().setOnKeyPressed(null); // Désactive la liaison après la configuration
        });
    }

    /**
     * Méthode pour gérer l'activation de la touche d'alimentation.
     */
    private void power() {
        view.getButtonpower().getButton().setText("Appuyez sur une touche...");
        view.getButtonpower().getButton().setOnKeyPressed(event -> {
            GameConstants.SPACE = event.getCode();
            view.getButtonpower().getButton().setText(GameConstants.SPACE.getName());
            view.getButtonpower().getButton().setOnKeyPressed(null); // Désactive la liaison après la configuration
        });
    }

    /**
     * Méthode pour gérer l'activation de la touche droite.
     */
    private void right() {
        view.getButtonright().getButton().setText("Appuyez sur une touche...");
        view.getButtonright().getButton().setOnKeyPressed(event -> {
            GameConstants.RIGHT = event.getCode();
            view.getButtonright().getButton().setText(GameConstants.RIGHT.getName());
            view.getButtonright().getButton().setOnKeyPressed(null); // Désactive la liaison après la configuration
        });
        ;
    }

    private void Theme() {
        String selectedTheme = view.getTheme().getComboBox().getValue();
        if (selectedTheme != null) {
            GameConstants.CSS = Theme.valueOf(selectedTheme.toUpperCase());
            //change le css de chaque scene dans SceneManager
            App.sceneManager.getScenes().forEach((k, v) -> {
                App.sceneManager.addStylesheet(v);
            });
        }
    }
}

