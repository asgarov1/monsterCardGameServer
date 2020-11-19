package at.fhtw.bif3.domain;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

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

    private Set<Card> cards = new HashSet<>();
    private Set<Card> deck = new HashSet<>();
//TODO question: does deck get generated randomly? so it doesn't need to be persisted?
    private Stats stats;
    private int numberOfCoins = 20;

    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User player = (User) o;
        return numberOfCoins == player.numberOfCoins &&
                Objects.equals(id, player.id) &&
                Objects.equals(username, player.username) &&
                Objects.equals(password, player.password) &&
                cards.size() == player.getCards().size();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, numberOfCoins, cards);
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
