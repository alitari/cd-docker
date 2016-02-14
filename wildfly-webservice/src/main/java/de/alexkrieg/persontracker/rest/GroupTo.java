package de.alexkrieg.persontracker.rest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;

import de.alexkrieg.persontracker.domain.model.Group;
import de.alexkrieg.persontracker.domain.model.Member;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupTo {
    private final String name;
    private final String message;
    private List<String> memberEmails;

    public GroupTo(Group group, Optional<String> message) {
        this.name = group.getName();
        this.message = message.isPresent() ? message.get() : "OK";
    }

    public GroupTo(Group group, Optional<String> message, String memberEmail) {
        this(group,message);
        memberEmails = Arrays.asList(new String[] { memberEmail });
    }

    public GroupTo(Group group, Optional<String> message, Member member) {
        this(group,message);
        memberEmails = Arrays.asList(new String[] { member.getEmail() });
    }

    public GroupTo(Group group, Optional<String> message, Set<Member> members) {
        this(group,message);
        this.memberEmails = members.stream().map(m -> m.getEmail()).collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getMemberEmails() {
        return memberEmails;
    }

}
