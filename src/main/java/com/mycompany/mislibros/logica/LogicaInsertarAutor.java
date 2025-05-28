package com.mycompany.mislibros.logica;
import com.mycompany.mislibros.controlador.Controladora;
import com.mycompany.mislibros.modelo.Autor;
import com.mycompany.mislibros.modelo.Libro;
import java.util.ArrayList;
import java.util.List;
public class LogicaInsertarAutor {
    private Controladora control;

    public LogicaInsertarAutor(Controladora control) {
        this.control = control;
    }
    public boolean agregarAutorALibro(int idLibro, String pseudonimoAutor) throws Exception {
        // Traer el libro desde la base
        Libro libro = control.traerLibro(idLibro);//se comunica con la controladora.
        if (libro == null) {
            throw new Exception("No se encontró el libro con ID: " + idLibro);
        }

        // Buscar autor existente
        Autor autorExistente = null;
        List<Autor> listaAutores = control.traerListaAutores();
        for (Autor a : listaAutores) {
            if (a.getPseudonimo().equalsIgnoreCase(pseudonimoAutor)) {
                autorExistente = a;
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
            control.crearAutor(autor);
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
        control.editarLibro(libro);

        return true;
    }
}
