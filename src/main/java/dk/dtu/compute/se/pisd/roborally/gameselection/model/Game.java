package dk.dtu.compute.se.pisd.roborally.gameselection.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.intellij.lang.annotations.Identifier;
import uk.co.blackpepper.bowman.annotation.LinkedResource;
import uk.co.blackpepper.bowman.annotation.RemoteResource;
import uk.co.blackpepper.bowman.annotation.ResourceId;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RemoteResource("/game")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(
        scope = Game.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "uid"
)
public class Game {

    private long uid;

    private String name;

    private int minPlayers;

    private int maxPlayers;

    private GameState state;

    private List<Player> players = new ArrayList<>();

    private User owner;

    public Game() {};

    public void setState(GameState state){
        this.state=state;
    }

    public GameState getState(){
        return this.state;
    }

    public User getOwner(){
        return this.owner;
    }

    public void setOwner(User owner){
        this.owner = owner;
    }

    public long getUid() {
        return this.uid;
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

    @LinkedResource(optionalLink = true)
    public List<Player> getPlayers() {
        return players;
    }

    public  void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @Override
    public String toString() {
        // IMPORTANT: Don't try to add more information here,
        //            since this will invoke a REST request!
        return "Game{ id=" + getUid() + " }";
    }

}