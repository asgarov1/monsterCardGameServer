package at.fhtw.bif3.controller.dto;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ScoreDTO {
    private int place;
    private String username;
    private int elo;
}
