package pe.edu.upeu.mspago.DTOs;

import lombok.Data;
import java.math.BigDecimal;


@Data
public class PagoDTO {
    private String codigoSesion;
    private String maquinaCodigo;
    private String clienteDni;
    private BigDecimal monto;
    private String metodo; // nombre del método de pago (EFECTIVO, YAPE, etc.)
}