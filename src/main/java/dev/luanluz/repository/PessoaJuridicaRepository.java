package dev.luanluz.repository;

import dev.luanluz.model.entity.PessoaJuridica;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PessoaJuridicaRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(PessoaJuridica pessoaJuridica) {
        em.persist(pessoaJuridica);
    }

    public PessoaJuridica pessoaJuridica(Long id) {
        return em.find(PessoaJuridica.class, id);
    }

    public List<PessoaJuridica> pessoaJuridicaPorNome(String razaoSocial) {
        try {
            Query query = em.createQuery("FROM PessoaJuridica WHERE razaoSocial LIKE CONCAT('%',:razaoSocial,'%')");
            query.setParameter("razaoSocial", razaoSocial);

            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<PessoaJuridica> pessoasJuridicas() {
        Query query = em.createQuery("from PessoaJuridica");
        return query.getResultList();
    }

    public void remove(Long id) {
        PessoaJuridica p = em.find(PessoaJuridica.class, id);
        em.remove(p);
    }

    public void update(PessoaJuridica pessoaJuridica) {
        em.merge(pessoaJuridica);
    }
}
