package pe.edu.upeu.msinternet.Dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SesionDto {
    private Long id;
    private String codigo;

    private String maquinaCodigo;
    private String maquinaNombre;

    private String clienteDni;
    private String clienteNombre;

    private Integer minutosAsignados;
    private Integer minutosConsumidos;
    private Integer minutosRestantes;

    private String estado;
    private BigDecimal costoHora;
    private BigDecimal totalCalculado;

    // >>> NUEVO: info del pago <<<
    private Long pagoId;
    private String estadoPago; // PENDIENTE / PAGADO / ANULADO
    private Boolean pagado;    // true si estadoPago = PAGADO
}
