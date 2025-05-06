package dk.dtu.compute.se.pisd.roborally.gameselection.view;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.gameselection.controller.OnlineController;
import dk.dtu.compute.se.pisd.roborally.gameselection.model.*;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import uk.co.blackpepper.bowman.Client;
import uk.co.blackpepper.bowman.ClientFactory;
import uk.co.blackpepper.bowman.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GamesView extends GridPane {

    public static final int width = 640;
    public static final int height = 400;

    private List<GameButtons> games;
    private RestClient restClient = RestClient.builder().baseUrl("http://localhost:8080/roborally/").build();

    private OnlineController onlineController;

    public GamesView(OnlineController onlineController, GameSelection gameSelection) {
        this.onlineController = onlineController;
        games = new ArrayList<>();
        this.setPadding(new Insets(10,10,10,10));
        this.getColumnConstraints().add(new ColumnConstraints(300));
        this.getColumnConstraints().add(new ColumnConstraints(90));
        this.getColumnConstraints().add(new ColumnConstraints(90));
        this.getColumnConstraints().add(new ColumnConstraints(90));
        this.getColumnConstraints().add(new ColumnConstraints(90));

        update();
    }

    private void update() {
        try {
            onlineController.getOpenGames();

            List<Game> openGames = onlineController.onlineState.getOpenGames();
            if(openGames.size()<=0){
                return;
            }

            for(var i=0; i<openGames.size(); i++){
                Game game = openGames.get(i);

                User gameOwner = game.getOwner();
                boolean isOwner = gameOwner.getUid() == onlineController.onlineState.getUser().getUid();
                boolean alreadyJoinedGame = false;
                GameState gameState = game.getState();

                //Create game details
                TextFlow gameInfo = new TextFlow();
                Text gameName = new Text("Game: " + game.getName() + ", min: " + game.getMinPlayers() + ", max: " + game.getMaxPlayers());
                gameInfo.getChildren().add(gameName);

                for(Player JoinedPlayer : game.getPlayers()){
                    Text JoinedPlayerText = new Text("\n    Player " + JoinedPlayer.getName());
                    gameInfo.getChildren().add(JoinedPlayerText);
                    if(JoinedPlayer.getUser().getUid() == onlineController.onlineState.getUser().getUid()){
                        alreadyJoinedGame=true;
                    }
                }

                gameInfo.setPadding(new Insets(0,0,0,0));

                Button joinButton = new Button("Join");
                joinButton.setOnAction(e ->{
                    onlineController.joinGame(game);
                });

                Button leaveButton = new Button("Leave");
                leaveButton.setOnAction(e -> {
                    User user = onlineController.onlineState.getUser();
                    Player userPlayer = null;
                    for (Player player : game.getPlayers()){
                        if(player.getUser().getUid()==user.getUid()){
                            userPlayer=player;
                            userPlayer.setUid(player.getUid());
                        }
                    }

                    if(userPlayer!=null) {
                        onlineController.leaveGame(userPlayer);
                    }
                });

                Button startButton = new Button("Start");
                startButton.setOnAction(e ->{
                    onlineController.startGame(game);
                });

                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(e -> {
                    onlineController.deleteGame(game);
                });


                //Adjust based off available buttons
                if(isOwner){
                    leaveButton.setDisable(true);
                    joinButton.setDisable(true);

                    if(game.getPlayers().size()<game.getMinPlayers()){
                        startButton.setDisable(true);
                    }
                }else{
                    startButton.setDisable(true);
                    deleteButton.setDisable(true);

                    if(alreadyJoinedGame==true){
                        joinButton.setDisable(true);
                    }else{
                        leaveButton.setDisable(true);
                    }
                }

                if(gameState!=GameState.SIGNUP || game.getPlayers().size()>=game.getMaxPlayers()){
                    joinButton.setDisable(true);
                }

                this.add(gameInfo, 0, i);
                this.add(joinButton, 1, i);
                this.add(leaveButton, 2, i);
                this.add(startButton, 3, i);
                this.add(deleteButton, 4, i);

            }
        } catch (Exception e) {
            System.out.println("Error in gamesview");
            System.out.println(e.getMessage());
            Label text = new Label("There was a problem with loading the games.");
            this.add(text, 0,0);
        }


    }

    private class GameButtons {

        public final Game game;
        public final Button nameButton;

        public final Button startButton;

        public final Button deleteButton;

        public GameButtons(Game game, Button nameButton, Button startButton, Button deleteButton) {
            this.game = game;
            this.nameButton = nameButton;
            this.startButton = startButton;
            this.deleteButton = deleteButton;
        }
    }

}
