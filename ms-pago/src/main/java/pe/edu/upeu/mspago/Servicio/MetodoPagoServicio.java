package pe.edu.upeu.mspago.Servicio;



import pe.edu.upeu.mspago.Entidad.MetodoPago;

import java.util.List;

public interface MetodoPagoServicio {
    MetodoPago crear(MetodoPago mp);
    MetodoPago buscarPorNombre(String nombre);
    List<MetodoPago> listar();
}