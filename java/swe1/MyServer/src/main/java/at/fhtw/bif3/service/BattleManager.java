package at.fhtw.bif3.service;

import at.fhtw.bif3.domain.User;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class BattleManager {

    private final ScheduledExecutorService scheduledService = Executors.newSingleThreadScheduledExecutor();
    private final BlockingQueue<User> usersWaitingForBattle = new LinkedBlockingQueue<>();
    private final List<User> usersWhoFinishedBattle = new ArrayList<>();
    private final BattleService battleService = new BattleService();

    {
        scheduledService.scheduleWithFixedDelay(this::work, 0, 1, MILLISECONDS);
    }

    private BattleManager() { }

    private static class LazyHolder {
        private static final BattleManager instance = new BattleManager();
    }

    public static BattleManager getInstance() {
        return LazyHolder.instance;
    }

    @SneakyThrows
    public boolean putUserToBattle(User user) {
        usersWaitingForBattle.add(user);
        while (usersWhoFinishedBattle.stream().noneMatch(user::equals)) {
            sleep(100);
        }
        return usersWhoFinishedBattle.removeIf(user::equals);
    }

    @SneakyThrows
    private void work() {
        var player1 = usersWaitingForBattle.take();
        var player2 = usersWaitingForBattle.take();
        battleService.performBattle(player1, player2);
        usersWhoFinishedBattle.add(player1);
        usersWhoFinishedBattle.add(player2);
    }
}
