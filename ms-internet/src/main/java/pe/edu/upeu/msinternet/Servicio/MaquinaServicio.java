package pe.edu.upeu.msinternet.Servicio;


import pe.edu.upeu.msinternet.Dto.MaquinaDto;

import java.util.List;

public interface MaquinaServicio {
    MaquinaDto crear(MaquinaDto dto);
    MaquinaDto actualizar(String codigo, MaquinaDto dto);
    MaquinaDto buscarPorCodigo(String codigo);
    List<MaquinaDto> listar();
    void bloquear(String codigo);
    void liberar(String codigo);
}