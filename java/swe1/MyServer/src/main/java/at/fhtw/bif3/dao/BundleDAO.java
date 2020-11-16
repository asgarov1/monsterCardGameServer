package at.fhtw.bif3.dao;

import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.domain.Bundle;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BundleDAO extends AbstractDAO<Bundle, String> {

    @Getter
    private final String tableName = "bundle";

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO " + getTableName() + "(id) VALUES (?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + getTableName() + " SET id = ? WHERE id = ?;";
    }

    @Override
    protected void setObjectStatement(PreparedStatement statement, Bundle bundle) throws DAOException {
        try {
            statement.setString(1, bundle.getId());
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    protected Bundle readObject(ResultSet resultSet) throws DAOException {
        Bundle bundle = new Bundle();
        try {
            bundle.setId(resultSet.getString("id"));
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }

        // TODO read all cards
        return bundle;
    }

    @Override
    public void delete(String bundleId) throws DAOException {
        // TODO delete from PLAYER_CARD
        super.delete(bundleId);
    }

}

