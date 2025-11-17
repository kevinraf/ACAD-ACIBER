package pe.edu.upeu.msinternet.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.msinternet.Entidad.Sesion;

import java.util.Optional;

public interface SesionRepositorio extends JpaRepository<Sesion, Long> {
    Optional<Sesion> findByCodigo(String codigo);
}