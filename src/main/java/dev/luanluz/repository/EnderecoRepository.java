package dev.luanluz.repository;

import dev.luanluz.model.entity.Endereco;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnderecoRepository {
    @PersistenceContext
    private EntityManager em;

    public List<Endereco> enderecos() {
        Query query = em.createQuery("from Endereco");
        return query.getResultList();
    }

    public Endereco endereco(Long id) {
        return em.find(Endereco.class, id);
    }

    public void save(Endereco endereco) {
        em.persist(endereco);
    }
}
