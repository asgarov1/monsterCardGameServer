package at.fhtw.bif3.service;

import at.fhtw.bif3.dao.*;
import at.fhtw.bif3.domain.TradingDeal;

public class TradingDealService extends AbstractService<TradingDeal, String> {
    public TradingDealService() {
        super(new TradingDealDAO());
    }
}
