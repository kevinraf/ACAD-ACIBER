package pe.edu.upeu.mspago.Servicio;



import pe.edu.upeu.mspago.DTOs.PagoDTO;
import pe.edu.upeu.mspago.DTOs.PagoListDTO;
import pe.edu.upeu.mspago.DTOs.PagoRespDTO;
import pe.edu.upeu.mspago.Entidad.Pago;

import java.util.List;

public interface PagoServicio {
    PagoRespDTO registrar(PagoDTO dto);
    PagoRespDTO confirmar(Long pagoId);
    List<PagoListDTO> listar();
}