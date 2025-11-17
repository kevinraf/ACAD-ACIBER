package pe.edu.upeu.mspago.DTOs;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PagoRespDTO {
    private Long id;
    private String codigoSesion;
    private String estado; // PENDIENTE/PAGADO/ANULADO
}