package com.mycompany.mislibros.persistencia;

import com.mycompany.mislibros.logica.Autor;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.mislibros.logica.Libro;
import com.mycompany.mislibros.persistencia.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * @author Martín_Guerra
 */
 
public class AutorJpaController implements Serializable {

    public AutorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    //Insertamos constructor que asocie ..
    public AutorJpaController(){
        emf = Persistence.createEntityManagerFactory("bibliotecaPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Autor autor) {
        if (autor.getLibrosescritos() == null) {
            autor.setLibrosescritos(new ArrayList<Libro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Libro> attachedLibrosescritos = new ArrayList<Libro>();
            for (Libro librosescritosLibroToAttach : autor.getLibrosescritos()) {
                librosescritosLibroToAttach = em.getReference(librosescritosLibroToAttach.getClass(), librosescritosLibroToAttach.getId());
                attachedLibrosescritos.add(librosescritosLibroToAttach);
            }
            autor.setLibrosescritos(attachedLibrosescritos);
            em.persist(autor);
            for (Libro librosescritosLibro : autor.getLibrosescritos()) {
                librosescritosLibro.getCreadores().add(autor);
                librosescritosLibro = em.merge(librosescritosLibro);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Autor autor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autor persistentAutor = em.find(Autor.class, autor.getId());
            List<Libro> librosescritosOld = persistentAutor.getLibrosescritos();
            List<Libro> librosescritosNew = autor.getLibrosescritos();
            List<Libro> attachedLibrosescritosNew = new ArrayList<Libro>();
            for (Libro librosescritosNewLibroToAttach : librosescritosNew) {
                librosescritosNewLibroToAttach = em.getReference(librosescritosNewLibroToAttach.getClass(), librosescritosNewLibroToAttach.getId());
                attachedLibrosescritosNew.add(librosescritosNewLibroToAttach);
            }
            librosescritosNew = attachedLibrosescritosNew;
            autor.setLibrosescritos(librosescritosNew);
            autor = em.merge(autor);
            for (Libro librosescritosOldLibro : librosescritosOld) {
                if (!librosescritosNew.contains(librosescritosOldLibro)) {
                    librosescritosOldLibro.getCreadores().remove(autor);
                    librosescritosOldLibro = em.merge(librosescritosOldLibro);
                }
            }
            for (Libro librosescritosNewLibro : librosescritosNew) {
                if (!librosescritosOld.contains(librosescritosNewLibro)) {
                    librosescritosNewLibro.getCreadores().add(autor);
                    librosescritosNewLibro = em.merge(librosescritosNewLibro);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = autor.getId();
                if (findAutor(id) == null) {
                    throw new NonexistentEntityException("The autor with id " + id + " no longer exists.");
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
            Autor autor;
            try {
                autor = em.getReference(Autor.class, id);
                autor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The autor with id " + id + " no longer exists.", enfe);
            }
            List<Libro> librosescritos = autor.getLibrosescritos();
            for (Libro librosescritosLibro : librosescritos) {
                librosescritosLibro.getCreadores().remove(autor);
                librosescritosLibro = em.merge(librosescritosLibro);
            }
            em.remove(autor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Autor> findAutorEntities() {
        return findAutorEntities(true, -1, -1);
    }

    public List<Autor> findAutorEntities(int maxResults, int firstResult) {
        return findAutorEntities(false, maxResults, firstResult);
    } 

    private List<Autor> findAutorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Autor.class));
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

    public Autor findAutor(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Autor.class, id);
        } finally {
            em.close();
        }
    }
    
    public Autor findAutorByName(String name) {
    EntityManager em = getEntityManager();
    try {
        TypedQuery<Autor> query = em.createQuery("SELECT a FROM Autor a WHERE a.pseudonimo = :name", Autor.class);
        query.setParameter("name", name);
        List<Autor> resultados = query.getResultList();
        
        if (!resultados.isEmpty()) {
            return resultados.get(0); // Devuelve el primero encontrado
        } else {
            return null; // No encontró ningún autor
        }
    } finally {
        em.close();
    }
}


    public int getAutorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Autor> rt = cq.from(Autor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
