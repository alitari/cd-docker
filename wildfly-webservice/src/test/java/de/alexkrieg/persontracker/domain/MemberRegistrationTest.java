package de.alexkrieg.persontracker.domain;

import static org.hamcrest.CoreMatchers.not;
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
		Member member = new Member.Builder("alex@test.de").withPassword("MySuperpass").build();
		Long id = registerInTransaction(member);
		Member registeredMember = memberRegistration.findById(id);
		assertThat(registeredMember.getId(), is(id));
		assertThat(registeredMember, is(not(member)));
		assertThat(registeredMember.getEmail(), is("alex@test.de"));
		assertThat(registeredMember.getPassword(), is("MySuperpass"));
	}

	@Test
	public void findByEmail() throws Exception {
		final String email = "alreadyThere@test.de";
		Optional<Member> foundMember = memberRegistration.findByEmail(email);
		assertThat(foundMember.isPresent(), is(false));
		registerInTransaction(new Member.Builder(email).withPassword("MySuperpass").build());
		foundMember = memberRegistration.findByEmail(email);
		assertThat(foundMember.isPresent(), is(true));
	}

	@Test
	public void unregister() throws Exception {
		Member member = new Member.Builder("alex@test.de").withPassword("MySuperpass").build();
		Long id = registerInTransaction(member);
		Member registeredMember = memberRegistration.findById(id);
		unregisterInTransaction(registeredMember);
		assertThat(memberRegistration.findById(id), is((Member) null));
	}

}
