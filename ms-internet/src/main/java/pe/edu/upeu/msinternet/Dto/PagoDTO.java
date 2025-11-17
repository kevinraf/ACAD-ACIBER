package pe.edu.upeu.msinternet.Dto;

import lombok.Data;
import java.math.BigDecimal;

/** DTO para Feign hacia ms-pago */
@Data
public class PagoDTO {
    private String codigoSesion;
    private String maquinaCodigo;
    private String clienteDni;
    private BigDecimal monto;
    private String metodo; // EFECTIVO/YAPE/...
}