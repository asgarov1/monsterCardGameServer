package at.fhtw.bif3.service;

import at.fhtw.bif3.dao.UserCardDAO;
import at.fhtw.bif3.dao.UserDAO;
import at.fhtw.bif3.dao.daoentity.PlayerCard;
import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.domain.Bundle;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.service.exception.TooPoorException;
import at.fhtw.bif3.service.exception.TransactionException;
import lombok.SneakyThrows;

import static at.fhtw.bif3.util.PropertiesReader.getProperties;
import static java.lang.Integer.parseInt;

public class UserService extends AbstractService<User, String> {
    public static final int CARD_PACKAGE_PRICE = parseInt(getProperties().getProperty("package.price"));
    private final UserCardDAO userCardDAO = new UserCardDAO();
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
    public void create(User player) {
        super.create(player);
        player.getCards().forEach(
                card -> {
                    try {
                        userCardDAO.create(new PlayerCard(player.getId(), card.getId()));
                    } catch (DAOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    @Override
    public void update(User user) {
        super.update(user);

        user.getCards()
                .stream()
                .filter(card -> userCardDAO.findAllByPlayerId(user.getId())
                        .stream()
                        .map(PlayerCard::getCardId)
                        .noneMatch(id -> id.equals(card.getId())))
                .forEach(card -> userCardDAO.create(new PlayerCard(user.getId(), card.getId())));
    }

    @SneakyThrows
    @Override
    public User findById(String id) {
        var user = super.findById(id);
        addCardsForUser(user);

        return user;
    }

    public User findByUsername(String username) {
        var user = byUsername(username);
        addCardsForUser(user);

        return user;
    }

    public synchronized boolean processPackagePurchaseFor(String username) {
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
        return true;
    }

    private User byUsername(String username) {
        return findByField("username", username);
    }

    private void addCardsForUser(User user) {
        userCardDAO.findAllByPlayerId(user.getId())
                .stream()
                .map(playerCard -> cardService.findById(playerCard.getCardId()))
                .forEach(user::addCard);
    }
}
