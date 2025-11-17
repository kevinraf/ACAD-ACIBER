package pe.edu.upeu.msinternet.Servicio;

import pe.edu.upeu.msinternet.Dto.SesionCrearDto;
import pe.edu.upeu.msinternet.Dto.SesionDto;

import java.util.List;

public interface SesionServicio {
    SesionDto iniciar(SesionCrearDto dto);
    SesionDto finalizar(String codigoSesion, String metodoPago); // EFECTIVO/YAPE...
    SesionDto cancelar(String codigoSesion);

    /** Listar todas las sesiones para el panel del frontend */
    List<SesionDto> listar();

    /** 🔹 Nuevo: confirmar el pago asociado a la sesión */
    SesionDto confirmarPagoSesion(String codigoSesion);
}
