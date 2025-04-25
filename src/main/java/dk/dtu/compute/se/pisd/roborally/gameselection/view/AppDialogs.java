package dk.dtu.compute.se.pisd.roborally.gameselection.view;

import dk.dtu.compute.se.pisd.roborally.gameselection.controller.OnlineController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AppDialogs {
    private OnlineController onlineController;

    public AppDialogs(OnlineController onlineController){
        this.onlineController=onlineController;
    }

    public void signIn(){
        Stage window = new Stage();

        Text registerNewText = new Text("Register new user");
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
                        window.close();
                        onlineController.signIn(usernameField.getText());
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

        window.show();
    }

    public void createNewGame(){
        //Dialog for creating the new game..
    }
}
