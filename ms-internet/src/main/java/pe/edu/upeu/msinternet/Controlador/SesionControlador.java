package pe.edu.upeu.msinternet.Controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.msinternet.Dto.SesionCrearDto;
import pe.edu.upeu.msinternet.Dto.SesionDto;
import pe.edu.upeu.msinternet.Servicio.SesionServicio;

@RestController
@RequestMapping("/sesiones")
@RequiredArgsConstructor
public class SesionControlador {
    private final SesionServicio servicio;

    @PostMapping("/iniciar")
    public ResponseEntity<SesionDto> iniciar(@RequestBody SesionCrearDto dto){
        return ResponseEntity.ok(servicio.iniciar(dto));
    }

    @PutMapping("/{codigo}/finalizar")
    public ResponseEntity<SesionDto> finalizar(@PathVariable String codigo, @RequestParam String metodoPago){
        return ResponseEntity.ok(servicio.finalizar(codigo, metodoPago));
    }

    @PutMapping("/{codigo}/cancelar")
    public ResponseEntity<SesionDto> cancelar(@PathVariable String codigo){
        return ResponseEntity.ok(servicio.cancelar(codigo));
    }
}
