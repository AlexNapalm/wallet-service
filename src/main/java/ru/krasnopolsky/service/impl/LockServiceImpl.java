package ru.krasnopolsky.service.impl;

import org.springframework.stereotype.Service;
import ru.krasnopolsky.service.LockService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class LockServiceImpl implements LockService {

    private final Map<String, Lock> locks = new ConcurrentHashMap<>();

    public void lock(String username) {
        locks.computeIfAbsent(username, s -> new ReentrantLock()).lock();
    }

    public void unlock(String username) {
        final Lock lock = locks.get(username);
        if (lock == null) {
            throw new IllegalStateException();
        }
        lock.unlock();
    }

}
