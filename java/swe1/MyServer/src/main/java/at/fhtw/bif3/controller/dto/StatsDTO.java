package at.fhtw.bif3.controller.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class StatsDTO {
    private final int numberOfGamesPlayed;
    private final int elo;
}
