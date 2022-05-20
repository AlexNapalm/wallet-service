package ru.krasnopolsky.service;

import ru.krasnopolsky.entity.Player;
import ru.krasnopolsky.entity.Transaction;

import java.util.Optional;

public interface TransactionService {

    void add(Transaction transaction);

    Optional<Transaction> findById(Player player, long id);

}
