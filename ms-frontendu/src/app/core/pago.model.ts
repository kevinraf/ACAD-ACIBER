export interface Pago {
  id: number;
  codigoSesion: string;
  maquinaCodigo: string;
  clienteDni: string;
  monto: number;
  metodoPago: string;
  estado: string;
  fechaPago: string;
  observacion?: string;
}
