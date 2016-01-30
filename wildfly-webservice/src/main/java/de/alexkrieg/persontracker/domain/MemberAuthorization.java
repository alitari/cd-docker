package de.alexkrieg.persontracker.domain;

import java.util.UUID;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import de.alexkrieg.persontracker.domain.model.Member;

@Stateless
public class MemberAuthorization {

    @Inject
    private Logger log;

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    public AuthAccessElement login(AuthLoginElement loginElement) {
        log.info("login member \"" + loginElement.getEmail() + "\"");
        try {
            Member member = em.createQuery("from Member where email = :email", Member.class)
                    .setParameter("email", loginElement.getEmail()).getSingleResult();
            if (!member.getPassword().equals(loginElement.getPassword())) {
                return new AuthAccessElement(loginElement.getEmail(), null, null,
                        "Unknown password for " + loginElement.getEmail());
            }

            String authToken = UUID.randomUUID().toString();
            member.setAuthToken(authToken);
            em.persist(member);
            return new AuthAccessElement(loginElement.getEmail(), authToken, "friend");
        } catch (NoResultException e) {

            return new AuthAccessElement(loginElement.getEmail(), null, null,
                    "Unknown member " + loginElement.getEmail());
        }
    }

}
