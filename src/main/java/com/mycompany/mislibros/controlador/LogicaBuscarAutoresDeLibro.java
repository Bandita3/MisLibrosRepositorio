package com.mycompany.mislibros.controlador;
import com.mycompany.mislibros.modelo.Autor;
import com.mycompany.mislibros.modelo.Libro;
import java.util.List;

public class LogicaBuscarAutoresDeLibro {
    private Controladora control;

    public LogicaBuscarAutoresDeLibro(Controladora control) {
        this.control = control;
    }
    
    public String obtenerAutoresDeLibro(int idLibro) throws Exception {
        Libro libro = control.traerLibro(idLibro);

        if (libro == null) {
            return "No se encontró ningún libro con esa ID.";
        }

        List<Autor> autores = libro.getCreadores();
        if (autores != null && !autores.isEmpty()) {
            StringBuilder resultado = new StringBuilder("Autores del libro:\n");
            for (Autor autor : autores) {
                resultado.append("- ").append(autor.getPseudonimo()).append("\n");
            }
            return resultado.toString();
        } else {
            return "El libro no tiene autores registrados.";
        }
    }
}
