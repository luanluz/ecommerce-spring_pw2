package dev.luanluz.repository;

import dev.luanluz.model.entity.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProdutoRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(Produto produto) {
        em.persist(produto);
    }

    public Produto produto(Long id) {
        return em.find(Produto.class, id);
    }

    public List<Produto> produtosPorNome(String descricao) {
        try {
            Query query = em.createQuery("FROM Produto WHERE descricao LIKE CONCAT('%',:descricao,'%')");
            query.setParameter("descricao", descricao);

            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Produto> produtos() {
        try {
            Query query = em.createQuery("FROM Produto");
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void remove(Long id) {
        Produto p = em.find(Produto.class, id);
        em.remove(p);
    }

    public void update(Produto produto) {
        em.merge(produto);
    }
}
