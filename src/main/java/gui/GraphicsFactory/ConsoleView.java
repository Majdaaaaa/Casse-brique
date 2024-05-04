package gui.GraphicsFactory;

import java.util.Timer;
import java.util.TimerTask;

import gui.App;
import gui.Console;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
* Créé l'objet qui sert d'afficheur pour la console, à chaque création il rétablit toute l'historique en cours.
*
* @info
* s'auto-update indépendamment
*/
public class ConsoleView extends VBox {

    private Stage stage;
    private Scene scene;

    private ScrollPane scrollPane;
    private VBox consoleTextArea;
    private HBox sendBox;
    private TextField inputField;
    private Button sendButton;

    private Timer updateTimer;

    public ConsoleView() {
        registerFocusStage();
        initComponents();
        getChildren().addAll(scrollPane, sendBox);
        startAutoUpdate();
        setStaticStyle();
    }

    public void registerFocusStage() { // EN TEST
        this.stage = App.primaryStage;
        this.stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.B) {
                inputField.requestFocus();
                System.out.println("Touche Entrée pressée sur le Stage");
            }
        });
    }

    private void initComponents() { // l'ordre compte, influe sur la taille déf
        initSendBox();
        initScrollPane();
    }

    private void initScrollPane() {
        scrollPane = new ScrollPane();
        consoleTextArea = new VBox();
        scrollPane.setContent(consoleTextArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setMaxHeight(200);
        scrollPane.setPrefWidth(scrollPane.getWidth());
        scrollPane.needsLayoutProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                scrollPane.setVvalue(1.0);
            }
        });
    }

    private void initSendBox() {
        sendBox = new HBox();

        inputField = new TextField();
        inputField.setPromptText("Entrez un message ou une commande (/) ...");
        inputField.setPrefWidth(300);
        inputField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                sendMessage();
            }
        });

        sendButton = new Button("Envoyer");
        sendButton.setOnAction(e -> {
            sendMessage();
            inputField.requestFocus();
        });

        sendBox.getChildren().addAll(inputField, sendButton);
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            inputField.clear();
            Platform.runLater(() -> {
                Console.process(message);
            });
        }
    }

    public void updateConsoleView() {
        String message = Console.getQueueMessage();
        if (message != null) {
            Label label = new Label(message);
            label.setWrapText(true);
            label.getStyleClass().add("console-scroll-pane-label");
            consoleTextArea.getChildren().add(label);
        }
    }

    private void startAutoUpdate() { // Vitesse de 10 messages / sec
        if (updateTimer != null) {
            updateTimer.cancel();
        }
        updateTimer = new Timer();
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> updateConsoleView());
            }
        }, 0, 100);
    }

    private void setStaticStyle() {

        this.getStyleClass().add("console");
        scrollPane.getStyleClass().addAll("console-scroll-pane", "console-scroll-pane-unfocus");
        consoleTextArea.getStyleClass().add("console-text-area");
        sendBox.getStyleClass().add("console-send-box");
        inputField.getStyleClass().add("console-input-field");
        sendButton.getStyleClass().add("console-send-button");

    }

    public void setDynamicStyle(Scene registerScene) {
        this.scene = registerScene;
        scrollPane.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                scrollPane.getStyleClass().add("console-scroll-pane-focus");
                scrollPane.getStyleClass().remove("console-scroll-pane-unfocus");
            } else {
                scrollPane.getStyleClass().add("console-scroll-pane-unfocus");
                scrollPane.getStyleClass().remove("console-scroll-pane-focus");
            }
        });

        inputField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                scrollPane.getStyleClass().add("console-scroll-pane-focus");
                scrollPane.getStyleClass().remove("console-scroll-pane-unfocus");
            } else {
                scrollPane.getStyleClass().add("console-scroll-pane-unfocus");
                scrollPane.getStyleClass().remove("console-scroll-pane-focus");
            }
        });

        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Node clickedNode = event.getPickResult().getIntersectedNode();
            // Vérifie si le clic a été effectué à l'extérieur du champ de texte
            if (!scrollPane.isFocused() && (clickedNode == null || !clickedNode.equals(inputField))) {
                scrollPane.getStyleClass().add("console-scroll-pane-unfocus");
                scrollPane.getStyleClass().remove("console-scroll-pane-focus");
            }
        });
    }

}
