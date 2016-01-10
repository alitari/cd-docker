package de.alexkrieg.persontracker.domain;

import java.util.Optional;
import java.util.logging.Logger;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Session;

import de.alexkrieg.persontracker.domain.model.Member;

// The @Stateless annotation eliminates the need for manual transaction
// demarcation
@Stateless
public class MemberRegistration {

    @Inject
    private Logger log;

    @PersistenceContext(unitName="primary")
    private EntityManager em;

    @Transactional
    public Member register(Member member) throws Exception {
        log.info("Registering " + member.getEmail());
        em.persist(member);
        em.flush();
        return member;
    }

    Member findById(long id) throws Exception {
        Session session = (Session) em.getDelegate();
        return (Member) session.byId(Member.class).load(id);
    }

    @Transactional
    public void unregister(long id) throws Exception {
        log.info("unregister member with id=" + id);
        em.createQuery("delete from Member where id = :id").setParameter("id", id).executeUpdate();
    }

    public Optional<Member> findByEmail(String email) throws Exception {
        log.info("find member with email=" + email);
        try {
            return Optional.of(em.createQuery("from Member where email = :email", Member.class)
                    .setParameter("email", email).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

}
