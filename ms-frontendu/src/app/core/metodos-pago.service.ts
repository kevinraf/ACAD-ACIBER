import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MetodoPago } from './metodo-pago.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MetodosPagoService {

  private baseUrl = `${environment.apiPagoUrl}/metodos-pago`;

  constructor(private http: HttpClient) {}

  listar(): Observable<MetodoPago[]> {
    return this.http.get<MetodoPago[]>(this.baseUrl);
  }
}
