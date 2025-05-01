package dk.dtu.compute.se.pisd.roborally.gameselection.controller;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.gameselection.model.OnlineState;
import dk.dtu.compute.se.pisd.roborally.gameselection.model.User;
import dk.dtu.compute.se.pisd.roborally.gameselection.view.AppDialogs;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import javax.swing.*;
import java.util.List;

public class OnlineController {

    private RestClient restClient = RestClient.builder().baseUrl("http://localhost:8080/roborally/").build();
    private AppController appController;
    private OnlineState onlineState;

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
                //Create user object
                User newUser = new User();
                newUser.setName(username);
                User user = restClient.post().uri("/users").body(newUser).retrieve().body(User.class);
                AppDialogs createdNewUserDialog = new AppDialogs(this);
                createdNewUserDialog.dialogMessage("New user created!", "Succesfully registered new user: " + user.getName());
                return;
            }

            User user = users.get(0);

            //Assign user & welcome user
            onlineState.setUser(user);
            AppDialogs welcomeDialog = new AppDialogs(this);
            welcomeDialog.dialogMessage("Signed in","Welcome " + user.getName());
        }catch(Exception err){
            AppDialogs ExceptionDialog = new AppDialogs(this);
            ExceptionDialog.dialogMessage("Unable to sign in", err.getMessage());
        }
    }

    public void signOut(){
        if(onlineState.getUser()==null){
            throw new IllegalStateException("User is not signed in");
        }

        User user = onlineState.getUser();
        onlineState.removeCurrentUser();
        AppDialogs signedOutDialog = new AppDialogs(this);
        signedOutDialog.dialogMessage("Signed out", "Succesfully signed out as " + user.getName());
    }
}
