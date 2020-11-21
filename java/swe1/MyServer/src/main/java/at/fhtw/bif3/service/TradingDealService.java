package at.fhtw.bif3.service;

import at.fhtw.bif3.dao.*;
import at.fhtw.bif3.dao.daoentity.PlayerCard;
import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.domain.Bundle;
import at.fhtw.bif3.domain.Card;
import at.fhtw.bif3.domain.TradingDeal;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.service.exception.TooPoorException;
import at.fhtw.bif3.service.exception.TransactionException;
import lombok.SneakyThrows;

public class TradingDealService extends AbstractService<TradingDeal, String> {
    public TradingDealService() {
        super(new TradingDealDAO());
    }
}
