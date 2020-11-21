package at.fhtw.bif3.dao;

import at.fhtw.bif3.dao.exception.DAOException;
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
        return "INSERT INTO " + getTableName() + " (id, card_id) VALUES (?, ?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + getTableName() + " SET card_id = ? WHERE id = ?;";
    }

    @Override
    protected void setObjectStatement(PreparedStatement statement, TradingDeal tradingDeal) throws DAOException {
        try {
            statement.setLong(1, tradingDeal.getId());
            statement.setString(1, tradingDeal.getCardToTrade().getId());
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    protected TradingDeal readObject(ResultSet resultSet) throws DAOException {
        TradingDeal tradingDeal = new TradingDeal();
        try {
            tradingDeal.setId(resultSet.getLong("id"));
            tradingDeal.setCardToTrade(new CardDAO().read(resultSet.getString("card_id")));
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return tradingDeal;
    }
}

