package pe.edu.upeu.msinternet.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.msinternet.Entidad.Cliente;

import java.util.Optional;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByDni(String dni);
}