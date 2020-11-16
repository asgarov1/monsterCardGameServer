package at.fhtw.bif3.service;

import at.fhtw.bif3.dao.PlayerCardDAO;
import at.fhtw.bif3.dao.PlayerDAO;
import at.fhtw.bif3.dao.domain.PlayerCard;
import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.domain.Player;

public class PlayerService extends AbstractService<Player, String> {
    private final PlayerCardDAO playerCardDAO = new PlayerCardDAO();

    public PlayerService() {
        super(new PlayerDAO());
    }

    @Override
    public void create(Player player) {
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
}
