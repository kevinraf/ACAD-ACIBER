package pe.edu.upeu.mspago.Entidad;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="metodo_pago")
public class MetodoPago {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=30)
    private String nombre; // EFECTIVO, YAPE, PLIN, TARJETA...

    @Column(length=200)
    private String descripcion;

    @Column(nullable=false)
    private Boolean activo = true;
}