package de.alexkrieg.persontracker.domain;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.needle4j.annotation.ObjectUnderTest;

import de.alexkrieg.persontracker.domain.model.Member;

public class MemberAuthorizationTest extends AbstractMemberTest {

	@Before
	public void setup() throws Exception {
		deleteAll();
	}

	@ObjectUnderTest
	protected MemberAuthorization memberAuthorization;

	@Test
	public void loginSuccess() throws Exception {
		Member member = new Member.Builder("alex@test.de").withPassword("MySuperpass").build();
		executeInTransaction(() -> memberRegistration.register(member));
		AuthLoginElement loginElement = createAuthLogin(member);
		AuthAccessElement access = memberAuthorization
				.login(loginElement);
		assertThat(access, is(not((AuthAccessElement) null)));
		assertThat(access.getAuthId(), is(member.getEmail()));
		assertThat(access.getAuthPermission(), is("friend"));
		assertThat(access.getAuthToken(), is(not((String) null)));
		assertThat(access.getAuthToken().length(), is(36));
		assertThat(access.getMessage(), is("OK"));
	}

	private static AuthLoginElement createAuthLogin(Member member) {
		AuthLoginElement loginElement = new AuthLoginElement();
		loginElement.setEmail(member.getEmail());
		loginElement.setPassword(member.getPassword());
		return loginElement;
	}
	
	@Test
	public void loginUnknownEmail() throws Exception {
		Member member = new Member.Builder("alex@test.de").withPassword("MySuperpass").build();
		executeInTransaction(() -> memberRegistration.register(member));
		AuthLoginElement loginElement = createAuthLogin(member);
		loginElement.setEmail(loginElement.getEmail()+"X");
		AuthAccessElement access = memberAuthorization
				.login(loginElement);
		assertThat(access, is(not((AuthAccessElement) null)));
		assertThat(access.getAuthId(), is(member.getEmail()+"X"));
		assertThat(access.getAuthPermission(), is((String)null));
		assertThat(access.getAuthToken(), is((String) null));
		assertThat(access.getMessage(), is("Unknown member "+member.getEmail()+"X"));
	}
	
	@Test
	public void loginUnknownPassword() throws Exception {
		Member member = new Member.Builder("alex@test.de").withPassword("MySuperpass").build();
		executeInTransaction(() -> memberRegistration.register(member));
		AuthLoginElement loginElement = createAuthLogin(member);
		loginElement.setPassword(loginElement.getPassword()+"X");
		AuthAccessElement access = memberAuthorization
				.login(loginElement);
		assertThat(access, is(not((AuthAccessElement) null)));
		assertThat(access.getAuthId(), is(member.getEmail()));
		assertThat(access.getAuthPermission(), is((String)null));
		assertThat(access.getAuthToken(), is((String) null));
		assertThat(access.getMessage(), is("Unknown password for "+member.getEmail()));
	}
	

}
