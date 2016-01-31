package de.alexkrieg.persontracker.domain;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.needle4j.annotation.ObjectUnderTest;

import de.alexkrieg.persontracker.domain.model.Group;
import de.alexkrieg.persontracker.domain.model.Member;

public class MemberGroupingTest extends AbstractMemberTest {

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
        Group group = executeInTransaction(() -> memberGrouping.create("newGroup", Optional.of( Arrays.asList(new Member[] {member}))));

        assertThat(group.getName(), is("newGroup"));
        assertThat(group.getMembers(), contains(member));

    }

    @Test
    public void createWithManyMember() throws Exception {
        ArrayList<Member> memberList = new ArrayList<Member>();
        Member member1 = new Member.Builder("alex1@test.de").withPassword("MySuperpass2").build();
        executeInTransaction(() -> memberRegistration.register(member1));
        memberList.add(member1);

        Member member2 = new Member.Builder("alex2@test.de").withPassword("MySuperpass2").build();
        executeInTransaction(() -> memberRegistration.register(member2));
        memberList.add(member2);

        Member member3 = new Member.Builder("alex3@test.de").withPassword("MySuperpass3").build();
        executeInTransaction(() -> memberRegistration.register(member3));
        memberList.add(member3);

        Group group = executeInTransaction(() -> memberGrouping.create("newGroup", Optional.of( memberList)));

        assertThat(group.getName(), is("newGroup"));
        assertThat(group.getMembers(), containsInAnyOrder(member1, member2, member3));
    }

    @Test
    public void addMember() throws Exception {
        Member member = executeInTransaction(() -> memberRegistration.register(new Member.Builder("alex@test.de").withPassword("MySuperpass").build()));
        Group group = executeInTransaction(() -> memberGrouping.create("newGroup", Optional.empty()));
        Member memberWithGroup = executeInTransaction(() -> memberGrouping.add(group.getName(), member));       
        
        Group groupOfmember = memberWithGroup.getGroups().iterator().next();
        Member memberOfGroupOfMember = groupOfmember.getMembers().iterator().next();
        assertThat(memberOfGroupOfMember,is(memberWithGroup));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void addMemberGroupNotExists() throws Exception {
        Member member = executeInTransaction(() -> memberRegistration.register(new Member.Builder("alex@test.de").withPassword("MySuperpass").build()));
        executeInTransaction(() -> memberGrouping.add("notExisting", member));
    }

    @Test
    public void createEmpty() throws Exception {
        Group group = executeInTransaction(() -> memberGrouping.create("newGroup", Optional.empty()));

        assertThat(group.getName(), is("newGroup"));
        assertThat(group.getMembers().size(), is(0));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void createTwoWithSameName() throws Exception {
        executeInTransaction(() -> memberGrouping.create("sameName", Optional.empty()));
        executeInTransaction(() -> memberGrouping.create("sameName", Optional.empty()));
    }
    
    @Test
    public void deleteWithoutMembers() throws Exception {
        Group group = executeInTransaction(() -> memberGrouping.create("newGroup", Optional.empty()));
        assertThat( memberGrouping.findGroup("newGroup").isPresent(),is(true));
        Long id = executeInTransaction(() -> memberGrouping.delete("newGroup"));

        assertThat(group.getId(), is(id));
        assertThat( memberGrouping.findGroup("newGroup").isPresent(),is(false));
    }
    
    



}
