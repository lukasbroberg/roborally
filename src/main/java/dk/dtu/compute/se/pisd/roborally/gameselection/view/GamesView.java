package dk.dtu.compute.se.pisd.roborally.gameselection.view;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.gameselection.controller.OnlineController;
import dk.dtu.compute.se.pisd.roborally.gameselection.model.Game;

import dk.dtu.compute.se.pisd.roborally.gameselection.model.OnlineState;
import dk.dtu.compute.se.pisd.roborally.gameselection.model.Player;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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

        this.getColumnConstraints().add(new ColumnConstraints(200));
        this.getColumnConstraints().add(new ColumnConstraints(70));
        this.getColumnConstraints().add(new ColumnConstraints(70));
        this.getColumnConstraints().add(new ColumnConstraints(70));
        this.getColumnConstraints().add(new ColumnConstraints(70));
        this.getColumnConstraints().add(new ColumnConstraints(70));
        this.getColumnConstraints().add(new ColumnConstraints(70));

        update();
    }

    private void update() {
        try {
            // Client<Game> clientGame = factory.create(Game.class);
            // Client<Player> clientPlayer = factory.create(Player.class);
            // Client<User> clientUser = factory.create(User.class);

            //Iterable<Game> games = clientGame.getAll();

            onlineController.getOpenGames();

            List<Game> openGames = onlineController.onlineState.getOpenGames();
            if(openGames.size()<=0){
                return;
            }

            for(var i=0; i<openGames.size(); i++){
                System.out.println(openGames.get(i).getName());
                Game game = openGames.get(i);

                Text gameName = new Text(game.getName());
                Text minPlayers = new Text("Min: " + game.getMinPlayers());
                Text maxPlayers = new Text("Max: " + game.getMaxPlayers());

                Button joinButton = new Button("Join");
                joinButton.setOnAction(e ->{
                    //Join game
                });
                Button leaveButton = new Button("Leave");
                leaveButton.setOnAction(e -> {
                    //Leave game
                });
                Button startButton = new Button("Start");
                startButton.setOnAction(e ->{
                    //Start game
                });
                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(e -> {
                    //delete game
                });

                VBox playersJoined = new VBox();
                List<Player> JoinedPlayers = openGames.get(i).getPlayers();
                for(Player JoinedPlayer : JoinedPlayers){
                    Text JoinedPlayerText = new Text(JoinedPlayer.getName());
                    playersJoined.getChildren().add(JoinedPlayerText);
                    playersJoined.setSpacing(10);
                }



                /*this.add(gameName, 0, i);
                this.add(minPlayers, 1, i);
                this.add(maxPlayers, 2, i);
                this.add(joinButton, 3, i);
                this.add(leaveButton, 4, i);
                this.add(startButton, 5, i);
                this.add(deleteButton, 6, i);
                this.add(playersJoined,0,1);*/

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
