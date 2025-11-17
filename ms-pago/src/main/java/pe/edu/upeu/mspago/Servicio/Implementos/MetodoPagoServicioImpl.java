package pe.edu.upeu.mspago.Servicio.Implementos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.mspago.Entidad.MetodoPago;
import pe.edu.upeu.mspago.Repositorio.MetodoPagoRepositorio;
import pe.edu.upeu.mspago.Servicio.MetodoPagoServicio;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MetodoPagoServicioImpl implements MetodoPagoServicio {

    private final MetodoPagoRepositorio repo;

    @Override
    public MetodoPago crear(MetodoPago mp) {
        return repo.save(mp);
    }

    @Override
    public MetodoPago buscarPorNombre(String nombre) {
        return repo.findByNombre(nombre).orElseThrow();
    }

    @Override
    public List<MetodoPago> listar() {
        return repo.findAll();
    }
}