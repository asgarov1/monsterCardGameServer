package at.fhtw.bif3.service;

import at.fhtw.bif3.dao.UserCardDAO;
import at.fhtw.bif3.dao.UserDAO;
import at.fhtw.bif3.dao.daoentity.PlayerCard;
import at.fhtw.bif3.domain.Bundle;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.domain.card.Card;
import at.fhtw.bif3.service.exception.TooPoorException;
import at.fhtw.bif3.service.exception.TransactionException;
import lombok.SneakyThrows;

import static at.fhtw.bif3.util.PropertiesReader.getProperties;
import static java.lang.Integer.parseInt;

public class UserService extends AbstractService<User, String> {
    public static final int CARD_PACKAGE_PRICE = parseInt(getProperties().getProperty("package.price"));
    public static final String PLAYER_CARD_TABLE = "player_card";
    public static final String PLAYER_DECK_CARD_TABLE = "player_deck_card";
    public static final String PLAYER_LOCKED_CARD_TABLE = "player_locked_card";

    private final UserCardDAO userCardDAO = new UserCardDAO(PLAYER_CARD_TABLE);
    private final UserCardDAO userDeckCardDAO = new UserCardDAO(PLAYER_DECK_CARD_TABLE);
    private final UserCardDAO userLockedCardDAO = new UserCardDAO(PLAYER_LOCKED_CARD_TABLE);
    private final CardService cardService = new CardService();


    public UserService() {
        super(new UserDAO());
    }

    public void save(User player) {
        if (!exists(player.getId())) {
            create(player);
        } else {
            update(player);
        }
    }

    @Override
    public void create(User user) {
        super.create(user);
        persistCardsForUser(user);
    }

    @Override
    public void update(User user) {
        super.update(user);
        userCardDAO.deleteByPlayerId(user.getId());
        userDeckCardDAO.deleteByPlayerId(user.getId());
        userLockedCardDAO.deleteByPlayerId(user.getId());
        persistCardsForUser(user);
    }

    private void persistCardsForUser(User user) {
        user.getCards().forEach(card -> userCardDAO.create(new PlayerCard(user.getId(), card.getId())));
        user.getDeck().forEach(card -> userDeckCardDAO.create(new PlayerCard(user.getId(), card.getId())));
        user.getDeck().forEach(card -> userLockedCardDAO.create(new PlayerCard(user.getId(), card.getId())));
    }

    public void delete(User user) {
        user.getCards().forEach(cardService::delete);
        user.getDeck().forEach(cardService::delete);
        user.getLockedForTrade().forEach(cardService::delete);
        super.delete(user.getId());
    }

    @SneakyThrows
    @Override
    public User findById(String id) {
        var user = super.findById(id);
        readCardsForUser(user);
        return user;
    }

    public User findByUsername(String username) {
        var user = byUsername(username);
        readCardsForUser(user);
        return user;
    }

    public synchronized void processPackagePurchaseFor(String username) {
        var user = byUsername(username);
        if (user.getNumberOfCoins() < CARD_PACKAGE_PRICE) {
            throw new TooPoorException("User with username " + username + " doesn't have enough coins for this operation");
        }

        int amountOfMoneyBeforeTransaction = user.getNumberOfCoins();
        try {
            user.setNumberOfCoins(user.getNumberOfCoins() - CARD_PACKAGE_PRICE);
            var bundleService = new BundleService();
            Bundle bundle = bundleService.findRandom();
            bundle.getCards().forEach(user::addCard);
            update(user);
            bundleService.delete(bundle.getId());
        } catch (Exception e) {
            user.setNumberOfCoins(amountOfMoneyBeforeTransaction);
            throw new TransactionException("Something went wrong during purchase! Transaction rolled back.");
        }
    }

    private User byUsername(String username) {
        return findByField("username", username);
    }

    private void readCardsForUser(User user) {
        userCardDAO.findAllByUserId(user.getId())
                .stream()
                .map(playerCard -> cardService.findById(playerCard.getCardId()))
                .forEach(user::addCard);

        userDeckCardDAO.findAllByUserId(user.getId())
                .stream()
                .map(playerCard -> cardService.findById(playerCard.getCardId()))
                .forEach(user::addCardToDeck);
    }

    public void transferCard(User giver, User receiver, Card card) {
        giver.removeCard(card);
        update(giver);

        receiver.addCard(card);
        update(receiver);
    }
}
