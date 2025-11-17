package pe.edu.upeu.msinternet.Dto;

import lombok.Data;

@Data
public class PagoRespDTO {
    private Long id;
    private String codigoSesion;
    private String estado; // PENDIENTE / PAGADO / ANULADO
}
