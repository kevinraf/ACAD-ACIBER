export interface Sesion {
  id?: number;
  codigo: string;

  maquinaCodigo: string;
  maquinaNombre: string;

  clienteDni: string;
  clienteNombre: string;

  minutosAsignados: number;
  minutosConsumidos: number;
  minutosRestantes: number;

  estado: string;          // EN_CURSO, FINALIZADA, CANCELADA
  costoHora: number;
  totalCalculado: number;

  // 🔹 campos del pago
  pagoId?: number;
  estadoPago?: string;
  pagado?: boolean;

  // 🔹 segundos transcurridos en tiempo real (solo frontend)
  elapsedSeconds?: number;
}
