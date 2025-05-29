package com.mycompany.mislibros.logica;
import com.mycompany.mislibros.modelo.Autor;
import com.mycompany.mislibros.modelo.Libro;
import java.util.ArrayList;
public class LogicaCrearLibro {
    private Controladora control;
    public LogicaCrearLibro(Controladora control){
        this.control = control;
    }
    public void crearLibro(String titulo, String clasificacion, int numero, String pseudonimoAutor) throws Exception {
        //  verificar existencia del autor
        Autor autor = control.traerAutorNombre(pseudonimoAutor);
        //si no existe el autor en la base de datos...
        if (autor == null) {//...Creo el autor.
            autor = new Autor(pseudonimoAutor, new ArrayList<>());
            control.crearAutor(autor); //sube el autor a la base de datos
            System.out.println("Autor nuevo creado");
        } else {/*...Si ya existe un autor con esa ID en la base de datos pero con otro pseudonimo,
             aviso por pantalla*/
            if (!autor.getPseudonimo().equals(pseudonimoAutor)) {
                throw new Exception("Ya existe un autor con ese ID, pero con otro pseudónimo");
            } else {/*Si ya existe el autor con esa ID y pseudonimo, 
                insertamos ese autor al libro.*/
                System.out.println("Autor existente reutilizado");
            }
        }

        // creamos el libro y lo asociamos al autor
        ArrayList<Autor> listaAutores = new ArrayList<>();
        listaAutores.add(autor);
        Libro libro = new Libro(titulo, clasificacion, numero, listaAutores);

        // Lo subimos a la base de datos.
        control.crearLibro(libro);//sube el libro a la base de datos
    }
}
