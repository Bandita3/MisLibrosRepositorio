    package com.mycompany.mislibros.logica;
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
public class Libro implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    @Basic
    public String titulo;
    public String clasificacion;
    public int numero;
    @ManyToMany(mappedBy="librosescritos")
    private List<Autor> creadores;

    public Libro() {
    }

    public Libro(int id, String titulo, String clasificacion, int numero, List<Autor> creadores) {
        this.id = id;
        this.titulo = titulo;
        this.clasificacion = clasificacion;
        this.numero = numero;
        this.creadores = creadores;
    }
    public void aniadirCreador(Autor autor){
       if (creadores == null) {
        creadores = new ArrayList<>();
    }
    creadores.add(autor);
   }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public List<Autor> getCreadores() {
        return creadores;
    }

    public void setCreadores(List<Autor> creadores) {
        this.creadores = creadores;
    }

  

    
    
}
