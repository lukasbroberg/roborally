package dk.dtu.compute.se.pisd.roborally.gameselection.view;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.gameselection.controller.OnlineController;
import dk.dtu.compute.se.pisd.roborally.gameselection.model.Game;
import dk.dtu.compute.se.pisd.roborally.gameselection.model.OnlineState;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URISyntaxException;

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

    public void createNewGame(AppController appController){
        TextField maxPlayersField = new TextField();
        TextField minPlayersField = new TextField();
        TextField nameField = new TextField();

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
        grid.add(new Label("Game Name:"),0,3);
        grid.add(nameField,1,3);

        createNewGameDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
        createNewGameDialog.getDialogPane().setContent(grid);

        createNewGameDialog.showAndWait().ifPresent(response ->{
            if(response ==ButtonType.OK){
                Game newGame = new Game();
                    newGame.setMinPlayers(Integer.parseInt(minPlayersField.getText()));
                    newGame.setMaxPlayers(Integer.parseInt(minPlayersField.getText()));
                    newGame.setName(nameField.getText());
                try{
                    appController.createGame(newGame);
                }catch (Exception err){
                    System.out.println(err.getMessage());
                }
            }
        });


    }
}
