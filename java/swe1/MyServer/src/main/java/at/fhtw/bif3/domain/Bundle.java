package at.fhtw.bif3.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Bundle {
    private String id;
    private final List<Card> cards = new ArrayList<>(5);
}
