package com.mycompany.mislibros.persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.mislibros.logica.Autor;
import com.mycompany.mislibros.logica.Libro;
import com.mycompany.mislibros.persistencia.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Martín_Guerra
 */
 
public class LibroJpaController implements Serializable {

    public LibroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    public LibroJpaController(){
        emf = Persistence.createEntityManagerFactory("bibliotecaPU");
    }
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Libro libro) {
        if (libro.getCreadores() == null) {
            libro.setCreadores(new ArrayList<Autor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Autor> attachedCreadores = new ArrayList<Autor>();
            for (Autor creadoresAutorToAttach : libro.getCreadores()) {
                creadoresAutorToAttach = em.getReference(creadoresAutorToAttach.getClass(), creadoresAutorToAttach.getId());
                attachedCreadores.add(creadoresAutorToAttach);
            }
            libro.setCreadores(attachedCreadores);
            em.persist(libro);
            for (Autor creadoresAutor : libro.getCreadores()) {
                creadoresAutor.getLibrosescritos().add(libro);
                creadoresAutor = em.merge(creadoresAutor);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Libro libro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Libro persistentLibro = em.find(Libro.class, libro.getId());
            List<Autor> creadoresOld = persistentLibro.getCreadores();
            List<Autor> creadoresNew = libro.getCreadores();
            List<Autor> attachedCreadoresNew = new ArrayList<Autor>();
            for (Autor creadoresNewAutorToAttach : creadoresNew) {
                creadoresNewAutorToAttach = em.getReference(creadoresNewAutorToAttach.getClass(), creadoresNewAutorToAttach.getId());
                attachedCreadoresNew.add(creadoresNewAutorToAttach);
            }
            creadoresNew = attachedCreadoresNew;
            libro.setCreadores(creadoresNew);
            libro = em.merge(libro);
            for (Autor creadoresOldAutor : creadoresOld) {
                if (!creadoresNew.contains(creadoresOldAutor)) {
                    creadoresOldAutor.getLibrosescritos().remove(libro);
                    creadoresOldAutor = em.merge(creadoresOldAutor);
                }
            }
            for (Autor creadoresNewAutor : creadoresNew) {
                if (!creadoresOld.contains(creadoresNewAutor)) {
                    creadoresNewAutor.getLibrosescritos().add(libro);
                    creadoresNewAutor = em.merge(creadoresNewAutor);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = libro.getId();
                if (findLibro(id) == null) {
                    throw new NonexistentEntityException("The libro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Libro libro;
            try {
                libro = em.getReference(Libro.class, id);
                libro.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The libro with id " + id + " no longer exists.", enfe);
            }
            List<Autor> creadores = libro.getCreadores();
            for (Autor creadoresAutor : creadores) {
                creadoresAutor.getLibrosescritos().remove(libro);
                creadoresAutor = em.merge(creadoresAutor);
            }
            em.remove(libro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Libro> findLibroEntities() {
        return findLibroEntities(true, -1, -1);
    }

    public List<Libro> findLibroEntities(int maxResults, int firstResult) {
        return findLibroEntities(false, maxResults, firstResult);
    }

    private List<Libro> findLibroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Libro.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Libro findLibro(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Libro.class, id);
        } finally {
            em.close();
        }
    }

    public int getLibroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Libro> rt = cq.from(Libro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
