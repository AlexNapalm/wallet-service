package ru.krasnopolsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.krasnopolsky.entity.Blacklist;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, String> {
}
