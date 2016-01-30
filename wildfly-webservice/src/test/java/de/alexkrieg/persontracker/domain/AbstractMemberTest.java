package de.alexkrieg.persontracker.domain;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Rule;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.db.transaction.TransactionHelper;
import org.needle4j.db.transaction.VoidRunnable;
import org.needle4j.junit.DatabaseRule;
import org.needle4j.junit.NeedleRule;

import de.alexkrieg.persontracker.domain.model.Member;

public class AbstractMemberTest {

    @Rule
    public DatabaseRule databaseRule = new DatabaseRule();

    @Rule
    public NeedleRule needleRule = new NeedleRule(databaseRule);

    @ObjectUnderTest
    protected MemberRegistration memberRegistration;

    protected TransactionHelper transactionHelper = databaseRule.getTransactionHelper();
    
    protected void deleteAll() throws Exception {
        transactionHelper.executeInTransaction(new VoidRunnable() {
            @Override
            public void doRun(EntityManager entityManager) throws Exception {
            	entityManager.createQuery("delete from Member").executeUpdate();
            }
        });
    }
    
    protected Long registerInTransaction(Member member) throws Exception {
        Long id = transactionHelper.executeInTransaction(new org.needle4j.db.transaction.Runnable<Long>() {
            @Override
            public Long run(EntityManager entityManager) throws Exception {
                Member registeredMember = memberRegistration.register(member);
                return registeredMember.getId();
            }
        });
        return id;
    }
    
    protected void unregisterInTransaction(Member member) throws Exception {
        transactionHelper.executeInTransaction(new VoidRunnable() {
            @Override
            public void doRun(EntityManager entityManager) throws Exception {
                memberRegistration.unregister(member.getId());
            }
        });
    }
}
