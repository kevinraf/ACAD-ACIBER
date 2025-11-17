package pe.edu.upeu.msinternet.Servicio;

import pe.edu.upeu.msinternet.Dto.ClienteDto;

import java.util.List;

public interface ClienteServicio {
    ClienteDto registrar(ClienteDto dto);
    ClienteDto buscarPorDni(String dni);
    List<ClienteDto> listar();
}