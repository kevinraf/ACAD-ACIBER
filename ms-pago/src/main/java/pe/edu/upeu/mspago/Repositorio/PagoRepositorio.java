package pe.edu.upeu.mspago.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.mspago.Entidad.Pago;


public interface PagoRepositorio extends JpaRepository<Pago, Long> {
}