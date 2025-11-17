package pe.edu.upeu.msinternet.Entidad;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="sesion")
public class Sesion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=40)
    private String codigo;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    private Maquina maquina;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    private Cliente cliente;

    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;

    @Column(nullable=false)
    private Integer minutosAsignados;

    @Column(nullable=false)
    private Integer minutosConsumidos = 0;

    /** Copia del costoHora de la máquina al iniciar la sesión */
    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal costoHora;

    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal totalCalculado = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private EstadoSesion estado = EstadoSesion.EN_CURSO;

    private LocalDateTime fechaRegistro = LocalDateTime.now();

    /** >>> NUEVO: relación lógica con el pago en ms-pago <<< */
    private Long pagoId; // ID del pago registrado en ms-pago

    @Column(length = 15)
    private String estadoPago; // PENDIENTE / PAGADO / ANULADO
}
