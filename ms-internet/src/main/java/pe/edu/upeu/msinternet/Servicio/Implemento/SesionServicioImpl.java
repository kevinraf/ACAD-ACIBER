package pe.edu.upeu.msinternet.Servicio.Implemento;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.msinternet.Dto.PagoDTO;
import pe.edu.upeu.msinternet.Dto.SesionCrearDto;
import pe.edu.upeu.msinternet.Dto.SesionDto;
import pe.edu.upeu.msinternet.Entidad.*;
import pe.edu.upeu.msinternet.Feign.PagoFeign;
import pe.edu.upeu.msinternet.Repositorio.ClienteRepositorio;
import pe.edu.upeu.msinternet.Repositorio.MaquinaRepositorio;
import pe.edu.upeu.msinternet.Repositorio.SesionRepositorio;
import pe.edu.upeu.msinternet.Servicio.SesionServicio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SesionServicioImpl implements SesionServicio {

    private final SesionRepositorio sesionRepo;
    private final MaquinaRepositorio maquinaRepo;
    private final ClienteRepositorio clienteRepo;
    private final PagoFeign pagoFeign;

    @Override
    public SesionDto iniciar(SesionCrearDto dto) {
        Maquina m = maquinaRepo.findByCodigo(dto.getCodigoMaquina()).orElseThrow();
        if (m.getEstado()!= EstadoMaquina.LIBRE) throw new IllegalStateException("Máquina no disponible");

        Cliente c = clienteRepo.findByDni(dto.getDniCliente()).orElseThrow();

        Sesion s = new Sesion();
        s.setCodigo("SES-" + UUID.randomUUID());
        s.setMaquina(m);
        s.setCliente(c);
        s.setHoraInicio(LocalDateTime.now());
        s.setMinutosAsignados(dto.getMinutosAsignados());
        s.setCostoHora(m.getCostoHora());
        s.setEstado(EstadoSesion.EN_CURSO);
        s = sesionRepo.save(s);

        m.setEstado(EstadoMaquina.OCUPADA);
        m.setUltimaActualizacion(LocalDateTime.now());

        return toDto(s);
    }

    @Override
    public SesionDto finalizar(String codigoSesion, String metodoPago) {
        Sesion s = sesionRepo.findByCodigo(codigoSesion).orElseThrow();
        if (s.getEstado()!=EstadoSesion.EN_CURSO) throw new IllegalStateException("Sesión no activa");

        s.setHoraFin(LocalDateTime.now());
        // Simple: minutosConsumidos = asignados (puedes medir real si tienes cron)
        s.setMinutosConsumidos(Math.min(s.getMinutosAsignados(), s.getMinutosAsignados()));
        BigDecimal horas = BigDecimal.valueOf(s.getMinutosConsumidos()).divide(BigDecimal.valueOf(60), 2, java.math.RoundingMode.HALF_UP);
        s.setTotalCalculado(s.getCostoHora().multiply(horas));
        s.setEstado(EstadoSesion.FINALIZADA);

        Maquina m = s.getMaquina();
        m.setEstado(EstadoMaquina.LIBRE);
        m.setUltimaActualizacion(LocalDateTime.now());

        Cliente c = s.getCliente();
        c.setHorasAcumuladas(c.getHorasAcumuladas() + (s.getMinutosConsumidos()/60));

        // Enviar a ms-pago
        PagoDTO pago = new PagoDTO();
        pago.setCodigoSesion(s.getCodigo());
        pago.setMaquinaCodigo(m.getCodigo());
        pago.setClienteDni(c.getDni());
        pago.setMonto(s.getTotalCalculado());
        pago.setMetodo(metodoPago);
        pagoFeign.registrarPago(pago);

        return toDto(s);
    }

    @Override
    public SesionDto cancelar(String codigoSesion) {
        Sesion s = sesionRepo.findByCodigo(codigoSesion).orElseThrow();
        s.setEstado(EstadoSesion.CANCELADA);
        Maquina m = s.getMaquina();
        m.setEstado(EstadoMaquina.LIBRE);
        m.setUltimaActualizacion(LocalDateTime.now());
        return toDto(s);
    }

    private SesionDto toDto(Sesion s){
        SesionDto d = new SesionDto();
        d.setId(s.getId());
        d.setCodigo(s.getCodigo());
        d.setMaquinaCodigo(s.getMaquina().getCodigo());
        d.setClienteDni(s.getCliente().getDni());
        d.setMinutosAsignados(s.getMinutosAsignados());
        d.setMinutosConsumidos(s.getMinutosConsumidos());
        d.setEstado(s.getEstado().name());
        d.setCostoHora(s.getCostoHora());
        d.setTotalCalculado(s.getTotalCalculado());
        return d;
    }
}
