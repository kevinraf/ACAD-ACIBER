package pe.edu.upeu.mspago.Entidad;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="comprobante")
public class Comprobante {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional=false)
    private Pago pago;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=15)
    private TipoComprobante tipo;

    @Column(nullable=false, unique=true, length=30)
    private String numero;

    @Column(length=120)
    private String cliente;

    private LocalDateTime fechaEmision = LocalDateTime.now();
}