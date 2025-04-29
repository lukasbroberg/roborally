package dk.dtu.compute.se.pisd.roborally.gameselection.controller;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.gameselection.model.OnlineState;
import dk.dtu.compute.se.pisd.roborally.gameselection.model.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

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
            List<User> users = restClient.get().uri(uriBuilder -> uriBuilder
                    .path("users/searchUsers").queryParam("name", username).build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<User>>() {});

            System.out.println(users);

            //Create new user:
            /*TODO: Use POST request to create new user on the server*/
            if(users.isEmpty()){

            }

            //Sign user in:
            /*TODO: Use POST request to create new user on the server*/



        }catch(Exception err){
            System.out.println(err.getMessage());
        }



        //restClient.post()




    }

    public void signOut(){
        //Todo: implement signout
    }
}
