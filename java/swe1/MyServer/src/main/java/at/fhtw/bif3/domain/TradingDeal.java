package at.fhtw.bif3.domain;

import at.fhtw.bif3.controller.dto.TradingDTO;
import at.fhtw.bif3.domain.card.Card;
import at.fhtw.bif3.domain.card.CardClass;
import at.fhtw.bif3.domain.card.CardType;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TradingDeal {
    @SerializedName("Id")
    private String id;
    @SerializedName("CardToTrade")
    private Card cardToTrade;
    @SerializedName("Type")
    private CardType type;
    @SerializedName("MinimumDamage")
    private double minimumDamage;
    private User creator;

    public static TradingDTO createDTO(TradingDeal tradingDeal) {
        return new TradingDTO(tradingDeal.getId(),tradingDeal.getCardToTrade().getId(),
                tradingDeal.getType().name(), tradingDeal.getMinimumDamage());
    }
}
