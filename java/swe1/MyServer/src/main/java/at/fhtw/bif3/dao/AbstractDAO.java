package at.fhtw.bif3.dao;

import at.fhtw.bif3.dao.connection.ConnectionFactory;
import at.fhtw.bif3.dao.exception.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDAO<T, K> implements GenericDAO<T, K> {

    protected abstract String getCreateQuery();

    protected abstract String getUpdateQuery();

    protected abstract String getTableName();

    private void setIdStatement(PreparedStatement statement, K id) throws DAOException {
        try {
            statement.setObject(1, id);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    protected abstract void setObjectStatement(PreparedStatement preparedStatement, T object) throws DAOException;

    protected abstract T readObject(ResultSet resultSet) throws DAOException;

    protected String getCountRowsQuery() {
        return "SELECT count(*) FROM " + getTableName();
    }


    @Override
    public boolean create(T object) throws DAOException {
        String createQuery = getCreateQuery();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(createQuery)) {

            setObjectStatement(statement, object);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return true;
    }

    @Override
    public T read(K id) throws DAOException {
        T object;

        String selectByIdQuery = getSelectByIdQuery();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectByIdQuery)) {

            setIdStatement(statement, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                object = readObject(resultSet);
            } else {
                throw new DAOException("Couldn't find an object with such ID!");
            }

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
        return object;
    }

    @Override
    public void update(T object) throws DAOException {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(getUpdateQuery())) {

            setObjectStatement(statement, object);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(K id) throws DAOException {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(getDeleteQuery())) {

            setIdStatement(statement, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    public int countEntities() throws DAOException {
        String countRowsQuery = getCountRowsQuery();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(countRowsQuery)) {

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new DAOException("Problem counting entities!");
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    protected String getSelectByIdQuery() {
        return "SELECT * FROM " + getTableName() + " WHERE id = ?;";
    }

    protected String getDeleteQuery() {
        return "DELETE FROM " + getTableName() + " WHERE id = ?";
    }
}

