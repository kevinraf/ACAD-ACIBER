import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../auth/auth';
import { SesionesService } from '../../core/sesiones.service';
import { Sesion } from '../../core/sesion.model';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit {

  sesiones: Sesion[] = [];
  loading = false;
  error: string | null = null;

  // método de pago seleccionado (global para la pantalla)
  metodoPago = 'EFECTIVO';
  metodosPago = ['EFECTIVO', 'YAPE', 'PLIN', 'TARJETA', 'TRANSFERENCIA'];

  constructor(
    private authService: AuthService,
    private router: Router,
    private sesionesService: SesionesService
  ) {}

  ngOnInit(): void {
    this.cargarSesiones();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  cargarSesiones(): void {
    this.loading = true;
    this.error = null;

    this.sesionesService.listar().subscribe({
      next: (data) => {
        this.sesiones = data;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'Error al cargar las sesiones';
        this.loading = false;
      }
    });
  }

  finalizarSesion(s: Sesion): void {
    if (s.estado !== 'EN_CURSO') {
      return;
    }

    if (!confirm(`¿Finalizar la sesión ${s.codigo} con método ${this.metodoPago}?`)) {
      return;
    }

    this.loading = true;
    this.sesionesService.finalizar(s.codigo, this.metodoPago).subscribe({
      next: () => {
        this.cargarSesiones();
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudo finalizar la sesión';
        this.loading = false;
      }
    });
  }

  cancelarSesion(s: Sesion): void {
    if (s.estado !== 'EN_CURSO') {
      return;
    }

    if (!confirm(`¿Cancelar la sesión ${s.codigo}?`)) {
      return;
    }

    this.loading = true;
    this.sesionesService.cancelar(s.codigo).subscribe({
      next: () => {
        this.cargarSesiones();
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudo cancelar la sesión';
        this.loading = false;
      }
    });
  }

  getEstadoPagoLabel(s: Sesion): string {
    if (!s.estadoPago) return 'N/A';
    return s.estadoPago;
  }
}
