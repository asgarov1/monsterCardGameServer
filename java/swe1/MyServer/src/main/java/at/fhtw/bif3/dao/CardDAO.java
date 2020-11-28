package at.fhtw.bif3.dao;

import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.domain.card.Card;
import at.fhtw.bif3.domain.card.CardType;
import at.fhtw.bif3.domain.card.ElementType;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Arrays.stream;

public class CardDAO extends AbstractDAO<Card, String> {

    @Getter
    private final String tableName = "card";

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO " + getTableName() + " (name, damage, element_type, card_type, id) VALUES (?,?,?,?,?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + getTableName() + " SET name = ?, damage = ?, element_type = ?, card_type = ? WHERE id = ?;";
    }

    @Override
    protected void setObjectStatement(PreparedStatement statement, Card card) throws DAOException {
        try {
            statement.setString(1, card.getName());
            statement.setDouble(2, card.getDamage());
            statement.setString(3, card.getElementType().name());
            statement.setString(4, card.getCardType().name());
            statement.setString(5, card.getId());
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    protected Card readObject(ResultSet resultSet) throws DAOException {
        Card card;
        try {
            CardType cardType = CardType.assignByName(resultSet.getString("card_type"));
            card = cardType.instantiateByType();

            card.setId(resultSet.getString("id"));
            card.setName(resultSet.getString("name"));
            card.setDamage(resultSet.getInt("damage"));
            card.setElementType(ElementType.assignByName(resultSet.getString("element_type")));

        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return card;
    }
}
