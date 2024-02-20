package mx.edu.utez.libros.controller;

import mx.edu.utez.libros.model.Libro;
import mx.edu.utez.libros.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/libros")
@CrossOrigin(origins = "http://localhost:5173")
public class LibroController {
    @Autowired
    private LibroService libroService;

    @GetMapping
    public List<Libro> obtenerTodosLosLibros() {
        return libroService.obtenerTodosLosLibros();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerLibroPorId(@PathVariable Long id) {
        Optional<Libro> libro = libroService.obtenerLibroPorId(id);
        return libro.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Libro> crearLibro(@RequestBody Libro libro) {
        return new ResponseEntity<>(libroService.guardarOActualizarLibro(libro), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable Long id, @RequestBody Libro libro) {
        libro.setId(id);
        return new ResponseEntity<>(libroService.guardarOActualizarLibro(libro), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        libroService.eliminarLibroPorId(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/buscar-por-nombre")
    public List<Libro> buscarPorNombre(@RequestParam String nombre) {
        return libroService.buscarPorNombre(nombre);
    }

    @GetMapping("/buscar-por-nombre-autor")
    public List<Libro> buscarPorNombreAutor(@RequestParam String nombreAutor) {
        return libroService.buscarPorNombreAutor(nombreAutor);
    }

    @GetMapping("/buscar-por-genero")
    public List<Libro> buscarPorGenero(@RequestParam String genero) {
        return libroService.buscarPorGenero(genero);
    }

    @GetMapping("/buscar-por-fecha-publicacion-desc")
    public List<Libro> buscarPorFechaPublicacionDesc(@RequestParam Date fechaPublicacion) {
        return libroService.buscarPorFechaPublicacionDesc(fechaPublicacion);
    }


}
