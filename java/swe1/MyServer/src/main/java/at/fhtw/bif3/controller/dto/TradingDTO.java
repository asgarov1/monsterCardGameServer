package at.fhtw.bif3.controller.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradingDTO {
    @SerializedName("Id")
    private String id;
    @SerializedName("CardToTrade")
    private String cardToTradeId;
    @SerializedName("Type")
    private String type;
    @SerializedName("MinimumDamage")
    private double minimumDamage;
}
