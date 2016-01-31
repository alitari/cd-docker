package de.alexkrieg.persontracker.domain;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.persistence.EntityManager;

import org.junit.Rule;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.db.transaction.TransactionHelper;
import org.needle4j.db.transaction.VoidRunnable;
import org.needle4j.junit.DatabaseRule;
import org.needle4j.junit.NeedleRule;

import de.alexkrieg.persontracker.domain.model.Member;

public abstract class AbstractMemberTest {

    @Rule
    public DatabaseRule databaseRule = new DatabaseRule();

    @Rule
    public NeedleRule needleRule = new NeedleRule(databaseRule);

    @ObjectUnderTest
    protected MemberRegistration memberRegistration;

    protected TransactionHelper transactionHelper = databaseRule.getTransactionHelper();

    protected void deleteAll() throws Exception {
        executeInTransaction(( EntityManager e) -> { 
            e.createQuery("delete from Group").executeUpdate();
            e.createQuery("delete from Member").executeUpdate();
            return null;
        });
    }

    protected <T> T executeInTransaction(Supplier<T> supplier) throws Exception {
        T result = transactionHelper.executeInTransaction(new org.needle4j.db.transaction.Runnable<T>() {
            @Override
            public T run(EntityManager entityManager) throws Exception {
                return supplier.get();
            }
        });
        return result;
    }

    protected <T> T executeInTransaction(Function<EntityManager, T> function) throws Exception {
        T result = transactionHelper.executeInTransaction(new org.needle4j.db.transaction.Runnable<T>() {
            @Override
            public T run(EntityManager entityManager) throws Exception {
                return function.apply(entityManager);
            }
        });
        return result;
    }

}
