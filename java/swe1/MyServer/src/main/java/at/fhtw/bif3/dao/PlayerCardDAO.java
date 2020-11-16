package at.fhtw.bif3.dao;

import at.fhtw.bif3.dao.domain.BundleCard;
import at.fhtw.bif3.dao.domain.PlayerCard;
import at.fhtw.bif3.dao.exception.DAOException;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Arrays.stream;

public class PlayerCardDAO extends AbstractDAO<PlayerCard, String> {

    @Getter
    private final String tableName = "player_cards";

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO " + getTableName() + " (player_id, cards_id) VALUES (?,?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + getTableName() + " SET player_id = ?, cards_id = ? WHERE player_id = ? AND cards_id = ?;";
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
            playerCard.setCardId(resultSet.getString("cards_id"));
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return playerCard;
    }

}
