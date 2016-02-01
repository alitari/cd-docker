package de.alexkrieg.persontracker.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.needle4j.annotation.ObjectUnderTest;

import de.alexkrieg.persontracker.domain.model.Group;
import de.alexkrieg.persontracker.domain.model.Member;

public class MemberGroupingTest extends AbstractMemberTest {
    
    public static Condition<Optional<?>> PRESENT = new Condition<>(Optional::isPresent, "is present");

    @Before
    public void setup() throws Exception {
        deleteAll();
    }

    @ObjectUnderTest
    protected MemberGrouping memberGrouping;

    @Test
    public void createWithOneMember() throws Exception {
        final Member member = new Member.Builder("alex@test.de").withPassword("MySuperpass").build();
        executeInTransaction(() -> memberRegistration.register(member));
        Group group = executeInTransaction(
                () -> memberGrouping.create("newGroup", Optional.of(Arrays.asList(new Member[] { member }))));

        assertThat(group.getName()).as("check group name").isEqualTo("newGroup");
        assertThat(group.getMembers()).as("check group members").contains(member);

    }

    @Test
    public void createWithManyMember() throws Exception {
        List<Member> memberList = registerMembers(new Member.Builder("alex1@test.de").withPassword("MySuperpass1"),
                new Member.Builder("alex2@test.de").withPassword("MySuperpass2"),
                new Member.Builder("alex3@test.de").withPassword("MySuperpass2"));

        Group group = executeInTransaction(() -> memberGrouping.create("newGroup", Optional.of(memberList)));

        assertThat(group.getName()).as("check group name").isEqualTo("newGroup");
        assertThat(group.getMembers()).as("check group members").containsAll(memberList);
    }

    @Test
    public void addMember() throws Exception {
        Member member = registerMembers(new Member.Builder("alex@test.de").withPassword("MySuperpass")).iterator()
                .next();
        Group group = executeInTransaction(() -> memberGrouping.create("newGroup", Optional.empty()));
        Member memberWithGroup = executeInTransaction(() -> memberGrouping.add(group.getName(), member));

        Group groupOfmember = memberWithGroup.getGroups().iterator().next();
        Member memberOfGroupOfMember = groupOfmember.getMembers().iterator().next();
        assertThat(memberOfGroupOfMember).as("check relation member<->group").isEqualTo(memberWithGroup);
    }

    @Test
    public void addMemberGroupNotExists() throws Exception {
        Member member = executeInTransaction(() -> memberRegistration
                .register(new Member.Builder("alex@test.de").withPassword("MySuperpass").build()));

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            executeInTransaction(() -> memberGrouping.add("notExisting", member));
        }).withCauseInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void createEmpty() throws Exception {
        Group group = executeInTransaction(() -> memberGrouping.create("newGroup", Optional.empty()));

        assertThat(group.getName()).as("check group name").isEqualTo("newGroup");
        assertThat(group.getMembers()).isEmpty();
    }

    @Test
    public void createTwoWithSameName() throws Exception {
        executeInTransaction(() -> memberGrouping.create("sameName", Optional.empty()));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            executeInTransaction(() -> memberGrouping.create("sameName", Optional.empty()));
        }).withCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void deleteWithoutMembers() throws Exception {
        Group group = executeInTransaction(() -> memberGrouping.create("newGroup", Optional.empty()));
        assertThat(memberGrouping.findGroup("newGroup")).isPresent();
        Long id = executeInTransaction(() -> memberGrouping.delete("newGroup"));

        assertThat(group.getId()).isEqualTo(id);
        assertThat(memberGrouping.findGroup("newGroup")).isNot(PRESENT);
    }

    @Test
    public void deleteWithMembers() throws Exception {
        Member.Builder[] memberBuilderArray = new Member.Builder[] {
                new Member.Builder("john@worldcom.net.de").withPassword("MySuperpass"),
                new Member.Builder("john@worldcom.net2.de").withPassword("MySuperpass2"),
                new Member.Builder("john@worldcom.net3.de").withPassword("MySuperpass3") };
        List<Member> members = registerMembers(memberBuilderArray);
        Group group = executeInTransaction(() -> memberGrouping.create("manyGroup", Optional.of(members)));
        assertThat(group.getMembers().iterator().next().getGroups()).isNotEmpty();
        Long id = executeInTransaction(() -> memberGrouping.delete("manyGroup"));
        assertThat(group.getId()).isEqualTo(id);
        assertThat(memberGrouping.findGroup("manyGroup")).isNot(PRESENT);
        Stream.of(memberBuilderArray).forEach(memberBuilder -> assertThat(
                memberRegistration.findByEmail(memberBuilder.build().getEmail()).get().getGroups()).isEmpty());

    }

}
