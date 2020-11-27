package at.fhtw.bif3.service;

import at.fhtw.bif3.domain.Card;
import at.fhtw.bif3.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

import static at.fhtw.bif3.domain.CardType.MONSTER;
import static at.fhtw.bif3.domain.ElementType.FIRE;
import static at.fhtw.bif3.domain.ElementType.WATER;
import static at.fhtw.bif3.util.PropertiesReader.getProperties;
import static java.lang.Integer.parseInt;

@Slf4j
public class BattleService {
    private static final int MAX_NUMBER_OF_ROUNDS = parseInt(getProperties().getProperty("battle.rounds-max"));

    public void performBattle(User player1, User player2) {
        player1.generateBattleDeck();
        player2.generateBattleDeck();

        int i = 0;
        while (player1.hasBattleCards() && player2.hasBattleCards() && i++ < MAX_NUMBER_OF_ROUNDS) {
            log.info("\n===BEGINNING ROUND " + i + "===");
            User startingPlayer = new Random().nextBoolean() ? player1 : player2;
            User secondPlayer = startingPlayer == player1 ? player2 : player1;

            Card playingCard = startingPlayer.playRandomCard();
            Card defendingCard = secondPlayer.playRandomCard();
            log.info(startingPlayer.getUsername() + " plays card " + playingCard.getName());
            log.info(secondPlayer.getUsername() + " defends with " + defendingCard.getName());

            boolean startingPlayerWon = calculateWinner(playingCard, defendingCard);

            User winner = startingPlayerWon ? startingPlayer : secondPlayer;
            User loser = startingPlayerWon ? secondPlayer : startingPlayer;
            log.info("And the winner iiiiiiiiis... " + winner.getUsername());

            adjustPointsForPlayers(winner, loser);
            winner.addCardsToDeck(playingCard, defendingCard);
            log.info(winner.getUsername() + " picked up the cards " + playingCard.getName() + " and " + defendingCard.getName());
            log.info("\n===END OF ROUND " + i + "===\n");
        }

        User winner = player1.hasBattleCards() ? player1 : player2;
        log.info("\nBATTLE HAS FINISHED! " + winner.getUsername() + " has won!!!\n");

        player1.returnCardsFromDeck();
        player2.returnCardsFromDeck();
    }

    private boolean calculateWinner(Card firstPlayerCard, Card secondPlayerCard) {
        double attacker = calculateDamage(firstPlayerCard, secondPlayerCard);
        double defender = calculateDamage(secondPlayerCard, firstPlayerCard);
        log.info("Attacker's damage: " + attacker);
        log.info("Defender's damage: " + defender);
        return attacker > defender;
    }

    private void adjustPointsForPlayers(User winner, User loser) {
        int pointsForWin = parseInt(getProperties().getProperty("elo.points.win"));
        int pointsForLoss = parseInt(getProperties().getProperty("elo.points.loss"));

        winner.setElo(winner.getElo() + pointsForWin);
        loser.setElo(winner.getElo() - pointsForLoss);

        log.info(winner.getUsername() + " gets " + pointsForWin + " points!");
        log.info(loser.getUsername() + " lost " + pointsForLoss + " points!");
    }

    //TODO question:
    // monster cards
    //  cards with active attacks and damage based on an element type (fire, water,
    //  normal). The element type does not effect pure monster fights.

    private double calculateDamage(Card card1, Card card2) {
        double damage = card1.getDamage();

        if (!card1.getCardType().equals(MONSTER) && card2.getCardType().equals(MONSTER)) {
            if (card1.getElementType().equals(WATER) && card2.getElementType().equals(FIRE)) {
                damage *= 2;
            } else if (card1.getElementType().equals(FIRE) && card2.getElementType().equals(WATER)) {
                damage = 0;
            }
        }

        return damage;
//        TODO:
//            Effectiveness:
//          water -> fire
//          fire -> normal
//          normal -> water
    }
}
