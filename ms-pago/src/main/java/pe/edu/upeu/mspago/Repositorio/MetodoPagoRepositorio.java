package pe.edu.upeu.mspago.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.mspago.Entidad.MetodoPago;


import java.util.Optional;

public interface MetodoPagoRepositorio extends JpaRepository<MetodoPago, Long> {
    Optional<MetodoPago> findByNombre(String nombre);
}