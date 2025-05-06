package dk.dtu.compute.se.pisd.roborally.gameselection.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import uk.co.blackpepper.bowman.annotation.LinkedResource;
import uk.co.blackpepper.bowman.annotation.RemoteResource;
import uk.co.blackpepper.bowman.annotation.ResourceId;

import java.net.URI;

@RemoteResource("/player")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(
        scope = Player.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "uid"
)
public class Player {

    private long uid;

    private String name;

    private Game game;

    private User user;

    /*
       Note that Bowman client (or actually it uses javassis) requires
       every entity class to have a standard constructor.
     */
    public Player() {};

//    public Player(String name) {
//        this.name = name;
//    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        // IMPORTANT: Don't try to add more information here,
        //            since this will invoke a REST request!
        return "Player{ id = " + getUid() + " }";
    }

}
