package com.mycompany.mislibros.controlador;

import com.mycompany.mislibros.modelo.Libro;
import com.mycompany.mislibros.modelo.Autor;
import com.mycompany.mislibros.modelo.AutorJpaController;
import com.mycompany.mislibros.modelo.LibroJpaController;
import com.mycompany.mislibros.modelo.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Controladora {
    AutorJpaController autJpa = new AutorJpaController();
    LibroJpaController libJpa = new LibroJpaController();
    

    /*Este Método muestra todos los objetos libros que tengo en mi base de
        datos*/
   public List<Libro> traerLibros(){
        return libJpa.findLibroEntities(); //se comunica con nuestro JPa que trae el libro de la base de datos
        //SELECT * FROM LIBROS
    }

    public void crearLibro(Libro lib) {
        libJpa.create(lib);
    }
    public void eliminarLibro(int id){
       
        try {
            libJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(Controladora.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    public Libro traerLibro(int id){
        return libJpa.findLibro(id);
    }
    public void editarLibro(Libro libro){
        try{
            libJpa.edit(libro);
        }catch(Exception ex){
            Logger.getLogger(Controladora.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public ArrayList<Libro> traerListaLibros() {
        List<Libro> listita = libJpa.findLibroEntities();
        ArrayList<Libro> listaLibros = new ArrayList<Libro>(listita);
        return listaLibros;
    }
    //------------------autor

    public void crearAutor(Autor aut) {
        autJpa.create(aut);
    }

    public void eliminarAutor(int id) {
        try {
            autJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(Controladora.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarAutor(Autor aut) {
        try {
            autJpa.edit(aut);
        } catch (Exception ex) {
            Logger.getLogger(Controladora.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Autor traerAutor(int id) {
        return autJpa.findAutor(id);
    }

    public ArrayList<Autor> traerListaAutores() {
        List<Autor> listita = autJpa.findAutorEntities();
        ArrayList<Autor> listaAutores = new ArrayList<Autor>(listita);
        return listaAutores;
    }

    public Autor traerAutorNombre(String pseudonimo) {
        return autJpa.findAutorByName(pseudonimo);
    }
    //Métodos ventanaPrincipal
    
    
    ///logica insertar autor
    public boolean agregarAutorALibro(int idLibro, String pseudonimoAutor) throws Exception {
        // Traer el libro desde la base
        Libro libro =traerLibro(idLibro);//se comunica con la controladora.
        if (libro == null) {
            throw new Exception("No se encontró el libro con ID: " + idLibro);
        }

        // Buscar autores repetidos
        Autor autorExistente = null;
        List<Autor> listaAutores =traerListaAutores(); //traigo todos lso autores de mi base de datos y lo agrego ami nueva lsita Autor
        for (Autor a : listaAutores) {
            if (a.getPseudonimo().equalsIgnoreCase(pseudonimoAutor)) {
                autorExistente = a;//si el autor se encuetra en la base de datos usamos el mismo autor
                break;
            }
        }

        Autor autor;
        if (autorExistente != null) { 
            autor = autorExistente;
        } else {
            // Crear nuevo autor con pseudónimo y lista vacía de libros
            autor = new Autor();
            autor.setPseudonimo(pseudonimoAutor);
            autor.setLibrosescritos(new ArrayList<>());
            crearAutor(autor);
        }

        // Agregar autor al libro si no está
        if (!libro.getCreadores().contains(autor)) {
            libro.getCreadores().add(autor);
        }

        // Agregar libro al autor si no está
        if (!autor.getLibrosescritos().contains(libro)) {
            autor.getLibrosescritos().add(libro);
        }

        // Guardar cambios en el libro
        editarLibro(libro);

        return true;
    }
    
    
    
}

