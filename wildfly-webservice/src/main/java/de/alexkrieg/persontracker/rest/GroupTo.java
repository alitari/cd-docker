package de.alexkrieg.persontracker.rest;

import java.util.Optional;

import de.alexkrieg.persontracker.domain.model.Group;

public class GroupTo {
    private final String name;
    private final String message;
    
    public GroupTo(Group group, Optional<String> message) {
        this.name = group.getName();
        this.message = message.isPresent() ? message.get() : "OK";
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }


}
