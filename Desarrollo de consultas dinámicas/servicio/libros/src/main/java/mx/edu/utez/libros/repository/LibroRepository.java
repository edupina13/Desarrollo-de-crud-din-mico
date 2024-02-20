package mx.edu.utez.libros.repository;

import mx.edu.utez.libros.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByNombreContaining(String nombre);
    List<Libro> findByNombreAutorContaining(String nombreAutor);
    List<Libro> findByGenero(String genero);
    List<Libro> findByFechaPublicacionOrderByFechaPublicacionDesc(Date fechaPublicacion);
}
