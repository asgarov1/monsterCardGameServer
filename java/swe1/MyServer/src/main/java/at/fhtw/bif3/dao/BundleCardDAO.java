package at.fhtw.bif3.dao;

import at.fhtw.bif3.dao.connection.ConnectionFactory;
import at.fhtw.bif3.dao.daoentity.BundleCard;
import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.domain.card.Card;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BundleCardDAO extends AbstractDAO<BundleCard, String> {

    @Getter
    private final String tableName = "bundle_card";

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO " + getTableName() + " (bundle_id, card_id) VALUES (?,?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + getTableName() + " SET bundle_id = ?, card_id = ? WHERE bundle_id = ? AND card_id = ?;";
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
            bundleCard.setCardId(resultSet.getString("card_id"));
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return bundleCard;
    }

    protected List<Card> findAllByBundleId(String id) throws DAOException {
        String query = "select * from " + getTableName() + " where bundle_id  = ?;";
        return findAllByQuery(id, query);
    }

    protected List<Card> findAllByQuery(String id, String query) throws DAOException {
        List<Card> cards = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            CardDAO cardDAO = new CardDAO();
            while (resultSet.next()) {
                var cardId = resultSet.getString("card_id");
                cards.add(cardDAO.read(cardId));
            }
        } catch (SQLException | DAOException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return cards;
    }

    public void deleteByBundleId(String bundleId) throws DAOException {
        String query = "delete from " + getTableName() + " where bundle_id  = ?;";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, bundleId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }
}
