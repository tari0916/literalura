package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.IAutorRepository;
import com.aluracursos.literalura.repository.ILibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class LibroService {
    @Autowired
    private ILibroRepository repositoryLibro;
    @Autowired
    private IAutorRepository repositoryAutor;

    public void guardarLibroEnBd(DatosLibro libro){
        Libro l = new Libro(libro);
        Set<Autor> autores = libro.autores()
                .stream()
                .map(a -> repositoryAutor.findByNombre(a.nombre())
                        .orElseGet(() -> repositoryAutor.save(new Autor(a))))
                .collect(Collectors.toSet());
        l.setAutores(autores);
        repositoryLibro.save(l);
    }

    public List<Libro> listarLibrosGuardados() {
        return repositoryLibro.findAllByOrderByNumeroDeDescargasDesc();
    }

    public List<Autor> listarAutoresGuardados() {
        return repositoryAutor.findAll();
    }

    public void estadisticasLibrosPorIdioma(){
        List<Libro> libros = listarLibrosGuardados();
        List<String> idiomas = List.of("en","it");

        idiomas.forEach(idioma -> {
            DoubleSummaryStatistics stats = libros.stream()
                    .filter(l -> idioma.equals(l.getIdiomas()))
                    .mapToDouble(l -> 1)
                    .summaryStatistics();
            System.out.println("Idioma: "+idioma+" | Cantidad de libros: "+(int) stats.getSum());
        });
    }

    public List<Autor> obtenerAutoresVivosEn(int anio) {
        return repositoryAutor.findAutoresVivosEn(anio);
    }
}
