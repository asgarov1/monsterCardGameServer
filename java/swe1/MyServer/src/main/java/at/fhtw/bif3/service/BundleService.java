package at.fhtw.bif3.service;

import at.fhtw.bif3.dao.BundleCardDAO;
import at.fhtw.bif3.dao.BundleDAO;
import at.fhtw.bif3.dao.daoentity.BundleCard;
import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.domain.Bundle;
import lombok.SneakyThrows;

public class BundleService extends AbstractService<Bundle, String> {
    private final BundleCardDAO bundleCardDAO = new BundleCardDAO();

    public BundleService() {
        super(new BundleDAO());
    }

    @Override
    public void create(Bundle bundle) {
        super.create(bundle);
        bundle.getCards()
                .stream()
                .map(card -> new BundleCard(bundle.getId(), card.getId()))
                .forEach(object -> {
                    try {
                        bundleCardDAO.create(object);
                    } catch (DAOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @SneakyThrows
    @Override
    public void update(Bundle bundle) {
        bundleCardDAO.deleteByBundleId(bundle.getId());
        bundle.getCards()
                .stream()
                .map(card -> new BundleCard(bundle.getId(), card.getId()))
                .forEach(object -> {
                    try {
                        bundleCardDAO.create(object);
                    } catch (DAOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
