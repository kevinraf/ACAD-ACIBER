import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente } from './cliente.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ClientesService {
  private baseUrl = `${environment.apiUrl}/clientes`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.baseUrl);
  }

  registrar(cliente: Cliente): Observable<Cliente> {
    return this.http.post<Cliente>(this.baseUrl, cliente);
  }

  buscarPorDni(dni: string): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.baseUrl}/${dni}`);
  }
}
