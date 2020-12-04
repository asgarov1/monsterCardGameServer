package at.fhtw.bif3.controller.dto;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ScoreDTO {
    private final int place;
    private final String username;
    private final int elo;
}
