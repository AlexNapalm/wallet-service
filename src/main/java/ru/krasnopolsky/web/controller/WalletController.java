package ru.krasnopolsky.web.controller;

import ru.krasnopolsky.web.dto.request.BalanceChangeRequest;
import ru.krasnopolsky.web.dto.response.BalanceChangeResult;
import ru.krasnopolsky.service.impl.WalletServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class WalletController {

    private final WalletServiceImpl walletService;

    public WalletController(WalletServiceImpl walletService) {
        this.walletService = walletService;
    }

    @PostMapping(value = "/balance/update")
    public BalanceChangeResult updateBalance(@Valid @RequestBody BalanceChangeRequest request) {
        return walletService.updateBalance(request.getUsername(), request.getTransactionId(), request.getBalanceChange());
    }
}
