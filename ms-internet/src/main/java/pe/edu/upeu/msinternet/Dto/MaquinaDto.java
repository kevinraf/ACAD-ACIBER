package pe.edu.upeu.msinternet.Dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MaquinaDto {
    private Long id;
    private String codigo;
    private String nombre;
    private String estado; // LIBRE/OCUPADA/etc
    private BigDecimal costoHora;
    private String descripcion;
}