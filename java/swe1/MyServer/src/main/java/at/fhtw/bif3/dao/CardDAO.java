package at.fhtw.bif3.dao;

import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.domain.Card;
import at.fhtw.bif3.domain.CardType;
import at.fhtw.bif3.domain.ElementType;
import at.fhtw.bif3.domain.Player;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static java.util.Arrays.stream;

public class CardDAO extends AbstractDAO<Card, String> {

    @Getter
    private final String tableName = "card";

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO " + getTableName() + " (id, card_type, damage, element_type, name) VALUES (?,?,?,?,?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + getTableName() + " SET username = ?, password = ?, number_of_coins = ? WHERE id = ?;";
    }

    @Override
    protected void setObjectStatement(PreparedStatement statement, Card card) throws DAOException {
        try {
            statement.setString(1, card.getId());
            statement.setString(2, card.getName());
            statement.setInt(3, card.getDamage());
            statement.setString(4, card.getElementType().name());
            statement.setString(5, card.getCardType().name());
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    protected Card readObject(ResultSet resultSet) throws DAOException {
        Card card = new Card();
        try {
            card.setId(resultSet.getString("id"));
            card.setName(resultSet.getString("name"));
            card.setDamage(resultSet.getInt("damage"));

            String element_type = resultSet.getString("element_type");
            card.setElementType(stream(ElementType.values()).filter(elementType -> elementType.name().equals(element_type)).findFirst().orElseThrow());

            String card_type = resultSet.getString("card_type");
            card.setElementType(stream(ElementType.values()).filter(cardType -> cardType.name().equals(card_type)).findFirst().orElseThrow());

        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return card;
    }

    @Override
    public void delete(String cardId) throws DAOException {
        super.delete(cardId);
    }
}
