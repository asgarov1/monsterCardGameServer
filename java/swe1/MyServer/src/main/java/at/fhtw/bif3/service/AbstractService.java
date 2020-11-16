package at.fhtw.bif3.service;

import at.fhtw.bif3.dao.AbstractDAO;
import lombok.SneakyThrows;

public abstract class AbstractService<P, K> {

    private final AbstractDAO<P, K> abstractDAO;

    public AbstractService(AbstractDAO<P, K> abstractDAO) {
        this.abstractDAO = abstractDAO;
    }

    @SneakyThrows
    public void save(P object) {
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
}
