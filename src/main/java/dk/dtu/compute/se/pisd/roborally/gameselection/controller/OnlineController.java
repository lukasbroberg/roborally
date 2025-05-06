package dk.dtu.compute.se.pisd.roborally.gameselection.controller;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.gameselection.model.*;
import dk.dtu.compute.se.pisd.roborally.gameselection.view.AppDialogs;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import javax.swing.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

public class OnlineController {

    private RestClient restClient = RestClient.builder().baseUrl("http://localhost:8080/roborally/").build();
    private AppController appController;
    public OnlineState onlineState;

    public OnlineController(AppController appController) {
        this.appController = appController;
        this.onlineState = new OnlineState();
    }

    public void signIn(String username){

        //Query user, with username, from server:
        try{

            //User is already signed in - unable to sign in
            if(onlineState.getUser()!=null){
                throw new IllegalStateException("User is already signed in.");
            }

            //Fetch users from server
            List<User> users = restClient.get().uri(uriBuilder -> uriBuilder
                    .path("users/searchUsers").queryParam("name", username).build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            //Create new user:
            if(users.isEmpty()){

                Alert signUpAlert = new Alert(Alert.AlertType.CONFIRMATION);
                signUpAlert.setTitle("Register as new user");
                signUpAlert.setHeaderText("User doesn't exist");
                signUpAlert.setContentText("Want to sign up as a new user?");

                Optional<ButtonType> result = signUpAlert.showAndWait();

                if(result.isPresent() && result.get() == ButtonType.OK){
                    //Create user object
                    User newUser = new User();
                    newUser.setName(username);

                    User user = restClient.post().uri("/users").body(newUser).retrieve().body(User.class);
                    onlineState.setUser(user);

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("New user created");
                    successAlert.setHeaderText("New user succesfully created: " + user.getName());
                    successAlert.show();

                }
                return;
            }

            User user = users.get(0);

            //Assign user & welcome user
            onlineState.setUser(user);
            Alert welcomeAlert = new Alert(Alert.AlertType.INFORMATION);
            welcomeAlert.setTitle("Signed in");
            welcomeAlert.setHeaderText("Welcome " + user.getName());
            welcomeAlert.show();

        }catch(Exception err){
            Alert unableToSignInAlert = new Alert(Alert.AlertType.INFORMATION);
            unableToSignInAlert.setTitle("Unable to sign in");
            unableToSignInAlert.setHeaderText(err.getMessage().toString());
            unableToSignInAlert.show();
        }
    }

    public void signOut(){
        if(onlineState.getUser()==null){
            throw new IllegalStateException("User is not signed in");
        }

        User user = onlineState.getUser();
        onlineState.removeCurrentUser();

        Alert signOutAlert = new Alert(Alert.AlertType.INFORMATION);
        signOutAlert.setTitle("Signed out");
        signOutAlert.setHeaderText(user.getName() + " succesfully signed out");
        signOutAlert.show();
    }

    public void getOpenGames(){
        try{
            List<Game> games = restClient.get().uri("/games/openGames").retrieve().body(new ParameterizedTypeReference<>() {});
            onlineState.setOpenGames(games);
        }catch(Exception err){
            System.out.println(err.getMessage());
        }
    }

    public void createNewGame(Game newGame) throws IllegalStateException{
        if(onlineState.getUser()==null){
            throw new IllegalStateException("User is not signed in. Sign in to create a new game.");
        }

        newGame.setState(GameState.SIGNUP);

        try{
            User owner = new User();
            owner.setUid(onlineState.getUser().getUid());
            owner.setName(onlineState.getUser().getName());

            newGame.setOwner(owner);

            Game game = restClient.post().uri("games/createNewGame").body(newGame).retrieve().body(Game.class);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unable to create game");
            alert.setHeaderText("Unable to create game: " + e.getMessage());
            alert.show();
        }
    }

    public void deleteGame(Game game){
        User owner = game.getOwner();
        User user = onlineState.getUser();

        //Make sure its the owner, trying to delete the game
        if(owner.getUid()!=user.getUid()){
            return;
        }

        try{
            ResponseEntity<Void> result = restClient.delete().uri("/games/{id}", game.getUid()).retrieve().toBodilessEntity();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unable to delete game");
            alert.setHeaderText("Unable to delete game: " + e.getMessage());
            alert.show();
        }finally {
            appController.refreshGameSelection();
        }
    }

    public void startGame(Game game){
        User user = onlineState.getUser();
        User owner = game.getOwner();

        if(user.getUid()!=owner.getUid()){
            return;
        }

        try{
            ResponseEntity<Void> result = restClient.patch().uri("/games/start/{id}",game.getUid()).retrieve().toBodilessEntity();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unable to start game");
            alert.setHeaderText("Unable to start the game: " + e.getMessage());
            alert.show();
        }finally {
            appController.refreshGameSelection();
        }
    }

    public void joinGame(Game game){
        try{
            if(game.getPlayers().size()<game.getMaxPlayers()){
                Game joinGame = new Game();
                joinGame.setUid(game.getUid());

                User user = new User();
                user.setUid(onlineState.getUser().getUid());

                Player player = new Player();
                player.setUser(user);
                player.setName(user.getName());
                player.setGame(joinGame);

                Player newPlayer = restClient.post().uri("/players/createNew").body(player).retrieve().body(Player.class);
            }
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unable to join game");
            alert.setHeaderText("Unable to join new game: " + e.getMessage());
            alert.show();
        }finally {
            appController.refreshGameSelection();
        }
    }

    public void leaveGame(Player player){
        try{
            ResponseEntity<Void> result = restClient.delete().uri("/players/delete/{id}",player.getUid()).retrieve().toBodilessEntity();
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unable to leave game");
            alert.setHeaderText("Unable to leave game: " + e.getMessage());
            alert.show();
        }finally {
            appController.refreshGameSelection();
        }
    }
}
