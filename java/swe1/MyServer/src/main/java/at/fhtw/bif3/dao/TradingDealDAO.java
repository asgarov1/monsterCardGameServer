package at.fhtw.bif3.dao;

import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.domain.card.CardType;
import at.fhtw.bif3.domain.TradingDeal;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TradingDealDAO extends AbstractDAO<TradingDeal, String> {

    @Getter
    private final String tableName = "trading_deal";

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO " + getTableName() + "(card_id, card_type, minimum_damage, creator_id id) VALUES (?, ?, ?, ?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + getTableName() + " SET card_id = ?, card_type = ?, minimum_damage = ?, creator_id = ? WHERE id = ?;";
    }

    @Override
    protected void setObjectStatement(PreparedStatement statement, TradingDeal tradingDeal) throws DAOException {
        try {
            statement.setString(1, tradingDeal.getCardToTrade().getId());
            statement.setString(2, tradingDeal.getCardtype().name());
            statement.setLong(3, tradingDeal.getMinimumDamage());
            statement.setString(4, tradingDeal.getCreator().getUsername());
            statement.setString(5, tradingDeal.getId());
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    protected TradingDeal readObject(ResultSet resultSet) throws DAOException {
        TradingDeal tradingDeal = new TradingDeal();
        try {
            tradingDeal.setId(resultSet.getString("id"));
            tradingDeal.setCardToTrade(new CardDAO().read(resultSet.getString("card_id")));
            tradingDeal.setCardtype(CardType.assignByName(resultSet.getString("card_type")));
            tradingDeal.setMinimumDamage(resultSet.getInt("minimum_damage"));
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return tradingDeal;
    }
}

