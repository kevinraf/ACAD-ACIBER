package pe.edu.upeu.msinternet.Entidad;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "maquina")
public class Maquina {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=20)
    private String codigo;

    @Column(nullable=false, length=80)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private EstadoMaquina estado = EstadoMaquina.LIBRE;

    /** Tarifa por HORA para esta máquina */
    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal costoHora;

    @Column(length=200)
    private String descripcion;

    private LocalDateTime ultimaActualizacion = LocalDateTime.now();
}