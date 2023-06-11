package dev.luanluz.repository;

import dev.luanluz.model.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepository {
    @PersistenceContext
    private EntityManager em;

    public Usuario usuario(String usuario) {
        try {
            String jpql = "FROM Usuario u WHERE u.usuario = :usuario";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("usuario", usuario);

            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void save(Usuario usuario) {
        em.persist(usuario);
    }
}
