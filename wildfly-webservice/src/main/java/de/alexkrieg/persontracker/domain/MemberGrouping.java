package de.alexkrieg.persontracker.domain;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang.Validate;

import de.alexkrieg.persontracker.domain.model.Group;
import de.alexkrieg.persontracker.domain.model.Member;

@Stateless
public class MemberGrouping {

    @Inject
    private Logger log;

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    @Transactional
    public Group create(String name, Optional<List<Member>> members) {
        Validate.isTrue(!findGroup(name).isPresent(), "Group with name " + name + " already exists");
        Group.Builder builder = new Group.Builder(name);
        if (members.isPresent()) {
            builder.withMembers(members.get());
        }
        Group group = builder.build();
        em.persist(group);
        em.flush();
        return group;
    }

    @Transactional
    public Member add(String name, Member member) {
        Optional<Group> group = findGroup(name);
        Validate.isTrue(group.isPresent(), "Group with name " + name + " does not exist");
        member.addGroup(group.get());
        em.merge(member);
        em.merge(group.get());
        return member;
    }

    public Optional<Group> findGroup(String name) {
        List<Group> resultList = em.createQuery("from Group where name = :name", Group.class).setParameter("name", name)
                .getResultList();
        return resultList.size() == 1 ? Optional.of(resultList.iterator().next()) : Optional.empty();
    }

    @Transactional
    public Long delete(String name) {
        Optional<Group> group = findGroup(name);
        Validate.isTrue(group.isPresent(), "Group with name " + name + " already exists");
        em.createQuery(" delete from Group where name =:name").setParameter("name", name).executeUpdate();
        return group.get().getId();
    }

}
