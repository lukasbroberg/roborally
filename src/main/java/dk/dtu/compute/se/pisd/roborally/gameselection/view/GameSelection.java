package dk.dtu.compute.se.pisd.roborally.gameselection.view;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GameSelection extends BorderPane {

    AppController appController;


    public GameSelection (AppController appcontroller) {
        this.appController = appcontroller;

        this.setPrefSize(GamesView.width,GamesView.height);


        Button close = new Button("Close");
        Button createNewGame = new Button("Create new");
        Dialog<String> createNewGameDialog = new Dialog<String>();
        createNewGameDialog.setContentText("test");
        close.setOnAction((e) -> appcontroller.gameSelected(null));
        createNewGame.setOnAction((e) ->
                createNewGameDialog.show()
        );
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
