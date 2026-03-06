package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAutorRepository extends JpaRepository<Autor,Long> {
    Optional<Autor> findByNombre(String nombre);
    @Query("""
    SELECT a FROM Autor a
    WHERE CAST(a.fechaDeNacimiento AS int) <= :anio
      AND (a.fechaDeFallecimiento IS NULL OR CAST(a.fechaDeFallecimiento AS int) >= :anio)
    """)
    List<Autor> findAutoresVivosEn(@Param("anio") int anio);
}
