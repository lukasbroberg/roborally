package dk.dtu.compute.se.pisd.roborally.gameselection.view;

import dk.dtu.compute.se.pisd.roborally.gameselection.controller.OnlineController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;

public class AppDialogs {
    private OnlineController onlineController;

    public AppDialogs(OnlineController onlineController){
        this.onlineController=onlineController;
    }

    public void signIn(){
        Stage window = new Stage();

        Text registerNewText = new Text("Sign in as user");
        TextField usernameField = new TextField();
        Text feedbackText = new Text();

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(
                e -> {
                    window.close();
                }
        );

        Button signInButton = new Button("Sign in");
        signInButton.setOnAction(
                e->{
                    feedbackText.setText(""); // clear feedback text
                    String username = usernameField.getText();
                    if(username.length()>3){
                        onlineController.signIn(usernameField.getText());
                        window.close();
                    }
                    feedbackText.setText("Username must be greater than 3 characters.");

                }
        );

        HBox buttonsBox = new HBox(cancelButton, signInButton);
        VBox newUserBox = new VBox(registerNewText, usernameField, feedbackText, buttonsBox);

        //Assemble scene
        Scene scene = new Scene(newUserBox);

        //Assemble window:
        window.setTitle("Register new user");
        window.setScene(scene);
        window.sizeToScene();
        window.setMinWidth(200);
        window.initModality(Modality.APPLICATION_MODAL);
        window.show();
    }

    public void registerNew(){


    }

    public void dialogMessage(String windowTitle, String message){
        Stage window = new Stage();
        Text welcomeText = new Text(message);
        Button okButton = new Button("ok");
        okButton.setOnAction(e -> window.close());
        VBox vbox = new VBox(welcomeText,okButton);
        Scene scene = new Scene(vbox);
        window.setTitle(windowTitle);
        window.setScene(scene);
        window.setMinHeight(100);
        window.setMinWidth(200);
        window.initModality(Modality.APPLICATION_MODAL);
        window.show();




    }

    public void createNewGame(){
        //Dialog for creating the new game..
    }
}
