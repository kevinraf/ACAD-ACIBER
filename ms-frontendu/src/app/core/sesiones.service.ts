// src/app/core/sesiones.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Sesion } from './sesion.model';
import { environment } from '../../environments/environment';

export interface SesionCrearRequest {
  codigoMaquina: string;
  dniCliente: string;
  minutosAsignados: number;
}

@Injectable({
  providedIn: 'root'
})
export class SesionesService {

  private baseUrl = `${environment.apiUrl}/sesiones`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Sesion[]> {
    return this.http.get<Sesion[]>(this.baseUrl);
  }

  /** 🔹 Iniciar sesión (usado en NuevaSesion) */
  iniciar(body: SesionCrearRequest): Observable<Sesion> {
    return this.http.post<Sesion>(`${this.baseUrl}/iniciar`, body);
  }

  finalizar(codigo: string, metodoPago: string): Observable<Sesion> {
    const params = new HttpParams().set('metodoPago', metodoPago);
    return this.http.put<Sesion>(`${this.baseUrl}/${codigo}/finalizar`, {}, { params });
  }

  cancelar(codigo: string): Observable<Sesion> {
    return this.http.put<Sesion>(`${this.baseUrl}/${codigo}/cancelar`, {});
  }

  /** 🔹 Confirmar pago de una sesión (llama a /confirmar-pago en ms-internet) */
  confirmarPago(codigo: string): Observable<Sesion> {
    return this.http.put<Sesion>(`${this.baseUrl}/${codigo}/confirmar-pago`, {});
  }
}
