package dk.dtu.compute.se.pisd.roborally.gameselection.controller;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.gameselection.model.Game;
import dk.dtu.compute.se.pisd.roborally.gameselection.model.GameState;
import dk.dtu.compute.se.pisd.roborally.gameselection.model.OnlineState;
import dk.dtu.compute.se.pisd.roborally.gameselection.model.User;
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

            for(Game game : games){
                System.out.println(game.getOwner().getUid());
                System.out.println(game.getOwner().getName());
            }
        }catch(Exception err){
            System.out.println("Error in onlinecontroller");
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
            System.out.println("Error in creating new game: ");
            System.out.println(e.getMessage());
        }
    }
}
