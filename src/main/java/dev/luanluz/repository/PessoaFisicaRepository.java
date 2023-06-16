package dev.luanluz.repository;

import dev.luanluz.model.entity.PessoaFisica;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PessoaFisicaRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(PessoaFisica pessoaFisica) {
        em.persist(pessoaFisica);
    }

    public PessoaFisica pessoaFisica(Long id) {
        return em.find(PessoaFisica.class, id);
    }

    public List<PessoaFisica> pessoaFisicaPorNome(String nome) {
        try {
            Query query = em.createQuery("FROM PessoaFisica WHERE nome LIKE CONCAT('%',:nome,'%')");
            query.setParameter("nome", nome);

            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<PessoaFisica> pessoasFisicas() {
        Query query = em.createQuery("from PessoaFisica");
        return query.getResultList();
    }

    public void remove(Long id) {
        PessoaFisica p = em.find(PessoaFisica.class, id);
        em.remove(p);
    }

    public void update(PessoaFisica pessoaFisica) {
        em.merge(pessoaFisica);
    }
}
