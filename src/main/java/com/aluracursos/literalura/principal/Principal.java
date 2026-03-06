package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.model.DatosAutor;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import com.aluracursos.literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal implements CommandLineRunner {

    private static final String URL_BASE = "https://gutendex.com/books/";

    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    @Autowired
    private LibroService service;

    @Override
    public void run(String... args) throws Exception {
        muestraMenu();
    }

    public void muestraMenu(){
        var json = consumoAPI.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(json, Datos.class);
        int opcion = 0;

        while (opcion!=10){
            System.out.println("""
                    ----- FUNCIONES API ----
                    1 - Listar libros disponibles en la API
                    2 - Buscar libro en la API
                    3 - Buscar libro en la API y guardar en base de datos
                    4 - Listar autores disponibles en la API
                    5 - Buscar autores vivos en un año en especifico
                    
                    ---- FUNCIONES BASE DE DATOS ----
                    6 - Listar libros guardados en la base de datos
                    7 - Listar autores guardados en la base de datos
                    8 - Cantidad de libros dependiendo el idioma
                    9 - Listar autores vivos en determinado año
                    
                    10 - Salir
                    """);

            opcion = teclado.nextInt();

            switch (opcion) {
                case 1 -> listarLibrosAPI(datos);
                case 2 -> buscarLibroAPI();
                case 3 -> buscarLibro();
                case 4 -> listarAutoresAPI(datos);
                case 5 -> autoresVivosPorAnio(datos);
                case 6 -> listarLibrosBD();
                case 7 -> listarAutoresBD();
                case 8 -> estadisticasIdiomas();
                case 9 -> listarAutoresBDanio();
                case 10 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida");
            }
        }
    }

    private void listarLibrosAPI(Datos datos){
        System.out.println("Los libros disponibles son:");
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibro::numeroDeDescargas).reversed())
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);
    }

    private Optional<DatosLibro> buscarLibroAPI(){
        System.out.println("Escribe el título del libro:");
        String titulo = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + titulo.replace(" ","+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        return datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(titulo.toUpperCase()))
                .findFirst();
    }
    private void guardarLibro(DatosLibro datosLibro){
        try {
            service.guardarLibroEnBd(datosLibro);
            System.out.println("Guardado con éxito...");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Ya existía. No guardado");
        } catch (Exception e) {
            System.out.println("No se guardó.");
        }
    }

    private void buscarLibro() {
        Optional<DatosLibro> libroBuscado = buscarLibroAPI();

        if (libroBuscado.isPresent()) {
            System.out.println("Libro encontrado:");
            System.out.println(libroBuscado.get());

            guardarLibro(libroBuscado.get());
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void listarAutoresAPI(Datos datos){
        System.out.println("Los autores disponibles son:");
        datos.resultados().stream()
                .flatMap(l -> l.autores().stream())
                .map(DatosAutor::nombre)
                .distinct()
                .forEach(System.out::println);
    }

    private void autoresVivosPorAnio(Datos datos){
        System.out.println("Ingresa el año que deseas buscar: ");
        var anio = teclado.nextInt();
        System.out.println("Autores vivos en el año "+anio);
        System.out.println("Los autores disponibles son:");
        datos.resultados().stream()
                .flatMap(l -> l.autores().stream())
                .distinct()
                .filter(a -> a.fechaDeNacimiento() != null &&
                        Integer.parseInt(a.fechaDeNacimiento())<= anio &&
                        (a.fechaDeFallecimiento()==null||Integer.parseInt(a.fechaDeFallecimiento())>=anio))
                .map(DatosAutor::nombre)
                .forEach(System.out::println);
    }

    private void listarLibrosBD() {
        var libros = service.listarLibrosGuardados();

        if (libros.isEmpty()) {
            System.out.println("No hay libros guardados en la base de datos.");
            return;
        }
        System.out.println("Libros guardados en la base de datos:");
        libros.forEach(l -> System.out.println(
                "Título: " + l.getTitulo() + " | Descargas: " + l.getNumeroDeDescargas()));
    }

    private void listarAutoresBD() {
        var autores = service.listarAutoresGuardados();

        if (autores.isEmpty()) {
            System.out.println("No hay autores guardados en la base de datos.");
            return;
        }
        System.out.println("Autores guardados en la base de datos:");
        autores.forEach(a -> System.out.println(
                "Autor: " + a.getNombre()
        ));
    }

    private void estadisticasIdiomas(){
        System.out.println("Las cantidad de libros disponibles en estos idiomas son:");
        service.estadisticasLibrosPorIdioma();
    }

    private void listarAutoresBDanio() {
        int anio = 0;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.println("Ingresa el año que deseas buscar:");
                anio = teclado.nextInt();
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Año inválido...");
            } catch (Exception e){
                System.out.println("Intenta otro año...");
            }
        }

        var autores = service.obtenerAutoresVivosEn(anio);

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en " + anio);
            return;
        }

        System.out.println("Autores vivos en " + anio + ":");
        autores.forEach(a -> System.out.println(" --- " + a.getNombre()));
    }

}
