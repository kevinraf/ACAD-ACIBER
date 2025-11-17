package pe.edu.upeu.msinternet.Feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.msinternet.Dto.PagoDTO;
import pe.edu.upeu.msinternet.Dto.PagoRespDTO;

@FeignClient(name = "ms-pago", path = "/pagos")
public interface PagoFeign {

    @PostMapping
    @CircuitBreaker(name="pagoRegistrarCB", fallbackMethod = "fallbackRegistrar")
    PagoRespDTO registrarPago(@RequestBody PagoDTO dto);

    @PutMapping("/{id}/confirmar")
    @CircuitBreaker(name="pagoConfirmarCB", fallbackMethod = "fallbackConfirmar")
    PagoRespDTO confirmarPago(@PathVariable("id") Long id);

    // --- fallbacks ---

    default PagoRespDTO fallbackRegistrar(PagoDTO dto, Exception e) {
        PagoRespDTO r = new PagoRespDTO();
        r.setCodigoSesion(dto.getCodigoSesion());
        r.setEstado("PENDIENTE_INTEGRACION");
        return r;
    }

    default PagoRespDTO fallbackConfirmar(Long id, Exception e) {
        PagoRespDTO r = new PagoRespDTO();
        r.setId(id);
        r.setEstado("PENDIENTE_CONFIRMACION");
        return r;
    }
}
