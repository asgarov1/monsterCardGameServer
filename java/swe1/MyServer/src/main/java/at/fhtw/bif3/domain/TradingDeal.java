package at.fhtw.bif3.domain;

import at.fhtw.bif3.domain.card.Card;
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
    @SerializedName("CardToTradeId")
    private Card cardToTrade;
    @SerializedName("CardType")
    private CardType cardtype;
    @SerializedName("MinimumDamage")
    private double minimumDamage;
    private User creator;
}
