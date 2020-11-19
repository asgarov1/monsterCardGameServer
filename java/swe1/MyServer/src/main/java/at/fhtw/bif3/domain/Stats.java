package at.fhtw.bif3.domain;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class Stats {
    private long id;
    private int numberOfGamesPlayed;
    private int ELO;
}
