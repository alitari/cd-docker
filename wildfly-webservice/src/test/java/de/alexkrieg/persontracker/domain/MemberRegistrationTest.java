package de.alexkrieg.persontracker.domain;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;

import org.junit.Rule;
import org.junit.Test;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.db.transaction.TransactionHelper;
import org.needle4j.db.transaction.VoidRunnable;
import org.needle4j.junit.DatabaseRule;
import org.needle4j.junit.NeedleRule;

import de.alexkrieg.persontracker.domain.model.Member;

public class MemberRegistrationTest {

    @Rule
    public DatabaseRule databaseRule = new DatabaseRule();

    @Rule
    public NeedleRule needleRule = new NeedleRule(databaseRule);

    @ObjectUnderTest
    private MemberRegistration memberRegistration;

    private TransactionHelper transactionHelper = databaseRule.getTransactionHelper();

    @Test
    public void register() throws Exception {
        Member member = new Member.Builder("alex@test.de").hasPhone("07214536231").withName("Alex").livingIn("Germany")
                .build();
        Long id = registerInTransaction(member);
        Member registeredMember = memberRegistration.findById(id);
        assertThat(registeredMember.getId(), is(id));
        assertThat(registeredMember, is(not(member)));
        assertThat(registeredMember.getEmail(), is("alex@test.de"));
        assertThat(registeredMember.getPhoneNumber(), is("07214536231"));
        assertThat(registeredMember.getName(), is("Alex"));
        assertThat(registeredMember.getAddress(), is("Germany"));
    }
    
    @Test
    public void unregister() throws Exception {
        Member member = new Member.Builder("alex@test.de").hasPhone("07214536231").withName("Alex").livingIn("Germany")
                .build();
        Long id = registerInTransaction(member);
        Member registeredMember = memberRegistration.findById(id);
        unregisterInTransaction(registeredMember);
        assertThat(memberRegistration.findById(id), is((Member) null));
    }

    private Long registerInTransaction(Member member) throws Exception {
        Long id = transactionHelper.executeInTransaction(new org.needle4j.db.transaction.Runnable<Long>() {
            @Override
            public Long run(EntityManager entityManager) throws Exception {
                Member registeredMember = memberRegistration.register(member);
                return registeredMember.getId();
            }
        });
        return id;
    }
    
    private void unregisterInTransaction(Member member) throws Exception {
        transactionHelper.executeInTransaction(new VoidRunnable() {
            @Override
            public void doRun(EntityManager entityManager) throws Exception {
                memberRegistration.unregister(member.getId());
            }
        });
    }
}
