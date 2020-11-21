package at.fhtw.bif3.controller.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ScoreDTO {
    private int place;
    private String username;
    private int elo;
}
