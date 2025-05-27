package com.mycompany.mislibros.logica;

import com.mycompany.mislibros.persistencia.ControladoraPersistencia;
import java.util.ArrayList;
import java.util.List;

public class Controladora {
    //esta controladora llama a la controladora de persistencia
    ControladoraPersistencia controlPersis = new ControladoraPersistencia();
    /*Este Método muestra todos los objetos libros que tengo en mi base de
        datos*/
    public String mostrarLibros(){ //tengo que borrarlo
        
        String texto = "";
        List<Libro> listaLibros = controlPersis.traerLibros();
        for(Libro libro:listaLibros){
            texto = texto  + libro.getId() +"\t"  + libro.getTitulo() 
            +"\t" + libro.getClasificacion() +"\t"
                    + libro.getNumero() + "\n";
        }
        return texto;
    }
    public void crearLibro (Libro lib){
        controlPersis.crearLibro (lib);
    }
    public void eliminarLibro(int id){
        controlPersis.eliminarLibro(id);
    }
    public void editarLibro(Libro lib){
        controlPersis.editarLibro(lib);
    }
    public Libro traerLibro(int id){
        return controlPersis.traerLibro(id);
    }
    public ArrayList<Libro> traerListaLibros(){
        return controlPersis.traerListaLibros();
    }
    //----------------autor--------------------------------------
    //CREATE - alta de registros
    public void crearAutor(Autor aut){
        controlPersis.crearAutor(aut);
    }
    //DESTROY - BAJA
    public void eliminarAutor(int id){
        controlPersis.eliminarAutor(id);
    }
    //EDIT - EDICION
    public void editarAutor(Autor aut){
        controlPersis.editarAutor(aut);
    }
    //FIND - LECTURA
    public Autor traerAutor(int id){
        return controlPersis.traerAutor(id);
    }
    
    public Autor traerAutorNombre(String pseudonimo){
        return controlPersis.traerAutorNombre(pseudonimo);
    }
    
    public ArrayList<Autor> traerListaAutores(){
        return controlPersis.traerListaAutores();
    }
    //Métodos ventanaPrincipal
    
}

