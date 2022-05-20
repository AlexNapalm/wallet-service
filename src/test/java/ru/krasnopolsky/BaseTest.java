package ru.krasnopolsky;

import ru.krasnopolsky.entity.Player;
import ru.krasnopolsky.repository.BlacklistRepository;
import ru.krasnopolsky.repository.PlayerRepository;
import ru.krasnopolsky.service.PlayerService;
import ru.krasnopolsky.service.WalletService;
import ru.krasnopolsky.usecase.balance.UpdateBalanceBuilder;
import ru.krasnopolsky.usecase.user.BlacklistAddPlayerBuilder;
import ru.krasnopolsky.usecase.user.BlacklistRemovePlayerBuilder;
import ru.krasnopolsky.usecase.user.CreatePlayerBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {WalletServiceApplication.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(BaseTest.TEST_PROFILE)
public abstract class BaseTest {

    static final String TEST_PROFILE = "test";

    @Autowired
    protected PlayerService playerService;

    @Autowired
    protected WalletService walletService;

    @Autowired
    protected BlacklistRepository blacklistRepository;

    @Autowired
    protected PlayerRepository playerRepository;

    @BeforeEach
    void setUp() {
        blacklistRepository.deleteAll();
        playerService.cleanup();
        playerRepository.deleteAll();
    }

    public CreatePlayerBuilder createPlayer() {
        return new CreatePlayerBuilder(playerService);
    }

    public UpdateBalanceBuilder updateBalance(Player player) {
        return new UpdateBalanceBuilder(walletService, player.getUsername());
    }

    public BlacklistAddPlayerBuilder addToBlackList(Player player) {
        return new BlacklistAddPlayerBuilder(blacklistRepository, player);
    }

    public BlacklistRemovePlayerBuilder removeFromBlacklist(Player player) {
        return new BlacklistRemovePlayerBuilder(blacklistRepository, player);
    }

}
