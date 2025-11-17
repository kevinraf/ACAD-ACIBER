// src/app/pages/dashboard/dashboard.ts
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

  // método de pago global (header)
  metodoPago = 'EFECTIVO';
  metodosPago = ['EFECTIVO', 'YAPE', 'PLIN', 'TARJETA', 'TRANSFERENCIA'];

  // 🔹 Modal de finalización + comprobante
  mostrarModalFinalizar = false;
  sesionSeleccionada: Sesion | null = null;
  sesionComprobante: Sesion | null = null;
  metodoPagoModal = 'EFECTIVO';

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

  navegar(ruta: string): void {
    this.router.navigate([ruta]);
  }

  cargarSesiones(): void {
    this.loading = true;
    this.error = null;

    this.sesionesService.listar().subscribe({
      next: (data) => {
        this.sesiones = data;
        this.loading = false;
      },
      error: (err: any) => {
        console.error(err);
        this.error = 'Error al cargar las sesiones';
        this.loading = false;
      }
    });
  }

  /** 🔹 Abrir modal desde botón "Finalizar" */
  abrirModalFinalizar(s: Sesion): void {
    if (s.estado !== 'EN_CURSO') return;
    this.sesionSeleccionada = s;
    this.sesionComprobante = null;
    this.metodoPagoModal = this.metodoPago; // toma por defecto el del header
    this.error = null;
    this.mostrarModalFinalizar = true;
  }

  cerrarModalFinalizar(): void {
    this.mostrarModalFinalizar = false;
    this.sesionSeleccionada = null;
    this.sesionComprobante = null;
  }

  /** 🔹 Paso 1: llamar al backend para finalizar la sesión */
  ejecutarFinalizacion(): void {
    if (!this.sesionSeleccionada) return;

    this.loading = true;
    this.error = null;

    this.sesionesService.finalizar(this.sesionSeleccionada.codigo, this.metodoPagoModal)
      .subscribe({
        next: (sesionFinal: Sesion) => {
          this.loading = false;
          this.sesionComprobante = sesionFinal;
          this.sesionSeleccionada = sesionFinal;
          this.cargarSesiones(); // refresca la tabla
        },
        error: (err: any) => {
          console.error(err);
          this.error = 'No se pudo finalizar la sesión';
          this.loading = false;
        }
      });
  }

  /** 🔹 Paso 2: Confirmar pago (llama /confirmar-pago) */
  confirmarPagoSesion(): void {
    if (!this.sesionComprobante) return;

    this.loading = true;
    this.error = null;

    this.sesionesService.confirmarPago(this.sesionComprobante.codigo)
      .subscribe({
        next: (sesionAct: Sesion) => {
          this.sesionComprobante = sesionAct;
          this.sesionSeleccionada = sesionAct;
          this.loading = false;
          this.cargarSesiones();
        },
        error: (err: any) => {
          console.error(err);
          this.error = 'No se pudo confirmar el pago';
          this.loading = false;
        }
      });
  }

  cancelarSesion(s: Sesion): void {
    if (s.estado !== 'EN_CURSO') return;

    if (!confirm(`¿Cancelar la sesión ${s.codigo}?`)) {
      return;
    }

    this.loading = true;
    this.error = null;

    this.sesionesService.cancelar(s.codigo).subscribe({
      next: () => this.cargarSesiones(),
      error: (err: any) => {
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

  totalEnCurso(): number {
    return this.sesiones
      .filter(s => s.estado === 'EN_CURSO')
      .reduce((acc, s) => acc + (s.totalCalculado || 0), 0);
  }
}
