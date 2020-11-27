package at.fhtw.bif3.service;

import at.fhtw.bif3.domain.User;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BattleManagerTest {

    ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(10);

    @Test
    public void battleShouldWorkOk() throws InterruptedException, ExecutionException {
        var a = new User("userA", "pass");
        var b = new User("userB", "pass");

        CompletionService<Boolean> service = new ExecutorCompletionService<>(WORKER_THREAD_POOL);

        assertTrue(service.submit(() -> BattleManager.getInstance().putUserToBattle(a)).get());
        assertTrue(service.submit(() -> BattleManager.getInstance().putUserToBattle(b)).get());
    }
}