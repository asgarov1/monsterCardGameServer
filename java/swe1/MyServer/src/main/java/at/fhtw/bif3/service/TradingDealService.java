package at.fhtw.bif3.service;

import at.fhtw.bif3.dao.TradingDealDAO;
import at.fhtw.bif3.domain.TradingDeal;

public class TradingDealService extends AbstractService<TradingDeal, String> {
    UserService userService = new UserService();

    public TradingDealService() {
        super(new TradingDealDAO());
    }

    @Override
    public TradingDeal findById(String id) {
        TradingDeal tradingDeal = super.findById(id);
        tradingDeal.setCreator(userService.findByUsername(tradingDeal.getCreator().getUsername()));
        return tradingDeal;
    }
}
