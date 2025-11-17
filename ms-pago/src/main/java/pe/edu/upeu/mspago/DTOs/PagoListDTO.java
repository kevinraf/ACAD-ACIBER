package pe.edu.upeu.mspago.DTOs;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagoListDTO {
    private Long id;
    private String codigoSesion;
    private String maquinaCodigo;
    private String clienteDni;
    private BigDecimal monto;
    private String metodoPago;   // nombre
    private String estado;       // enum -> string
    private LocalDateTime fechaPago;
    private String observacion;
}