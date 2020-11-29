package at.fhtw.bif3.controller.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class StatsDTO {
    private int numberOfGamesPlayed;
    private int elo;
}
