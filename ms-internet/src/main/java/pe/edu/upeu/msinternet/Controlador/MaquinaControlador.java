package pe.edu.upeu.msinternet.Controlador;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.msinternet.Dto.MaquinaDto;
import pe.edu.upeu.msinternet.Servicio.MaquinaServicio;


import java.util.List;

@RestController
@RequestMapping("/maquinas")
@RequiredArgsConstructor
public class MaquinaControlador {
    private final MaquinaServicio servicio;

    @PostMapping
    public ResponseEntity<MaquinaDto> crear(@RequestBody MaquinaDto dto){
        return ResponseEntity.ok(servicio.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<MaquinaDto>> listar(){
        return ResponseEntity.ok(servicio.listar());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<MaquinaDto> buscar(@PathVariable String codigo){
        return ResponseEntity.ok(servicio.buscarPorCodigo(codigo));
    }

    @PutMapping("/{codigo}/bloquear")
    public ResponseEntity<Void> bloquear(@PathVariable String codigo){
        servicio.bloquear(codigo); return ResponseEntity.ok().build();
    }

    @PutMapping("/{codigo}/liberar")
    public ResponseEntity<Void> liberar(@PathVariable String codigo){
        servicio.liberar(codigo); return ResponseEntity.ok().build();
    }
}
