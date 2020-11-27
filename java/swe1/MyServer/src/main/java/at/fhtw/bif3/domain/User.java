package at.fhtw.bif3.domain;

import at.fhtw.bif3.util.NumberUtil;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

import static at.fhtw.bif3.util.NumberUtil.randomInt;
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
    private int elo = parseInt(getProperties().getProperty("elo.start-value"));

    private List<Card> cards = new ArrayList<>();
    private List<Card> deck = new ArrayList<>();
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

    public void generateBattleDeck() {
        int numberOfElements = parseInt(getProperties().getProperty("deck.battle.size"));
        for (int i = 0; i < numberOfElements; i++) {
            Card randomCard = cards.get(randomInt(0, cards.size()));
            cards.remove(randomCard);
            deck.add(randomCard);
        }
    }

    public void returnCardsFromDeck() {
        cards.addAll(deck);
        deck.clear();
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public Card playRandomCard() {
        Card randomCard = deck.get(randomInt(0, deck.size()));
        deck.remove(randomCard);
        return randomCard;
    }

    public void addCardsToDeck(Card playingCard, Card defendingCard) {
        deck.addAll(Arrays.asList(playingCard, defendingCard));
    }

    public boolean hasBattleCards() {
        return !deck.isEmpty();
    }
}
