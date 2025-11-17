package pe.edu.upeu.msinternet.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.msinternet.Entidad.Maquina;


import java.util.Optional;

public interface MaquinaRepositorio extends JpaRepository<Maquina, Long> {
    Optional<Maquina> findByCodigo(String codigo);
}