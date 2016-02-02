package de.alexkrieg.persontracker.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;

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
        Member member = registerMembers(new Member.Builder("alex@test.de").withPassword("MySuperpass")).iterator().next();
        AuthLoginElement loginElement = createAuthLogin(member);
        AuthAccessElement access = memberAuthorization.login(loginElement);
        assertThat(access).isNotNull();
        assertThat(access.getAuthId()).isEqualTo(member.getEmail());
        assertThat(access.getAuthPermission()).isEqualTo("friend");
        assertThat(access.getAuthToken()).isNotNull();
        assertThat(access.getAuthToken()).hasSize(36);
        assertThat(access.getMessage()).isEqualTo("OK");
    }

    private static AuthLoginElement createAuthLogin(Member member) {
        AuthLoginElement loginElement = new AuthLoginElement();
        loginElement.setEmail(member.getEmail());
        loginElement.setPassword(member.getPassword());
        return loginElement;
    }

    @Test
    public void loginUnknownEmail() throws Exception {
        Member member = registerMembers(new Member.Builder("alex@test.de").withPassword("MySuperpass")).iterator().next();
        AuthLoginElement loginElement = createAuthLogin(member);
        loginElement.setEmail(loginElement.getEmail() + "X");
        AuthAccessElement access = memberAuthorization.login(loginElement);
        assertThat(access).isNotNull();
        assertThat(access.getAuthId()).isEqualTo(member.getEmail() + "X");
        assertThat(access.getAuthPermission()).isNull();
        assertThat(access.getAuthToken()).isNull();
        assertThat(access.getMessage()).isEqualTo("Unknown member " + member.getEmail() + "X");
    }

    @Test
    public void loginUnknownPassword() throws Exception {
        Member member = registerMembers(new Member.Builder("alex@test.de").withPassword("MySuperpass")).iterator().next();
        AuthLoginElement loginElement = createAuthLogin(member);
        loginElement.setPassword(loginElement.getPassword() + "X");
        AuthAccessElement access = memberAuthorization.login(loginElement);
        assertThat(access).isNotNull();
        assertThat(access.getAuthId()).isEqualTo(member.getEmail());
        assertThat(access.getAuthPermission()).isNull();
        assertThat(access.getAuthToken()).isNull();
        assertThat(access.getMessage()).isEqualTo("Unknown password for " + member.getEmail());
    }

    @Test
    public void logoutSuccess() throws Exception {
        List<Member> members = registerMembers(new Member.Builder("alex@test.de").withPassword("MySuperpass"));
        AuthLoginElement loginElement = createAuthLogin(members.iterator().next());
        loginElement.setPassword(loginElement.getPassword());
        AuthAccessElement accessElement = memberAuthorization.login(loginElement);
        assertThat(accessElement.getAuthToken()).isNotNull();
        Member loggedOutMember = memberAuthorization.logout("alex@test.de");
        assertThat(loggedOutMember.getAuthToken()).isNull();
    }

    @Test
    public void logoutUnknownEmail() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> memberAuthorization.logout("alex@test.de"));
    }
    
    @Test
    public void logoutNotLoggedIn() throws Exception {
        registerMembers(new Member.Builder("alex@test.de").withPassword("MySuperpass"));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> memberAuthorization.logout("alex@test.de"));
    }

}
