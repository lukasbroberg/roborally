package dk.dtu.compute.se.pisd.roborally.gameselection.view;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.BoardFactory;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URISyntaxException;

public class GameSelection extends BorderPane {

    AppController appController;


    public GameSelection (AppController appcontroller) {
        this.appController = appcontroller;

        this.setPrefSize(GamesView.width,GamesView.height);

        TextField maxPlayersField = new TextField();
        TextField minPlayersField = new TextField();
        TextField gameIdField = new TextField();
        TextField nameField = new TextField();

        Button close = new Button("Close");
        Button createNewGame = new Button("New game");
        createNewGame.setMinWidth(80);

        Dialog<ButtonType> createNewGameDialog = new Dialog<>();
        createNewGameDialog.setTitle("Create New Game");
        //layout for form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Max players:"),0,0);
        grid.add(maxPlayersField,1,0);
        grid.add(new Label("Min players:"),0,1);
        grid.add(minPlayersField,1,1);
        grid.add(new Label("Game ID:"),0,2);
        grid.add(gameIdField,1,2);
        grid.add(new Label("Game Name:"),0,3);
        grid.add(nameField,1,3);
        // create button OK and CANCEL
      createNewGameDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
      createNewGameDialog.getDialogPane().setContent(grid);

        close.setOnAction((e) -> appcontroller.gameSelected(null));
        createNewGame.setOnAction((e) ->
                createNewGameDialog.showAndWait().ifPresent(response ->{
                    if(response ==ButtonType.OK){
                        int maxPlayers =Integer.parseInt(maxPlayersField.getText());
                        int minPlayers = Integer.parseInt(minPlayersField.getText());
                        String gameID = gameIdField.getText();
                        String gameName = nameField.getText();

                        try {
                            appController.createGame(gameID,gameName,minPlayers,maxPlayers);
                        } catch (URISyntaxException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }));

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
