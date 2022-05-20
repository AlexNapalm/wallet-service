package ru.krasnopolsky.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

//@Document(collection = "blacklist", schemaVersion = "1.0")
@Entity
public class Blacklist {

    @Id
    private String username;

    public String getUsername() {
        return username;
    }

    public Blacklist setUsername(String username) {
        this.username = username;
        return this;
    }
}