package at.fhtw.bif3.dao;

import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.domain.User;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends AbstractDAO<User, String> {

    @Getter
    private final String tableName = "player";

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO " + getTableName() + " (username, password, number_of_coins, name, bio, image, games_played, elo, id)" +
                " VALUES (?,?,?,?,?,?,?,?,?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + getTableName() + " SET username = ?, password = ?, number_of_coins = ?, name = ?, bio = ?, " +
                "image = ?, games_played = ?, elo = ? WHERE id = ?;";
    }

    @Override
    protected void setObjectStatement(PreparedStatement statement, User player) throws DAOException {
        try {
            statement.setString(1, player.getUsername());
            statement.setString(2, player.getPassword());
            statement.setInt(3, player.getNumberOfCoins());
            statement.setString(4, player.getName());
            statement.setString(5, player.getBio());
            statement.setString(6, player.getImage());
            statement.setInt(7, player.getGamesPlayed());
            statement.setInt(8, player.getElo());
            statement.setString(9, player.getId());
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    protected User readObject(ResultSet resultSet) throws DAOException {
        User player = new User();
        try {
            player.setId(resultSet.getString("id"));
            player.setUsername(resultSet.getString("username"));
            player.setPassword(resultSet.getString("password"));
            player.setNumberOfCoins(resultSet.getInt("number_of_coins"));
            player.setName(resultSet.getString("name"));
            player.setBio(resultSet.getString("bio"));
            player.setImage(resultSet.getString("image"));
            player.setGamesPlayed(resultSet.getInt("games_played"));
            player.setElo(resultSet.getInt("elo"));
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return player;
    }

    @Override
    public void delete(String studentId) throws DAOException {
        super.delete(studentId);
    }

}

