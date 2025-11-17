package pe.edu.upeu.mspago.Entidad;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="pago")
public class Pago {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=50)
    private String codigoSesion;

    @Column(nullable=false, length=20)
    private String maquinaCodigo;

    @Column(nullable=false, length=15)
    private String clienteDni;

    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal monto;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=15)
    private EstadoPago estado = EstadoPago.PENDIENTE;

    private LocalDateTime fechaPago = LocalDateTime.now();

    @Column(length=250)
    private String observacion;
}