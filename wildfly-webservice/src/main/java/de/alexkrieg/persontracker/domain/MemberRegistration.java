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

import org.hibernate.Session;

import de.alexkrieg.persontracker.domain.model.Member;

// The @Stateless annotation eliminates the need for manual transaction
// demarcation
@Stateless
public class MemberRegistration {

	@Inject
	private Logger log;

	@PersistenceContext(unitName = "primary")
	private EntityManager em;

	@Transactional
	public Member register(Member member) {
		log.info("Registering " + member.getEmail());
		em.persist(member);
		em.flush();
		return member;
	}

	Member findById(long id) {
		Session session = (Session) em.getDelegate();
		return (Member) session.byId(Member.class).load(id);
	}

	@Transactional
	public void unregister(long id) {
		log.info("unregister member with id=" + id);
		em.createQuery("delete from Member where id = :id").setParameter("id", id).executeUpdate();
	}

	public Optional<Member> findByEmail(String email) {
		log.info("find member with email=" + email);
		List<Member> resultList = em.createQuery("from Member where email = :email", Member.class)
				.setParameter("email", email).getResultList();
		return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.iterator().next());
	}

}
