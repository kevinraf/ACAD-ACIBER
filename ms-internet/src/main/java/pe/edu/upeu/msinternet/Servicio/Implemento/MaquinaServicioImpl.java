package pe.edu.upeu.msinternet.Servicio.Implemento;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.msinternet.Dto.MaquinaDto;
import pe.edu.upeu.msinternet.Entidad.EstadoMaquina;
import pe.edu.upeu.msinternet.Entidad.Maquina;
import pe.edu.upeu.msinternet.Repositorio.MaquinaRepositorio;
import pe.edu.upeu.msinternet.Servicio.MaquinaServicio;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaquinaServicioImpl implements MaquinaServicio {

    private final MaquinaRepositorio repo;

    @Override
    public MaquinaDto crear(MaquinaDto dto) {
        Maquina m = new Maquina();
        m.setCodigo(dto.getCodigo());
        m.setNombre(dto.getNombre());
        m.setEstado(EstadoMaquina.LIBRE);
        m.setCostoHora(dto.getCostoHora());
        m.setDescripcion(dto.getDescripcion());
        m.setUltimaActualizacion(LocalDateTime.now());
        m = repo.save(m);
        return toDto(m);
    }

    @Override
    public MaquinaDto actualizar(String codigo, MaquinaDto dto) {
        Maquina m = repo.findByCodigo(codigo).orElseThrow();
        if (dto.getNombre()!=null) m.setNombre(dto.getNombre());
        if (dto.getCostoHora()!=null) m.setCostoHora(dto.getCostoHora());
        if (dto.getDescripcion()!=null) m.setDescripcion(dto.getDescripcion());
        m.setUltimaActualizacion(LocalDateTime.now());
        return toDto(repo.save(m));
    }

    @Override
    public MaquinaDto buscarPorCodigo(String codigo) {
        return repo.findByCodigo(codigo).map(this::toDto).orElseThrow();
    }

    @Override
    public List<MaquinaDto> listar() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public void bloquear(String codigo) {
        Maquina m = repo.findByCodigo(codigo).orElseThrow();
        m.setEstado(EstadoMaquina.BLOQUEADA);
        m.setUltimaActualizacion(LocalDateTime.now());
    }

    @Override
    public void liberar(String codigo) {
        Maquina m = repo.findByCodigo(codigo).orElseThrow();
        m.setEstado(EstadoMaquina.LIBRE);
        m.setUltimaActualizacion(LocalDateTime.now());
    }

    private MaquinaDto toDto(Maquina m){
        MaquinaDto d = new MaquinaDto();
        d.setId(m.getId());
        d.setCodigo(m.getCodigo());
        d.setNombre(m.getNombre());
        d.setEstado(m.getEstado().name());
        d.setCostoHora(m.getCostoHora());
        d.setDescripcion(m.getDescripcion());
        return d;
    }
}
