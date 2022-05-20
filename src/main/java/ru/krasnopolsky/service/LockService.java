package ru.krasnopolsky.service;

public interface LockService {

    void lock(String username);

    void unlock(String username);

}
