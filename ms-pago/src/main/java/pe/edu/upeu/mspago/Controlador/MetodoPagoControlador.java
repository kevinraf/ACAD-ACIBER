package pe.edu.upeu.mspago.Controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.mspago.Entidad.MetodoPago;
import pe.edu.upeu.mspago.Servicio.MetodoPagoServicio;


import java.util.List;

@RestController
@RequestMapping("/metodos-pago")
@RequiredArgsConstructor
public class MetodoPagoControlador {
    private final MetodoPagoServicio servicio;

    @PostMapping
    public ResponseEntity<MetodoPago> crear(@RequestBody MetodoPago mp){
        return ResponseEntity.ok(servicio.crear(mp));
    }

    @GetMapping
    public ResponseEntity<List<MetodoPago>> listar(){
        return ResponseEntity.ok(servicio.listar());
    }
}