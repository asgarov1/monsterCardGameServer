package at.fhtw.bif3.service;

import at.fhtw.bif3.dao.BundleDAO;
import at.fhtw.bif3.domain.Bundle;

public class BundleService extends AbstractService<Bundle, String> {
    public BundleService() {
        super(new BundleDAO());
    }
}
