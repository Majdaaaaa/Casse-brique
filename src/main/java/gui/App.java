package gui;

import gui.Menu.MenuControllers.StartMenuController;
import gui.Menu.MenuViews.StartMenuView;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    protected Stage primaryStage;

    @Override
    public void start(Stage p) throws Exception {
        this.primaryStage = p;
        this.primaryStage.setResizable(false);
        primaryStage.setTitle("Casse Brique");

        new StartMenuController(primaryStage, new StartMenuView(primaryStage));

        primaryStage.show();
        primaryStage.getOnCloseRequest();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
