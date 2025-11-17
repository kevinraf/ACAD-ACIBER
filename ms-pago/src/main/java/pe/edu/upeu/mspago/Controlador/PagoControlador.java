package pe.edu.upeu.mspago.Controlador;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.mspago.DTOs.PagoDTO;
import pe.edu.upeu.mspago.DTOs.PagoListDTO;
import pe.edu.upeu.mspago.DTOs.PagoRespDTO;
import pe.edu.upeu.mspago.Entidad.Pago;
import pe.edu.upeu.mspago.Servicio.PagoServicio;

import java.util.List;

@RestController
@RequestMapping("/pagos")
@Tag(name = "Pagos", description = "Gestión de pagos")
@RequiredArgsConstructor
public class PagoControlador {
    private final PagoServicio servicio;

    @Operation(summary = "Registrar un pago")
    @PostMapping
    public ResponseEntity<PagoRespDTO> registrar(@RequestBody PagoDTO dto){
        return ResponseEntity.ok(servicio.registrar(dto));
    }
    @Operation(summary = "Confirmar pago")
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<PagoRespDTO> confirmar(@PathVariable Long id){
        return ResponseEntity.ok(servicio.confirmar(id));
    }
    @Operation(summary = "Listar pagos")
    @GetMapping
    public ResponseEntity<List<PagoListDTO>> listar(){
        return ResponseEntity.ok(servicio.listar());
    }
}