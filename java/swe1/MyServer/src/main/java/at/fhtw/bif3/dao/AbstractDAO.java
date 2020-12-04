package at.fhtw.bif3.dao;

import at.fhtw.bif3.dao.connection.ConnectionFactory;
import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.dao.exception.EntityNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO<T, K> implements GenericDAO<T, K> {

    protected abstract String getCreateQuery();

    protected abstract String getUpdateQuery();

    protected abstract String getTableName();

    protected abstract void setObjectStatement(PreparedStatement preparedStatement, T object) throws DAOException;

    protected abstract T readObject(ResultSet resultSet) throws DAOException;

    protected String getCountRowsQuery() {
        return "SELECT count(*) FROM " + getTableName();
    }

    protected String getSelectByIdQuery() {
        return "SELECT * FROM " + getTableName() + " WHERE id = ?;";
    }

    protected String getDeleteQuery() {
        return "DELETE FROM " + getTableName() + " WHERE id = ?";
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
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                object = readObject(resultSet);
            } else {
                throw new EntityNotFoundException("Couldn't find an object with such ID!");
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
        return object;
    }

    public T findByField(String fieldName, String fieldValue) throws DAOException {
        T object;
        String query = "SELECT * FROM " + getTableName() + " WHERE " + fieldName + " = ?;";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, fieldValue);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                object = readObject(resultSet);
            } else {
                throw new EntityNotFoundException("Couldn't find an object with such " + fieldName + " = " + fieldValue + "!");
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
            statement.setObject(1, id);
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

    public T findRandom() {
        T object;
        try (Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM " + getTableName() + " ORDER BY RANDOM() LIMIT 1;";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                object = readObject(resultSet);
            } else {
                throw new EntityNotFoundException("Couldn't find a single result in table " + getTableName() + "!");
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
        return object;
    }

    public List<T> findAll() {
        List<T> objects = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM " + getTableName() + ";";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                objects.add(readObject(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
        return objects;
    }
}

