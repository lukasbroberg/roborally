package dk.dtu.compute.se.pisd.roborally.gameselection.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import uk.co.blackpepper.bowman.annotation.LinkedResource;
import uk.co.blackpepper.bowman.annotation.RemoteResource;
import uk.co.blackpepper.bowman.annotation.ResourceId;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RemoteResource("/user")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(
        scope = User.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "uid"
)
public class User {

    private long uid;

    private String name;

    public List<Player> players = new ArrayList<>();

    public User() {};

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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        // IMPORTANT: Don't try to add more information here,
        //            since this will invoke a REST request!
        return "User{ id=" + getUid() + " }";
    }

}
