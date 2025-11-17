package pe.edu.upeu.mspago.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.mspago.Entidad.Comprobante;

public interface ComprobanteRepositorio extends JpaRepository<Comprobante, Long> {
}