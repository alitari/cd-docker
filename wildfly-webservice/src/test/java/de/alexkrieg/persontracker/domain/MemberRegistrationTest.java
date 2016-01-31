package de.alexkrieg.persontracker.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import de.alexkrieg.persontracker.domain.model.Member;

public class MemberRegistrationTest extends AbstractMemberTest {

    @Before
    public void setup() {

    }

    @Test
    public void register() throws Exception {
        Member member = executeInTransaction(() -> memberRegistration
                .register(new Member.Builder("alex@test.de").withPassword("MySuperpass").build()));
        Member registeredMember = memberRegistration.findById(member.getId());
        assertThat(registeredMember.getId(), is(member.getId()));
        assertThat(registeredMember, is(member));
        assertThat(registeredMember.getEmail(), is("alex@test.de"));
        assertThat(registeredMember.getPassword(), is("MySuperpass"));
    }

    @Test
    public void findByEmail() throws Exception {
        final String email = "alreadyThere@test.de";
        Optional<Member> foundMember = memberRegistration.findByEmail(email);
        assertThat(foundMember.isPresent(), is(false));
        executeInTransaction(
                () -> memberRegistration.register(new Member.Builder(email).withPassword("MySuperpass").build()));

        foundMember = memberRegistration.findByEmail(email);
        assertThat(foundMember.isPresent(), is(true));
    }

    @Test
    public void unregister() throws Exception {
        Member member = executeInTransaction(() -> memberRegistration
                .register(new Member.Builder("alex@test.de").withPassword("MySuperpass").build()));
        Member registeredMember = memberRegistration.findById(member.getId());
        executeInTransaction(() -> {
            memberRegistration.unregister(registeredMember.getId());
            return null;
        });
        assertThat(memberRegistration.findById(member.getId()), is((Member) null));
    }

}
