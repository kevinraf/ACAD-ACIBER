import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Maquina } from './maquina.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MaquinasService {
  private baseUrl = `${environment.apiUrl}/maquinas`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Maquina[]> {
    return this.http.get<Maquina[]>(this.baseUrl);
  }

  crear(maquina: Maquina): Observable<Maquina> {
    return this.http.post<Maquina>(this.baseUrl, maquina);
  }

  bloquear(codigo: string): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${codigo}/bloquear`, {});
  }

  liberar(codigo: string): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${codigo}/liberar`, {});
  }
}
