package pe.edu.upeu.msinternet.Entidad;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="cliente", uniqueConstraints = @UniqueConstraint(columnNames = "dni"))
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=15)
    private String dni;

    @Column(nullable=false, length=120)
    private String nombreCompleto;

    @Column(length=20)
    private String telefono;

    @Column(length=120)
    private String correo;

    /** Horas acumuladas históricamente (redondeo entero de sesiones) */
    private Integer horasAcumuladas = 0;

    private LocalDateTime fechaRegistro = LocalDateTime.now();
}