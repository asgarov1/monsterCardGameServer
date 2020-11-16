package at.fhtw.bif3.dao;

import at.fhtw.bif3.dao.domain.BundleCard;
import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.domain.Card;
import at.fhtw.bif3.domain.ElementType;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Arrays.stream;

public class BundleCardDAO extends AbstractDAO<BundleCard, String> {

    @Getter
    private final String tableName = "bundle_cards";

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO " + getTableName() + " (bundle_id, cards_id) VALUES (?,?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + getTableName() + " SET bundle_id = ?, cards_id = ? WHERE bundle_id = ? AND cards_id = ?;";
    }

    @Override
    protected void setObjectStatement(PreparedStatement statement, BundleCard bundleCard) throws DAOException {
        try {
            statement.setString(1, bundleCard.getBundleId());
            statement.setString(2, bundleCard.getCardId());
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    protected BundleCard readObject(ResultSet resultSet) throws DAOException {
        BundleCard bundleCard = new BundleCard();
        try {
            bundleCard.setBundleId(resultSet.getString("bundle_id"));
            bundleCard.setCardId(resultSet.getString("cards_id"));
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return bundleCard;
    }

}
