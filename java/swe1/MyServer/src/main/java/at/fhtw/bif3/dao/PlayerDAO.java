package at.fhtw.bif3.dao;

import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.domain.Player;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDAO extends AbstractDAO<Player, String> {

    @Getter
    private final String tableName = "player";

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO " + getTableName() + " (username, password, number_of_coins, id) VALUES (?,?,?,?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + getTableName() + " SET username = ?, password = ?, number_of_coins = ? WHERE id = ?;";
    }

    @Override
    protected void setObjectStatement(PreparedStatement statement, Player player) throws DAOException {
        try {
            statement.setString(1, player.getUsername());
            statement.setString(2, player.getPassword());
            statement.setInt(3, player.getNumberOfCoins());
            statement.setString(4, player.getId());
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    protected Player readObject(ResultSet resultSet) throws DAOException {
        Player player = new Player();
        try {
            player.setId(resultSet.getString("id"));
            player.setUsername(resultSet.getString("username"));
            player.setPassword(resultSet.getString("password"));
            player.setNumberOfCoins(resultSet.getInt("number_of_coins"));

            //TODO read cards
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return player;
    }

    @Override
    public void delete(String studentId) throws DAOException {
        // TODO delete from PLAYER_CARD
        super.delete(studentId);
    }

}

