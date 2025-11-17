package pe.edu.upeu.msinternet.Dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ClienteDto {
    private Long id;
    private String dni;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private Integer horasAcumuladas;
}