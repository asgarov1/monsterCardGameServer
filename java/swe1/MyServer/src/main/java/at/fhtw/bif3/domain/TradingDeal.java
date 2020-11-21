package at.fhtw.bif3.domain;

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
    private String cardToTradeId; //TODO question: why is this needed? I thought creating a deal is looking for a card?
    @SerializedName("CardType")
    private CardType cardtype;
    @SerializedName("MinimumDamage")
    private int minimumDamage;
    private String creatorUsername;
}
