package dk.dtu.compute.se.pisd.roborally.gameselection.view;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.BoardFactory;
import dk.dtu.compute.se.pisd.roborally.gameselection.controller.OnlineController;
import dk.dtu.compute.se.pisd.roborally.gameselection.model.OnlineState;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URISyntaxException;

public class GameSelection extends BorderPane {

    private AppController appController;
    private OnlineController onlineController;
    private AppDialogs appDialogs;

    public GameSelection (AppController appcontroller, OnlineController onlineController) {
        this.appController = appcontroller;
        this.onlineController = onlineController;
        this.appDialogs = new AppDialogs(onlineController);
    }

    public void openGameSelectionView(){
        this.setPrefSize(GamesView.width,GamesView.height);

        Button createNewGame = new Button("New game");
        createNewGame.setMinWidth(80);
        createNewGame.setOnAction(e -> {
                appDialogs.createNewGame(appController);
        });
        Button close = new Button("Close");
        close.setOnAction((e) -> {
            appController.gameSelected(null);
        });

        // also here, this is a quick hack
        close.setMinWidth(50);
        close.setMinHeight(30);

        GridPane top = new GridPane();
        top.add(close,2, 0);
        top.add(createNewGame,3, 0);

        // we should also have a textfield for the selected user
        // who will own the created game

        Pane bottom  = new GamesView(appController, this);
        VBox vbox =  new VBox(top, bottom);

        this.getChildren().add(vbox);
    }


}
