package dev.luanluz.repository;

import dev.luanluz.model.entity.Venda;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VendaRepository {
    @PersistenceContext
    private EntityManager em;

    public List<Venda> vendas() {
        Query query = em.createQuery("from Venda");
        return query.getResultList();
    }

    public List<Venda> vendasPorPessoa(Long usuarioId) {
        try {
            String jpql = "FROM Venda v WHERE v.pessoa.usuario.id = :usuarioId";
            TypedQuery<Venda> query = em.createQuery(jpql, Venda.class);
            query.setParameter("usuarioId", usuarioId);

            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Venda venda(Long id) {
        return em.find(Venda.class, id);
    }

    public void save(Venda venda) {
        em.persist(venda);
    }
}
