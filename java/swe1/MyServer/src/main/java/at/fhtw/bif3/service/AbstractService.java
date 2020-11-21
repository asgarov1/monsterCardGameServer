package at.fhtw.bif3.service;

import at.fhtw.bif3.dao.AbstractDAO;
import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.dao.exception.EntityNotFoundException;
import lombok.SneakyThrows;

import java.util.List;

public abstract class AbstractService<P, K> {

    private final AbstractDAO<P, K> abstractDAO;

    protected AbstractService(AbstractDAO<P, K> abstractDAO) {
        this.abstractDAO = abstractDAO;
    }

    @SneakyThrows
    public void create(P object) {
        abstractDAO.create(object);
    }

    @SneakyThrows
    public void update(P object) {
        abstractDAO.update(object);
    }

    @SneakyThrows
    public void delete(K id) {
        abstractDAO.delete(id);
    }

    @SneakyThrows
    public P findById(K id) {
        return abstractDAO.read(id);
    }

    @SneakyThrows
    public int countEntities(){
        return abstractDAO.countEntities();
    }

    public boolean exists(K id) {
        try {
            abstractDAO.read(id);
        } catch (EntityNotFoundException e) {
            return false;
        }
        return true;
    }

    public P findByField(String fieldName, String fieldValue){
        return abstractDAO.findByField(fieldName, fieldValue);
    }

    public P findRandom(){
        return abstractDAO.findRandom();
    }

    public List<P> findAll() {
        return abstractDAO.findAll();
    }
}
