package pe.edu.upeu.msinternet.Dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SesionDto {
    private Long id;
    private String codigo;
    private String maquinaCodigo;
    private String clienteDni;
    private Integer minutosAsignados;
    private Integer minutosConsumidos;
    private String estado;
    private BigDecimal costoHora;
    private BigDecimal totalCalculado;
}