// src/app/auth/auth.ts (o auth.service.ts)
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap, Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface TokenDto {
  token: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private readonly TOKEN_KEY = 'token';

  constructor(private http: HttpClient) {}

  login(userName: string, password: string): Observable<TokenDto> {
    const url = `${environment.apiUrl}/auth/login`; // 👈 lo dejamos así
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
