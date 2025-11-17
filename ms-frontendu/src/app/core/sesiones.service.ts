import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Sesion } from './sesion.model';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SesionesService {

  private baseUrl = `${environment.apiUrl}/sesiones`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Sesion[]> {
    return this.http.get<Sesion[]>(this.baseUrl);
  }

  finalizar(codigo: string, metodoPago: string): Observable<Sesion> {
    let params = new HttpParams().set('metodoPago', metodoPago);
    return this.http.put<Sesion>(`${this.baseUrl}/${codigo}/finalizar`, {}, { params });
  }

  cancelar(codigo: string): Observable<Sesion> {
    return this.http.put<Sesion>(`${this.baseUrl}/${codigo}/cancelar`, {});
  }
}
