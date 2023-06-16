package dev.luanluz.repository;

import dev.luanluz.model.entity.Venda;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    public List<Venda> vendasDePessoaPorData(Long usuarioId, LocalDate date) {
        try {
            String jpql = "FROM Venda v WHERE v.pessoa.usuario.id = :usuarioId AND data = :date";
            TypedQuery<Venda> query = em.createQuery(jpql, Venda.class);
            query.setParameter("usuarioId", usuarioId);
            query.setParameter("date", date);

            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Venda> vendasPorData(LocalDate date) {
        try {
            Query query = em.createQuery("FROM Venda WHERE data = :date");
            query.setParameter("date", date);

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
