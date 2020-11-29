package at.fhtw.bif3.domain;

import at.fhtw.bif3.domain.card.Card;
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

    public static final int DECK_SIZE = parseInt(getProperties().getProperty("deck.battle.size"));

    @SerializedName("Id")
    private String id;

    @SerializedName("Username")
    private String username;

    @SerializedName("Password")
    private String password;

    @SerializedName("Name")
    private String name;

    @SerializedName("Bio")
    private String bio;

    @SerializedName("Image")
    private String image;

    private int gamesPlayed;  //TODO question: is this number of rounds? Or battle events?
    private int elo = parseInt(getProperties().getProperty("elo.start-value")); //TODO question: can elo go beyond zero? should elo be updated after each round? or after each battle?
    private int numberOfCoins = parseInt(getProperties().getProperty("user.start-money"));
    private List<Card> cards = new ArrayList<>();
    private List<Card> deck = new ArrayList<>();
    private List<Card> lockedForTrade = new ArrayList<>();

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

    public void addCardToDeck(Card card) {
        deck.add(card);
    }

    public Card playRandomCard() {
        Card randomCard = getDeck().get(randomInt(0, deck.size()));
        deck.remove(randomCard);
        return randomCard;
    }

    public void addCardsToDeck(Card playingCard, Card defendingCard) {
        deck.addAll(Arrays.asList(playingCard, defendingCard));
    }

    public void generateBattleDeck() {
        for (int i = 0; i < DECK_SIZE; i++) {
            Card randomCard = cards.get(randomInt(0, cards.size()));
            cards.remove(randomCard);
            deck.add(randomCard);
        }
    }

    public List<Card> getDeck() {
        return deck;
    }

    public boolean hasBattleCards() {
        return !deck.isEmpty();
    }

    public void incrementGamesPlayed() {
        gamesPlayed++;
    }

    public void incrementElo(int pointsForWin) {
        elo += pointsForWin;
    }

    public void decrementElo(int pointsForLoss) {
        elo -= pointsForLoss;
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

    public void configureDeck(List<String> cardIds) {
        returnCardsFromDeck();

        cards.stream()
                .filter(card -> cardIds.contains(card.getId()))
                .forEach(card -> {
                    deck.add(card);
                });

        for (Card card : deck) {
            cards.remove(card);
        }
    }
}
