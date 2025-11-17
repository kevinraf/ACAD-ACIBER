package pe.edu.upeu.mspago.Servicio.Implementos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.mspago.DTOs.PagoDTO;
import pe.edu.upeu.mspago.DTOs.PagoListDTO;
import pe.edu.upeu.mspago.DTOs.PagoRespDTO;
import pe.edu.upeu.mspago.Entidad.EstadoPago;
import pe.edu.upeu.mspago.Entidad.MetodoPago;
import pe.edu.upeu.mspago.Entidad.Pago;
import pe.edu.upeu.mspago.Repositorio.MetodoPagoRepositorio;
import pe.edu.upeu.mspago.Repositorio.PagoRepositorio;
import pe.edu.upeu.mspago.Servicio.PagoServicio;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoServicioImpl implements PagoServicio {

    private final PagoRepositorio pagoRepo;
    private final MetodoPagoRepositorio metodoRepo;

    @Override
    public PagoRespDTO registrar(PagoDTO dto) {
        MetodoPago mp = metodoRepo.findByNombre(dto.getMetodo())
                .orElseThrow(() -> new IllegalArgumentException("Método de pago no disponible: "+dto.getMetodo()));

        Pago p = new Pago();
        p.setCodigoSesion(dto.getCodigoSesion());
        p.setMaquinaCodigo(dto.getMaquinaCodigo());
        p.setClienteDni(dto.getClienteDni());
        p.setMonto(dto.getMonto());
        p.setMetodoPago(mp);
        p.setEstado(EstadoPago.PENDIENTE);

        p = pagoRepo.save(p);
        return toResp(p);
    }

    @Override
    public PagoRespDTO confirmar(Long pagoId) {
        Pago p = pagoRepo.findById(pagoId).orElseThrow();
        p.setEstado(EstadoPago.PAGADO);
        return toResp(p);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PagoListDTO> listar() {
        return pagoRepo.findAll().stream().map(this::toListDto).toList();
    }

    private PagoRespDTO toResp(Pago p){
        PagoRespDTO r = new PagoRespDTO();
        r.setId(p.getId());
        r.setCodigoSesion(p.getCodigoSesion());
        r.setEstado(p.getEstado().name());
        return r;
    }

    private PagoListDTO toListDto(Pago p){
        PagoListDTO d = new PagoListDTO();
        d.setId(p.getId());
        d.setCodigoSesion(p.getCodigoSesion());
        d.setMaquinaCodigo(p.getMaquinaCodigo());
        d.setClienteDni(p.getClienteDni());
        d.setMonto(p.getMonto());
        d.setMetodoPago(p.getMetodoPago()!=null ? p.getMetodoPago().getNombre() : null);
        d.setEstado(p.getEstado().name());
        d.setFechaPago(p.getFechaPago());
        d.setObservacion(p.getObservacion());
        return d;
    }
}