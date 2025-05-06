package dk.dtu.compute.se.pisd.roborally.gameselection.model;

import java.util.List;

public class OnlineState {

    private User user;

    private List<Game> openGames;
    private Game signedUpGame;

    public void setUser(User user){
        this.user=user;
    }

    public User getUser(){
        return this.user;
    }

    public void removeCurrentUser(){
        this.user=null;
    }

    public void setOpenGames(List<Game> openGames){
        this.openGames=openGames;
    }

    public List<Game> getOpenGames() {
        return openGames;
    }

    public Game getSignedUpGame() {
        return signedUpGame;
    }

    public void setSignedUpGame(Game signedUpGame) {
        this.signedUpGame = signedUpGame;
    }
}
