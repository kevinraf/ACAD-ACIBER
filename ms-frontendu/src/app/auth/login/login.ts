// src/app/auth/login/login.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login implements OnInit {

  userName = '';
  password = '';
  loading = false;
  error: string | null = null;
  currentYear = new Date().getFullYear();

  showPassword = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    }
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit(): void {
    if (!this.userName || !this.password) {
      this.error = 'Ingrese usuario y contraseña';
      return;
    }

    this.loading = true;
    this.error = null;

    this.authService.login(this.userName, this.password).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error(err);
        this.loading = false;

        if (err.status === 400 || err.status === 401) {
          this.error = 'Usuario o contraseña incorrectos';
        } else {
          this.error = 'Ocurrió un error al iniciar sesión';
        }
      }
    });
  }
}
