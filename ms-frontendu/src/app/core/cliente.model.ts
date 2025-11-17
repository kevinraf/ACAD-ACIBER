export interface Cliente {
  id?: number;
  dni: string;
  nombreCompleto: string;
  telefono?: string;
  correo?: string;
  horasAcumuladas?: number;
}
