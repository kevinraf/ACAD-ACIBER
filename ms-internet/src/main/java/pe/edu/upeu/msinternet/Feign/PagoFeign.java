package pe.edu.upeu.msinternet.Feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import pe.edu.upeu.msinternet.Dto.PagoDTO;


@FeignClient(name = "ms-pago", path = "/pagos")
public interface PagoFeign {

    @PostMapping
    @CircuitBreaker(name="pagoRegistrarCB", fallbackMethod = "fallbackRegistrar")
    PagoDTO registrarPago(PagoDTO dto);

    default PagoDTO fallbackRegistrar(PagoDTO dto, Exception e) {
        PagoDTO r = new PagoDTO();
        r.setCodigoSesion(dto.getCodigoSesion());
        r.setMaquinaCodigo(dto.getMaquinaCodigo());
        r.setClienteDni(dto.getClienteDni());
        r.setMetodo("N/A");
        r.setMonto(dto.getMonto());
        return r; // Marca lógica local si se desea
    }
}

