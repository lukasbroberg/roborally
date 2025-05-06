package dk.dtu.compute.se.pisd.roborally.gameselection.view;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.BoardFactory;
import dk.dtu.compute.se.pisd.roborally.gameselection.controller.OnlineController;
import dk.dtu.compute.se.pisd.roborally.gameselection.model.OnlineState;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.net.URISyntaxException;

public class GameSelection extends BorderPane {

    private AppController appController;
    private OnlineController onlineController;
    private AppDialogs appDialogs;
    private GamesView gamesView;

    public GameSelection (AppController appcontroller, OnlineController onlineController) {
        this.appController = appcontroller;
        this.onlineController = onlineController;
        this.appDialogs = new AppDialogs(onlineController);
        this.gamesView = new GamesView(onlineController, this);
    }

    public void openGameSelectionView(){
        this.setPrefSize(GamesView.width,GamesView.height);

        Label signedInUser = new Label("Signed in as: " + onlineController.onlineState.getUser().getName());

        Button createNewGame = new Button("New game");
        createNewGame.setMinWidth(80);
        createNewGame.setOnAction(e -> {
            appDialogs.createNewGame(appController);
        });

        Button close = new Button("Close");
        close.setOnAction((e) -> {
            appController.gameSelected(null);
        });

        Button refresh = new Button("Refresh");
        refresh.setOnAction(e -> {
            appController.refreshGameSelection();
        });

        // also here, this is a quick hack
        close.setMinWidth(50);
        close.setMinHeight(30);

        GridPane top = new GridPane();
        top.setPadding(new Insets(5,5,5,5));
        top.getColumnConstraints().add(new ColumnConstraints(300));
        top.getColumnConstraints().add(new ColumnConstraints(90));
        top.getColumnConstraints().add(new ColumnConstraints(90));
        top.getColumnConstraints().add(new ColumnConstraints(90));
        top.getColumnConstraints().add(new ColumnConstraints(90));

        top.add(signedInUser,0, 0);
        top.add(createNewGame,1, 0);
        top.add(refresh,2, 0);
        top.add(close,3, 0);

        Pane bottom  = gamesView;
        VBox vbox =  new VBox(top, bottom);

        this.getChildren().add(vbox);
    }


}
