package com.aluracursos.literalura.model;

import jakarta.persistence.*;

import java.util.HashSet;

import java.util.Set;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    private String idiomas;
    private Double numeroDeDescargas;
    @ManyToMany
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores = new HashSet<>();

    public Libro(){}

    public Libro(DatosLibro datosLibros) {
        this.titulo = datosLibros.titulo();
        this.idiomas = datosLibros.idiomas().stream()
                .findFirst()
                .orElse(null);
        this.numeroDeDescargas = datosLibros.numeroDeDescargas();
    }

    @Override
    public String toString() {
        return "Titulo: "+titulo+
                ", Idiomas: "+idiomas+
                ", Descargas: "+numeroDeDescargas;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public Set<Autor> getAutores() {
        return autores;
    }

    public void agregarAutor(Autor autor){
        this.autores.add(autor);
        autor.getLibros().add(this);
    }

    public void removerAutor(Autor autor){
        this.autores.add(autor);
        autor.getLibros().remove(this);
    }

    public void setAutores(Set<Autor> autores) {
        this.autores.clear();
        if (autores!=null){
            autores.forEach(this::agregarAutor);
        }
    }
}
