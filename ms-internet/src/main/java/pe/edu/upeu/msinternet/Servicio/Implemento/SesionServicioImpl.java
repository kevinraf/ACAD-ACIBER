package pe.edu.upeu.msinternet.Servicio.Implemento;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.msinternet.Dto.PagoDTO;
import pe.edu.upeu.msinternet.Dto.PagoRespDTO;
import pe.edu.upeu.msinternet.Dto.SesionCrearDto;
import pe.edu.upeu.msinternet.Dto.SesionDto;
import pe.edu.upeu.msinternet.Entidad.*;
import pe.edu.upeu.msinternet.Feign.PagoFeign;
import pe.edu.upeu.msinternet.Repositorio.ClienteRepositorio;
import pe.edu.upeu.msinternet.Repositorio.MaquinaRepositorio;
import pe.edu.upeu.msinternet.Repositorio.SesionRepositorio;
import pe.edu.upeu.msinternet.Servicio.SesionServicio;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
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
        if (m.getEstado() != EstadoMaquina.LIBRE) {
            throw new IllegalStateException("Máquina no disponible");
        }

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
        if (s.getEstado() != EstadoSesion.EN_CURSO) {
            throw new IllegalStateException("Sesión no activa");
        }

        s.setHoraFin(LocalDateTime.now());

        // Calcular minutos consumidos reales desde horaInicio
        long minutosReales = Duration.between(s.getHoraInicio(), s.getHoraFin()).toMinutes();
        int consumidos = (int) Math.min(minutosReales, s.getMinutosAsignados());
        if (consumidos < 0) consumidos = 0;

        s.setMinutosConsumidos(consumidos);

        BigDecimal horas = BigDecimal
                .valueOf(consumidos)
                .divide(BigDecimal.valueOf(60), 2, java.math.RoundingMode.HALF_UP);

        s.setTotalCalculado(s.getCostoHora().multiply(horas));
        s.setEstado(EstadoSesion.FINALIZADA);

        // Liberar máquina
        Maquina m = s.getMaquina();
        m.setEstado(EstadoMaquina.LIBRE);
        m.setUltimaActualizacion(LocalDateTime.now());

        // Actualizar horas acumuladas del cliente
        Cliente c = s.getCliente();
        int horasEnteras = s.getMinutosConsumidos() / 60;
        c.setHorasAcumuladas(c.getHorasAcumuladas() + horasEnteras);

        // Registrar pago en ms-pago (queda PENDIENTE)
        PagoDTO pago = new PagoDTO();
        pago.setCodigoSesion(s.getCodigo());
        pago.setMaquinaCodigo(m.getCodigo());
        pago.setClienteDni(c.getDni());
        pago.setMonto(s.getTotalCalculado());
        pago.setMetodo(metodoPago);

        PagoRespDTO pagoResp = pagoFeign.registrarPago(pago);

        // Guardar info del pago en la sesión
        if (pagoResp != null) {
            s.setPagoId(pagoResp.getId());
            s.setEstadoPago(pagoResp.getEstado()); // normalmente "PENDIENTE"
        }

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

    @Transactional(readOnly = true)
    @Override
    public List<SesionDto> listar() {
        return sesionRepo.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private SesionDto toDto(Sesion s) {
        SesionDto d = new SesionDto();
        d.setId(s.getId());
        d.setCodigo(s.getCodigo());

        d.setMaquinaCodigo(s.getMaquina().getCodigo());
        d.setMaquinaNombre(s.getMaquina().getNombre());

        d.setClienteDni(s.getCliente().getDni());
        d.setClienteNombre(s.getCliente().getNombreCompleto());

        d.setMinutosAsignados(s.getMinutosAsignados());
        d.setCostoHora(s.getCostoHora());
        d.setTotalCalculado(s.getTotalCalculado());
        d.setEstado(s.getEstado().name());

        // Tiempo consumido / restante para el panel
        if (s.getEstado() == EstadoSesion.EN_CURSO && s.getHoraInicio() != null) {
            long minutosReales = Duration.between(s.getHoraInicio(), LocalDateTime.now()).toMinutes();
            int consumidos = (int) Math.min(minutosReales, s.getMinutosAsignados());
            if (consumidos < 0) consumidos = 0;

            int restantes = s.getMinutosAsignados() - consumidos;
            if (restantes < 0) restantes = 0;

            d.setMinutosConsumidos(consumidos);
            d.setMinutosRestantes(restantes);
        } else {
            d.setMinutosConsumidos(s.getMinutosConsumidos());
            d.setMinutosRestantes(0);
        }

        // Info del pago
        d.setPagoId(s.getPagoId());
        d.setEstadoPago(s.getEstadoPago());
        d.setPagado("PAGADO".equalsIgnoreCase(s.getEstadoPago()));

        return d;
    }
}
