package at.fhtw.bif3.domain;

import java.util.List;

public class User {
    private Credentials credentials;
    private List<Card> cards;
    private List<Card> battleDeck;
    private int numberOfCoins = 20;
}
