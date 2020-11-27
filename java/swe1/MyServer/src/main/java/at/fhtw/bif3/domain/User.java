package at.fhtw.bif3.domain;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

import static at.fhtw.bif3.util.PropertiesReader.getProperties;
import static java.lang.Integer.parseInt;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @SerializedName("Id")
    private String id;

    @SerializedName("Username")
    private String username;

    @SerializedName("Password")
    private String password;

    @SerializedName("Bio")
    private String bio;

    @SerializedName("Image")
    private String image;

    private int numberOfGamesPlayed;
    private int elo;

    private Set<Card> cards = new HashSet<>();
    private Set<Card> deck = new HashSet<>();
//TODO question: deck gets generated randomly each round

    private int numberOfCoins = parseInt(getProperties().getProperty("user.start-money"));

    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this.id = username;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }

    public Set<Card> getCards() {
        return Collections.unmodifiableSet(cards);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }
}
