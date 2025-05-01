package dk.dtu.compute.se.pisd.roborally.gameselection.model;

public class OnlineState {

    private User user;

    public void setUser(User user){
        this.user=user;
    }

    public User getUser(){
        return this.user;
    }

    public void removeCurrentUser(){
        this.user=null;
    }
}
