package de.alexkrieg.persontracker.domain;

import javax.persistence.EntityManager;

import org.needle4j.db.testdata.AbstractTestdataBuilder;

import de.alexkrieg.persontracker.domain.model.Member;

public class MemberTestDataBuilder extends AbstractTestdataBuilder<Member> {

    private String email;
    private String password;
    private long id;

    public MemberTestDataBuilder() {
        super();
    }

    public MemberTestDataBuilder(EntityManager entityManager) {
        super(entityManager);
    }


    public MemberTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public MemberTestDataBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public MemberTestDataBuilder withId(long id) {
        this.id = id;
        return this;
    }
    


    private String getEmail() {
        return email != null ? email : "testemail@test.de";
    }

    private String getPassword() {
        return password != null ? password : "defaultpassword";
    }
    

    @Override
    public Member build() {
        final Member member = new Member();
        member.setEmail(getEmail());
        member.setPassword(getPassword());
        member.setId(id);
        return member;
    }
}
