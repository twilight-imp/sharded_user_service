package org.example.entity;

import java.util.UUID;

public class UserEvent {
    private UUID id;
    private String name;

    private String email;
    private Event event;

    public UserEvent(UUID id, String name, String email, Event event) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.event = event;
    }

    protected UserEvent() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
