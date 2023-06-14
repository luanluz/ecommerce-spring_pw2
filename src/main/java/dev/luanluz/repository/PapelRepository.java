package dev.luanluz.repository;

import dev.luanluz.model.entity.Papel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class PapelRepository {
    @PersistenceContext
    private EntityManager em;

    public Papel papel(String papel) {
        try {
            String jpql = "FROM Papel p WHERE p.nome = :papel";
            TypedQuery<Papel> query = em.createQuery(jpql, Papel.class);
            query.setParameter("papel", papel);

            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
