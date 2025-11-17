package pe.edu.upeu.msinternet.DatosSeeder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.msinternet.Entidad.*;
import pe.edu.upeu.msinternet.Repositorio.ClienteRepositorio;
import pe.edu.upeu.msinternet.Repositorio.MaquinaRepositorio;
import pe.edu.upeu.msinternet.Repositorio.SesionRepositorio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final MaquinaRepositorio maquinaRepo;
    private final ClienteRepositorio clienteRepo;
    private final SesionRepositorio sesionRepo;

    @Override
    @Transactional
    public void run(String... args) {
        seedMaquinas();
        seedClientes();
        seedSesionesDemo();
    }

    private void seedMaquinas() {
        if (maquinaRepo.count() > 0) return;

        Maquina m1 = new Maquina();
        m1.setCodigo("PC01");
        m1.setNombre("Máquina 01");
        m1.setEstado(EstadoMaquina.LIBRE);
        m1.setCostoHora(new BigDecimal("3.50"));
        m1.setDescripcion("Zona Gamer A");
        m1.setUltimaActualizacion(LocalDateTime.now());

        Maquina m2 = new Maquina();
        m2.setCodigo("PC02");
        m2.setNombre("Máquina 02");
        m2.setEstado(EstadoMaquina.LIBRE);
        m2.setCostoHora(new BigDecimal("2.50"));
        m2.setDescripcion("Zona Estándar B");
        m2.setUltimaActualizacion(LocalDateTime.now());

        Maquina m3 = new Maquina();
        m3.setCodigo("PC03");
        m3.setNombre("Máquina 03");
        m3.setEstado(EstadoMaquina.MANTENIMIENTO);
        m3.setCostoHora(new BigDecimal("4.00"));
        m3.setDescripcion("Zona Premium C");
        m3.setUltimaActualizacion(LocalDateTime.now());

        maquinaRepo.save(m1);
        maquinaRepo.save(m2);
        maquinaRepo.save(m3);
    }

    private void seedClientes() {
        if (clienteRepo.count() > 0) return;

        Cliente c1 = new Cliente();
        c1.setDni("12345678");
        c1.setNombreCompleto("Juan Pérez");
        c1.setTelefono("987654321");
        c1.setCorreo("juan.perez@example.com");

        Cliente c2 = new Cliente();
        c2.setDni("87654321");
        c2.setNombreCompleto("María García");
        c2.setTelefono("987111222");
        c2.setCorreo("maria.garcia@example.com");

        Cliente c3 = new Cliente();
        c3.setDni("44556677");
        c3.setNombreCompleto("Carlos Ramos");
        c3.setTelefono("999888777");
        c3.setCorreo("carlos.ramos@example.com");

        clienteRepo.save(c1);
        clienteRepo.save(c2);
        clienteRepo.save(c3);
    }

    private void seedSesionesDemo() {
        if (sesionRepo.count() > 0) return;

        // Utilidades
        Maquina pc01 = maquinaRepo.findByCodigo("PC01").orElseThrow();
        Maquina pc02 = maquinaRepo.findByCodigo("PC02").orElseThrow();
        Optional<Maquina> pc03 = maquinaRepo.findByCodigo("PC03"); // mantenimiento

        Cliente juan = clienteRepo.findByDni("12345678").orElseThrow();
        Cliente maria = clienteRepo.findByDni("87654321").orElseThrow();

        // SES-SEED-001: Sesión FINALIZADA en PC02 (María) 90 min
        Sesion s1 = new Sesion();
        s1.setCodigo("SES-SEED-001");
        s1.setMaquina(pc02);
        s1.setCliente(maria);
        s1.setHoraInicio(LocalDateTime.now().minusHours(2));
        s1.setHoraFin(LocalDateTime.now().minusMinutes(20));
        s1.setMinutosAsignados(120);
        s1.setMinutosConsumidos(90);
        s1.setCostoHora(pc02.getCostoHora());
        s1.setEstado(EstadoSesion.FINALIZADA);
        s1.setTotalCalculado(calcTotal(s1.getMinutosConsumidos(), s1.getCostoHora()));
        sesionRepo.save(s1);

        // liberar máquina (por ser finalizada)
        pc02.setEstado(EstadoMaquina.LIBRE);
        pc02.setUltimaActualizacion(LocalDateTime.now());
        maquinaRepo.save(pc02);

        // acumular horas a María
        maria.setHorasAcumuladas(maria.getHorasAcumuladas() + (s1.getMinutosConsumidos() / 60));
        clienteRepo.save(maria);

        // SES-SEED-002: Sesión EN_CURSO en PC01 (Juan) 60 min asignados, 25 min consumidos
        Sesion s2 = new Sesion();
        s2.setCodigo("SES-SEED-002");
        s2.setMaquina(pc01);
        s2.setCliente(juan);
        s2.setHoraInicio(LocalDateTime.now().minusMinutes(25));
        s2.setHoraFin(null);
        s2.setMinutosAsignados(60);
        s2.setMinutosConsumidos(25);
        s2.setCostoHora(pc01.getCostoHora());
        s2.setEstado(EstadoSesion.EN_CURSO);
        s2.setTotalCalculado(calcTotal(s2.getMinutosConsumidos(), s2.getCostoHora()));
        sesionRepo.save(s2);

        // máquina ocupada por sesión en curso
        pc01.setEstado(EstadoMaquina.OCUPADA);
        pc01.setUltimaActualizacion(LocalDateTime.now());
        maquinaRepo.save(pc01);

        // SES-SEED-003: Sesión CANCELADA (opcional, sin costo)
        if (pc03.isPresent()) {
            Sesion s3 = new Sesion();
            s3.setCodigo("SES-SEED-003");
            s3.setMaquina(pc03.get());
            s3.setCliente(juan);
            s3.setHoraInicio(LocalDateTime.now().minusMinutes(10));
            s3.setHoraFin(LocalDateTime.now().minusMinutes(5));
            s3.setMinutosAsignados(30);
            s3.setMinutosConsumidos(5);
            s3.setCostoHora(pc03.get().getCostoHora());
            s3.setEstado(EstadoSesion.CANCELADA);
            s3.setTotalCalculado(calcTotal(s3.getMinutosConsumidos(), s3.getCostoHora()));
            sesionRepo.save(s3);
            // pc03 sigue en mantenimiento (no cambiamos estado)
        }
    }

    private BigDecimal calcTotal(int minutos, BigDecimal costoHora){
        // total = (min/60) * costoHora, redondeo a 2 decimales
        return BigDecimal.valueOf(minutos)
                .divide(BigDecimal.valueOf(60), 2, java.math.RoundingMode.HALF_UP)
                .multiply(costoHora)
                .setScale(2, java.math.RoundingMode.HALF_UP);
    }
}