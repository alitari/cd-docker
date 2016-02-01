package de.alexkrieg.persontracker.domain;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.junit.Rule;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.db.transaction.TransactionHelper;
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

    protected void deleteAll()  {
        executeInTransaction((EntityManager e) -> {
            e.createQuery("delete from Group").executeUpdate();
            e.createQuery("delete from Member").executeUpdate();
            return null;
        });
    }
    
    protected List<Member> registerMembers(Member.Builder... memberBuilders) {
        List<Member> memberList = Stream
                .of(memberBuilders)
                .map(b -> executeInTransaction(() -> memberRegistration.register(b.build())))
                .collect(Collectors.toList());
        return memberList;
    }


    protected <T> T executeInTransaction(Supplier<T> supplier) {
        T result;
        try {
            result = transactionHelper.executeInTransaction(new org.needle4j.db.transaction.Runnable<T>() {
                @Override
                public T run(EntityManager entityManager) throws Exception {
                    return supplier.get();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    protected <T> T executeInTransaction(Function<EntityManager, T> function)  {
        T result;
        try {
            result = transactionHelper.executeInTransaction(new org.needle4j.db.transaction.Runnable<T>() {
                @Override
                public T run(EntityManager entityManager) throws Exception {
                    return function.apply(entityManager);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
