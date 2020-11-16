package at.fhtw.bif3.service;

import at.fhtw.bif3.dao.CardDAO;
import at.fhtw.bif3.domain.Card;
import lombok.SneakyThrows;

public class CardService extends AbstractService<Card, String> {
    public CardService() {
        super(new CardDAO());
    }
}
