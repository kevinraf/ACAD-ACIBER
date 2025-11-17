import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pago } from './pago.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PagosService {

  private baseUrl = `${environment.apiPagoUrl}/pagos`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Pago[]> {
    return this.http.get<Pago[]>(this.baseUrl);
  }
}
