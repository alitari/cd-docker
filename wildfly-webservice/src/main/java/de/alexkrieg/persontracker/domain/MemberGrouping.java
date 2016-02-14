package de.alexkrieg.persontracker.domain;

import static de.alexkrieg.persontracker.util.ValidationUtil.validateNotPresent;
import static de.alexkrieg.persontracker.util.ValidationUtil.validatePresent;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import de.alexkrieg.persontracker.domain.model.Group;
import de.alexkrieg.persontracker.domain.model.Member;

@Stateless
public class MemberGrouping {

    @Inject
    private Logger log;

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    @Transactional
    public Group create(String name, Optional<List<Member>> optMembers) {
        validateNotPresent(findGroup(name), "Group with name ");
        Group group = new Group.Builder(name).build();
        em.persist(group);
        optMembers.ifPresent(members -> members.stream().forEach(member -> {
            member.addGroup(group);
            em.merge(member);
        }));

        em.flush();
        return group;
    }

    @Transactional
    public Member add(String name, Member member) {
        Optional<Group> group = findGroup(name);
        validatePresent(group, "Group with name " + name);
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
        validatePresent(group, "Group with name " + name);
        Set<Member> members = group.get().getMembers();
        members.stream().forEach(member -> {
            member.getGroups().remove(group.get());
            em.merge(member);
        });
        em.createQuery(" delete from Group where name =:name").setParameter("name", name).executeUpdate();
        return group.get().getId();
    }

}
