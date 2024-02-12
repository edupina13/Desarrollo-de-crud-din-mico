package mx.edu.utez.libros.service;

import mx.edu.utez.libros.model.Libro;
import mx.edu.utez.libros.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {
    @Autowired
    private LibroRepository libroRepository;

    public List<Libro> obtenerTodosLosLibros() {
        return libroRepository.findAll();
    }

    public Optional<Libro> obtenerLibroPorId(Long id) {
        return libroRepository.findById(id);
    }

    public Libro guardarOActualizarLibro(Libro libro) {
        return libroRepository.save(libro);
    }

    public void eliminarLibroPorId(Long id) {
        libroRepository.deleteById(id);
    }
}
