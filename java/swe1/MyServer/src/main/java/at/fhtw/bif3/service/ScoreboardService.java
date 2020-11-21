package at.fhtw.bif3.service;

import at.fhtw.bif3.domain.User;

import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardService {
    private final UserService userService = new UserService();

    public List<User> getUsersSortedByElo() {
        return userService.findAll()
                .stream()
                .sorted((User a, User b) -> b.getElo() - a.getElo())
                .collect(Collectors.toList());
    }
}
