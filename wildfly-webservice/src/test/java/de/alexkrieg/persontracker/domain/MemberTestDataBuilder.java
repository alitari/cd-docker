package de.alexkrieg.persontracker.domain;

import javax.persistence.EntityManager;

import org.needle4j.db.testdata.AbstractTestdataBuilder;

import de.alexkrieg.persontracker.domain.model.Member;

public class MemberTestDataBuilder extends AbstractTestdataBuilder<Member> {

    private String address;
    private String email;
    private String phone;
    private long id;
    private String name;

    public MemberTestDataBuilder() {
        super();
    }

    public MemberTestDataBuilder(EntityManager entityManager) {
        super(entityManager);
    }

    public MemberTestDataBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public MemberTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public MemberTestDataBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public MemberTestDataBuilder withId(long id) {
        this.id = id;
        return this;
    }
    
    public MemberTestDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    private String getAddress() {
        return address != null ? address : "TestAdress";
    }

    private String getEmail() {
        return email != null ? email : "testemail@test.de";
    }

    private String getPhone() {
        return phone != null ? phone : "01767777888";
    }
    
    private String getName() {
        return name != null ? name : "TestName";
    }

    @Override
    public Member build() {
        final Member member = new Member();
        member.setAddress(getAddress());
        member.setEmail(getEmail());
        member.setName(getName());
        member.setPhoneNumber(getPhone());
        member.setId(id);

        return member;
    }
}
