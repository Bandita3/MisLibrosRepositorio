package com.mycompany.mislibros.modelo;
import com.mycompany.mislibros.modelo.Libro;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Autor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    @Basic
    public String pseudonimo;
    @ManyToMany
    private List<Libro> librosescritos;

    public Autor() {
    }

    public Autor(int id, String pseudonimo, ArrayList<Libro> librosescritos) {
        this.id = id;
        this.pseudonimo = pseudonimo;
        this.librosescritos = librosescritos;
    }
    
    public Autor( String pseudonimo, ArrayList<Libro> librosescritos) {
        this.pseudonimo = pseudonimo;
        this.librosescritos = librosescritos;
    }
    
    public void aniadirLibrosEscritos(Libro libro){
        librosescritos.add(libro);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPseudonimo() {
        return pseudonimo;
    }

    public void setPseudonimo(String pseudonimo) {
        this.pseudonimo = pseudonimo;
    }

    public List<Libro> getLibrosescritos() {
        return librosescritos;
    }

    public void setLibrosescritos(List<Libro> librosescritos) {
        this.librosescritos = librosescritos;
    }
    
}
