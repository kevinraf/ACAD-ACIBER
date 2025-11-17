package pe.edu.upeu.msinternet.Dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SesionCrearDto {
    private String codigoMaquina;
    private String dniCliente;
    private Integer minutosAsignados;
}