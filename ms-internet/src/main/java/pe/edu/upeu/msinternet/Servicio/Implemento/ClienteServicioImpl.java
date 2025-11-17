package pe.edu.upeu.msinternet.Servicio.Implemento;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.msinternet.Dto.ClienteDto;
import pe.edu.upeu.msinternet.Entidad.Cliente;
import pe.edu.upeu.msinternet.Repositorio.ClienteRepositorio;
import pe.edu.upeu.msinternet.Servicio.ClienteServicio;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServicioImpl implements ClienteServicio {

    private final ClienteRepositorio repo;

    @Override
    public ClienteDto registrar(ClienteDto dto) {
        Cliente c = repo.findByDni(dto.getDni()).orElseGet(Cliente::new);
        c.setDni(dto.getDni());
        c.setNombreCompleto(dto.getNombreCompleto());
        c.setTelefono(dto.getTelefono());
        c.setCorreo(dto.getCorreo());
        c = repo.save(c);
        return toDto(c);
    }

    @Override
    public ClienteDto buscarPorDni(String dni) {
        return repo.findByDni(dni).map(this::toDto).orElseThrow();
    }

    @Override
    public List<ClienteDto> listar() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    private ClienteDto toDto(Cliente c){
        ClienteDto d = new ClienteDto();
        d.setId(c.getId());
        d.setDni(c.getDni());
        d.setNombreCompleto(c.getNombreCompleto());
        d.setTelefono(c.getTelefono());
        d.setCorreo(c.getCorreo());
        d.setHorasAcumuladas(c.getHorasAcumuladas());
        return d;
    }
}
