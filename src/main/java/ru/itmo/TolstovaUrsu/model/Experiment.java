package ru.itmo.TolstovaUrsu.model;

import java.time.Instant;

public final class Experiment {
    private final long id;
    private final String name;
    private final String ownerUsername;
    private final Instant createdAt;

    public Experiment(long id, String name, String ownerUsername) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Ошибка: название эксперимента не может быть пустым");
        }
        this.id = id;
        this.name = name;
        this.ownerUsername = ownerUsername;
        this.createdAt = Instant.now();
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getOwnerUsername() { return ownerUsername; }
    public Instant getCreatedAt() { return createdAt; }
}