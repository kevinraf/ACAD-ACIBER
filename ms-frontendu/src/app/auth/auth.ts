import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

interface TokenDto {
  token: string;
}

@Injectable({
  providedIn: 'root',
})
//SERVICO DEL AUTH
export class AuthService {

  private readonly TOKEN_KEY = 'token';

  constructor(private http: HttpClient) {}

  login(userName: string, password: string): Observable<TokenDto> {
    const url = `${environment.apiUrl}/auth/login`;
    return this.http.post<TokenDto>(url, { userName, password }).pipe(
      tap(res => {
        if (res && res.token) {
          localStorage.setItem(this.TOKEN_KEY, res.token);
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
