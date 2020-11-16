package at.fhtw.bif3.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//Table name player because 'user' is a reserved table
public class Player {
    private String id;
    private String username;
    private String password;
    private List<Card> cards = new ArrayList<>();
    private int numberOfCoins = 20;

    public Player(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return numberOfCoins == player.numberOfCoins &&
                Objects.equals(id, player.id) &&
                Objects.equals(username, player.username) &&
                Objects.equals(password, player.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, numberOfCoins);
    }
}
