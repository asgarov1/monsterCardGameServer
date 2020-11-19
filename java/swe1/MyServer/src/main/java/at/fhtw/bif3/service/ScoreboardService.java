package at.fhtw.bif3.service;

import at.fhtw.bif3.dao.UserDAO;
import at.fhtw.bif3.domain.User;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

public class ScoreboardService {
    private final UserService userService = new UserService();

    public List<User> getUsersSortedByElo() {
        return userService.findAll()
                .stream()
                .sorted((a, b) -> b.getStats().getELO() - a.getStats().getELO())
                .collect(Collectors.toList());
    }

}
