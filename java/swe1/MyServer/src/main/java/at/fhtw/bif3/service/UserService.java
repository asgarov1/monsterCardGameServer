package at.fhtw.bif3.service;

import at.fhtw.bif3.dao.PlayerCardDAO;
import at.fhtw.bif3.dao.PlayerDAO;
import at.fhtw.bif3.dao.daoentity.PlayerCard;
import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.domain.Bundle;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.service.exception.TooPoorException;
import at.fhtw.bif3.service.exception.TransactionException;
import lombok.SneakyThrows;

public class UserService extends AbstractService<User, String> {
    public static final int CARD_PACKAGE_PRICE = 5;
    private final PlayerDAO playerDAO = new PlayerDAO();
    private final PlayerCardDAO playerCardDAO = new PlayerCardDAO();
    private final CardService cardService = new CardService();

    public UserService() {
        super(new PlayerDAO());
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
                        playerCardDAO.create(new PlayerCard(player.getId(), card.getId()));
                    } catch (DAOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    @SneakyThrows
    @Override
    public User findById(String id) {
        var user = super.findById(id);
        playerCardDAO.findAllByPlayerId(id)
                .stream()
                .map(playerCard -> cardService.findById(playerCard.getCardId()))
                .forEach(user::addCard);

        return user;
    }

    public User findByUsername(String username) {
        var user = byUsername(username);
        playerCardDAO.findAllByPlayerId(user.getId())
                .stream()
                .map(playerCard -> cardService.findById(playerCard.getCardId()))
                .forEach(user::addCard);

        return user;
    }

    public synchronized boolean processPackagePurchaseFor(String username) {
        var user = byUsername(username);
        if (user.getNumberOfCoins() < CARD_PACKAGE_PRICE) {
            throw new TooPoorException("User with username " + username + " doesn't have enough coins for this operation");
        }

        //start transaction
        int amountOfMoneyBeforeTransaction = user.getNumberOfCoins();
        try {
            user.setNumberOfCoins(user.getNumberOfCoins() - CARD_PACKAGE_PRICE);
            var bundleService = new BundleService();
            Bundle bundle = bundleService.findRandom();
            bundle.getCards().forEach(user::addCard);
            bundleService.delete(bundle.getId());
        } catch (Exception e) {
            user.setNumberOfCoins(amountOfMoneyBeforeTransaction);
            throw new TransactionException("Something went wrong during purchase!");
        }
        return true;
    }

    private User byUsername(String username) {
        return findByField("username", username);
    }
}
