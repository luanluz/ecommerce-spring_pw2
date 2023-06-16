package dev.luanluz.repository;

import dev.luanluz.model.entity.Pessoa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PessoaRepository {
    @PersistenceContext
    private EntityManager em;

    public List<Pessoa> pessoas() {
        try {
            Query query = em.createQuery("FROM Pessoa");
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
    public Pessoa pessoa(Long id) {
        return em.find(Pessoa.class, id);
    }

    public void update(Pessoa pessoa) {
        em.merge(pessoa);
    }
}
