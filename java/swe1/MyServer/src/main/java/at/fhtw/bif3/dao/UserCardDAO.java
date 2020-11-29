package at.fhtw.bif3.dao;

import at.fhtw.bif3.dao.connection.ConnectionFactory;
import at.fhtw.bif3.dao.daoentity.PlayerCard;
import at.fhtw.bif3.dao.exception.DAOException;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;

public class UserCardDAO extends AbstractDAO<PlayerCard, String> {

    @Getter
    private final String tableName;

    public UserCardDAO(String tableName) {
        super();
        this.tableName = tableName;
    }

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO " + getTableName() + " (player_id, card_id) VALUES (?,?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + getTableName() + " SET player_id = ?, card_id = ? WHERE player_id = ? AND card_id = ?;";
    }

    @Override
    protected void setObjectStatement(PreparedStatement statement, PlayerCard playerCard) throws DAOException {
        try {
            statement.setString(1, playerCard.getPlayerId());
            statement.setString(2, playerCard.getCardId());
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    protected PlayerCard readObject(ResultSet resultSet) throws DAOException {
        PlayerCard playerCard = new PlayerCard();
        try {
            playerCard.setPlayerId(resultSet.getString("player_id"));
            playerCard.setCardId(resultSet.getString("card_id"));
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return playerCard;
    }

    public List<PlayerCard> findAllByUserId(String id) throws DAOException{
        String query = "select * from " + getTableName() + " where player_id  = ?;";

        List<PlayerCard> palyerCards = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                var cardId = resultSet.getString("card_id");
                palyerCards.add(new PlayerCard(id, cardId));
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return palyerCards;
    }

    public void deleteByPlayerId(String id) {
        String query = "delete from " + getTableName() + " where player_id  = ?;";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }
}
