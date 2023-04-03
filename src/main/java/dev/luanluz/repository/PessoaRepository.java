package dev.luanluz.repository;

import dev.luanluz.model.entity.Pessoa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class PessoaRepository {
    @PersistenceContext
    private EntityManager em;
    public Pessoa pessoa(Long id) {
        return em.find(Pessoa.class, id);
    }
}
