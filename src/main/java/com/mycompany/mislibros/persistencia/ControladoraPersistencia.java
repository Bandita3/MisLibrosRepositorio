package com.mycompany.mislibros.persistencia;

import com.mycompany.mislibros.modelo.Autor;
import com.mycompany.mislibros.modelo.Libro;
import com.mycompany.mislibros.persistencia.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControladoraPersistencia {
    AutorJpaController autJpa = new AutorJpaController();
    LibroJpaController libJpa = new LibroJpaController();
    
    /*Aca van a ir todos los metodos que se conecten con la base de datos
    como traer y colocar*/
    public List<Libro> traerLibros(){
        return libJpa.findLibroEntities();
        //SELECT * FROM LIBROS
    }

    public void crearLibro(Libro lib) {
        libJpa.create(lib);
    }
    public void eliminarLibro(int id){
        try{
            libJpa.destroy(id);
        }catch(NonexistentEntityException ex){
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Libro traerLibro(int id){
        return libJpa.findLibro(id);
    }
    public void editarLibro(Libro libro){
        try{
            libJpa.edit(libro);
        }catch(Exception ex){
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarAutor(Autor aut) {
        try {
            autJpa.edit(aut);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
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

    
    
}
