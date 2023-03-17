package aula0203.atividade.repository;

import aula0203.atividade.model.entity.Venda;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
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
}
