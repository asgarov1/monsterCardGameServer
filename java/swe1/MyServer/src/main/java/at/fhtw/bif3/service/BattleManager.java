package at.fhtw.bif3.service;

import at.fhtw.bif3.domain.User;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class BattleManager {
    private final ScheduledExecutorService scheduledService = Executors.newSingleThreadScheduledExecutor();
    private final BlockingQueue<User> usersWaitingForBattle = new LinkedBlockingQueue<>();
    private final List<User> usersWhoFinishedBattle = new ArrayList<>();

    {
        scheduledService.scheduleWithFixedDelay(this::work, 0, 100, MILLISECONDS);
    }

    @SneakyThrows
    public boolean putUserToBattle(User user) {
        usersWaitingForBattle.add(user);

        while (usersWhoFinishedBattle.stream().noneMatch(user::equals)) {
            sleep(100);
        }

        return usersWhoFinishedBattle.removeIf(user::equals);
    }

    private static class LazyHolder {
        static final BattleManager instance = new BattleManager();
    }

    public static BattleManager getInstance() {
        return LazyHolder.instance;
    }

    @SneakyThrows
    private void work() {
        var player1 = usersWaitingForBattle.take();
        var player2 = usersWaitingForBattle.take();
        performBattle(player1, player2);
        usersWhoFinishedBattle.add(player1);
        usersWhoFinishedBattle.add(player2);
    }

    private void performBattle(User player1, User player2) {
        //didnt care to write the actual battle logic yet, this is just for test
        player1.setNumberOfCoins(player1.getNumberOfCoins()-1);
        player2.setNumberOfCoins(player2.getNumberOfCoins()+1);
    }
}
