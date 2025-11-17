export interface Sesion {
  id: number;
  codigo: string;

  maquinaCodigo: string;
  maquinaNombre: string;

  clienteDni: string;
  clienteNombre: string;

  minutosAsignados: number;
  minutosConsumidos: number;
  minutosRestantes: number;

  estado: string;
  costoHora: number;
  totalCalculado: number;

  pagoId?: number;
  estadoPago?: string;
  pagado?: boolean;
}
