package at.fhtw.bif3.dao;

import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.domain.card.Card;
import at.fhtw.bif3.domain.card.CardType;
import at.fhtw.bif3.domain.card.ElementType;
import at.fhtw.bif3.domain.card.SpellCard;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static at.fhtw.bif3.domain.card.CardType.SPELL;
import static java.util.Arrays.stream;

public class CardDAO extends AbstractDAO<Card, String> {

    @Getter
    private final String tableName = "card";

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO " + getTableName() + " (name, damage, weakness, element_type, card_type, id) VALUES (?,?,?,?,?,?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + getTableName() + " SET name = ?, damage = ?, weakness = ?, element_type = ?, card_type = ? WHERE id = ?;";
    }

    @Override
    protected void setObjectStatement(PreparedStatement statement, Card card) throws DAOException {
        try {
            statement.setString(1, card.getName());
            statement.setDouble(2, card.getDamage());
            statement.setDouble(3, card instanceof SpellCard ? ((SpellCard) card).getWeakness() : 0);
            statement.setString(4, card.getElementType().name());
            statement.setString(5, card.getCardType().name());
            statement.setString(6, card.getId());
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
            card.setDamage(resultSet.getDouble("damage"));
            if(card instanceof SpellCard) {
                ((SpellCard) card).setWeakness(resultSet.getDouble("weakness"));
            }
            card.setElementType(ElementType.assignByName(resultSet.getString("element_type")));

        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return card;
    }
}
