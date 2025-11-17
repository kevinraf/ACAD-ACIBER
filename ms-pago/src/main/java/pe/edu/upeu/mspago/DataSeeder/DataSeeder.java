package pe.edu.upeu.mspago.DataSeeder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.mspago.Entidad.*;
import pe.edu.upeu.mspago.Repositorio.ComprobanteRepositorio;
import pe.edu.upeu.mspago.Repositorio.MetodoPagoRepositorio;
import pe.edu.upeu.mspago.Repositorio.PagoRepositorio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final MetodoPagoRepositorio metodoRepo;
    private final PagoRepositorio pagoRepo;
    private final ComprobanteRepositorio compRepo;

    @Override
    @Transactional
    public void run(String... args) {
        seedMetodosPago();
        seedPagosYComprobantes();
    }

    private void seedMetodosPago() {
        if (metodoRepo.count() > 0) return;

        metodoRepo.save(build("EFECTIVO", "Pago en efectivo"));
        metodoRepo.save(build("YAPE", "Pago con Yape"));
        metodoRepo.save(build("PLIN", "Pago con Plin"));
        metodoRepo.save(build("TARJETA", "Tarjeta débito/crédito"));
        metodoRepo.save(build("TRANSFERENCIA", "Transferencia bancaria"));
    }

    private MetodoPago build(String nombre, String desc) {
        MetodoPago mp = new MetodoPago();
        mp.setNombre(nombre);
        mp.setDescripcion(desc);
        mp.setActivo(true);
        return mp;
    }

    private void seedPagosYComprobantes() {
        if (pagoRepo.count() > 0) return;

        // Método por defecto
        MetodoPago efectivo = metodoRepo.findByNombre("EFECTIVO").orElseThrow();
        MetodoPago yape = metodoRepo.findByNombre("YAPE").orElseThrow();

        // Pago de la sesión finalizada en el seeder de ms-internet: SES-SEED-001 (PC02, DNI 87654321)
        Pago p1 = new Pago();
        p1.setCodigoSesion("SES-SEED-001");
        p1.setMaquinaCodigo("PC02");
        p1.setClienteDni("87654321");
        p1.setMonto(new BigDecimal("3.75")); // ejemplo acorde al cálculo (90min a 2.50/h => 3.75)
        p1.setMetodoPago(efectivo);
        p1.setEstado(EstadoPago.PAGADO);
        p1.setFechaPago(LocalDateTime.now().minusMinutes(15));
        p1.setObservacion("Pago semilla (EFECTIVO).");
        p1 = pagoRepo.save(p1);

        // Comprobante de p1 (BOLETA)
        Comprobante c1 = new Comprobante();
        c1.setPago(p1);
        c1.setTipo(TipoComprobante.BOLETA);
        c1.setNumero("B001-000001");
        c1.setCliente("María García");
        c1.setFechaEmision(LocalDateTime.now().minusMinutes(14));
        compRepo.save(c1);

        // Pago PENDIENTE para sesión en curso (SES-SEED-002, PC01, DNI 12345678) — aún pendiente
        Pago p2 = new Pago();
        p2.setCodigoSesion("SES-SEED-002");
        p2.setMaquinaCodigo("PC01");
        p2.setClienteDni("12345678");
        p2.setMonto(new BigDecimal("1.46")); // 25 min a 3.50/h = 1.46 aprox
        p2.setMetodoPago(yape);
        p2.setEstado(EstadoPago.PENDIENTE);
        p2.setFechaPago(LocalDateTime.now());
        p2.setObservacion("Pendiente de confirmar (YAPE).");
        pagoRepo.save(p2);

        // Otro pago de ejemplo (anulado)
        Pago p3 = new Pago();
        p3.setCodigoSesion("SES-SEED-003");
        p3.setMaquinaCodigo("PC03");
        p3.setClienteDni("12345678");
        p3.setMonto(new BigDecimal("0.33"));
        p3.setMetodoPago(efectivo);
        p3.setEstado(EstadoPago.ANULADO);
        p3.setFechaPago(LocalDateTime.now().minusMinutes(5));
        p3.setObservacion("Anulado por cancelación de sesión.");
        pagoRepo.save(p3);
    }
}