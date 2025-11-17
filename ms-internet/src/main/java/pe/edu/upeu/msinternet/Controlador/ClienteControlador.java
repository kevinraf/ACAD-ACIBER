package pe.edu.upeu.msinternet.Controlador;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.msinternet.Dto.ClienteDto;
import pe.edu.upeu.msinternet.Servicio.ClienteServicio;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteControlador {
    private final ClienteServicio servicio;

    @PostMapping
    public ResponseEntity<ClienteDto> registrar(@RequestBody ClienteDto dto){
        return ResponseEntity.ok(servicio.registrar(dto));
    }

    @GetMapping("/{dni}")
    public ResponseEntity<ClienteDto> porDni(@PathVariable String dni){
        return ResponseEntity.ok(servicio.buscarPorDni(dni));
    }

    @GetMapping
    public ResponseEntity<List<ClienteDto>> listar(){
        return ResponseEntity.ok(servicio.listar());
    }
}
