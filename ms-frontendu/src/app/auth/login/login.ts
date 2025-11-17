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

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Si ya hay token, manda directo al dashboard
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    }
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
