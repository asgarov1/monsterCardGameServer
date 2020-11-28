package at.fhtw.bif3.controller.dto;

import at.fhtw.bif3.domain.card.ElementType;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {

    @SerializedName("Id")
    String id;

    @SerializedName("Name")
    String name;

    @SerializedName("Damage")
    double damage;

    @SerializedName("Weakness")
    Double weakness;
}
