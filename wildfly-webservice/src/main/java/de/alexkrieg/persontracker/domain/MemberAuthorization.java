package de.alexkrieg.persontracker.domain;

import static de.alexkrieg.persontracker.util.ValidationUtil.validatePresent;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.Validate;

import de.alexkrieg.persontracker.domain.model.Member;

@Stateless
public class MemberAuthorization {

    @Inject
    private Logger log;

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    public AuthAccessElement login(AuthLoginElement loginElement) {
        log.info("login member \"" + loginElement.getEmail() + "\"");
            Optional<Member> member = findMemberWithEmail(loginElement.getEmail());
            if ( !member.isPresent()) {
                return new AuthAccessElement(loginElement.getEmail(), null, null,
                        "Unknown member " + loginElement.getEmail());
            }
            if (!member.get().getPassword().equals(loginElement.getPassword())) {
                return new AuthAccessElement(loginElement.getEmail(), null, null,
                        "Unknown password for " + loginElement.getEmail());
            }

            String authToken = UUID.randomUUID().toString();
            member.get().setAuthToken(authToken);
            em.persist(member.get());
            return new AuthAccessElement(loginElement.getEmail(), authToken, "friend");
    }

    public Member logout(String email) {
        Optional<Member> member= findMemberWithEmail(email);
        validatePresent(member, " member with email="+email);
        Validate.notNull(member.get().getAuthToken());
        member.get().setAuthToken(null);
        return em.merge(member.get());
    }
    
    
    public boolean isAuthorized(AuthAccessElement authAccess) {
        return findMemberWithAuthToken(authAccess).isPresent();
    }

    public Optional<Member> findMemberWithAuthToken(AuthAccessElement authAccess) {
        Iterator<Member> resultIter = em.createQuery("from Member where authToken = :authToken", Member.class)
                .setParameter("authToken", authAccess.getAuthToken()).getResultList().iterator();
        return resultIter.hasNext() ? Optional.of(resultIter.next()) : Optional.empty();
    }
    
    public  Optional<Member> findMemberWithEmail(String email) {
        Iterator<Member> resultIter = em.createQuery("from Member where email= :email", Member.class)
                .setParameter("email", email).getResultList().iterator();
        return resultIter.hasNext() ? Optional.of(resultIter.next()) : Optional.empty();
    }


}
