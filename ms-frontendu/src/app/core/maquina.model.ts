export interface Maquina {
  id?: number;
  codigo: string;
  nombre: string;
  estado: string;      // LIBRE / OCUPADA / BLOQUEADA / ...
  costoHora: number;
  descripcion?: string;
}
