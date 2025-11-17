// src/app/pages/dashboard/dashboard.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
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
export class Dashboard implements OnInit, OnDestroy {

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

  // 🔹 Timer para tiempo real
  private timerId: any = null;

  constructor(
    private authService: AuthService,
    private router: Router,
    private sesionesService: SesionesService
  ) {}

  ngOnInit(): void {
    this.cargarSesiones();
    this.iniciarTimerTiempoReal();
  }

  ngOnDestroy(): void {
    if (this.timerId) {
      clearInterval(this.timerId);
    }
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
        this.inicializarElapsed();
        this.loading = false;
      },
      error: (err: any) => {
        console.error(err);
        this.error = 'Error al cargar las sesiones';
        this.loading = false;
      }
    });
  }

  /** 🔹 Inicializa el tiempo transcurrido base según minutosConsumidos */
  private inicializarElapsed(): void {
    this.sesiones.forEach(s => {
      const base = (s.minutosConsumidos || 0) * 60;
      s.elapsedSeconds = base;
    });
  }

  /** 🔹 Timer que cada segundo incrementa el tiempo en sesiones en curso */
  private iniciarTimerTiempoReal(): void {
    if (this.timerId) {
      clearInterval(this.timerId);
    }

    this.timerId = setInterval(() => {
      this.sesiones.forEach(s => {
        if (s.estado === 'EN_CURSO') {
          if (s.elapsedSeconds == null) {
            const base = (s.minutosConsumidos || 0) * 60;
            s.elapsedSeconds = base;
          }
          s.elapsedSeconds! += 1;
        }
      });
    }, 1000);
  }

  /** 🔹 Formato bonito: HH:MM:SS y si pasa de 1 día: Xd HH:MM:SS */
  formatElapsed(s: Sesion): string {
    const total = s.elapsedSeconds != null
      ? s.elapsedSeconds
      : (s.minutosConsumidos || 0) * 60;

    const days = Math.floor(total / 86400); // 60 * 60 * 24
    const remDay = total % 86400;
    const hours = Math.floor(remDay / 3600);
    const remHour = remDay % 3600;
    const minutes = Math.floor(remHour / 60);
    const seconds = remHour % 60;

    const hh = (hours < 10 ? '0' : '') + hours;
    const mm = (minutes < 10 ? '0' : '') + minutes;
    const ss = (seconds < 10 ? '0' : '') + seconds;

    if (days > 0) {
      return `${days}d ${hh}:${mm}:${ss}`;
    }
    return `${hh}:${mm}:${ss}`;
  }

  // ------- MODAL FINALIZAR + PAGO --------

  abrirModalFinalizar(s: Sesion): void {
    if (s.estado !== 'EN_CURSO') return;
    this.sesionSeleccionada = s;
    this.sesionComprobante = null;
    this.metodoPagoModal = this.metodoPago;
    this.error = null;
    this.mostrarModalFinalizar = true;
  }

  cerrarModalFinalizar(): void {
    this.mostrarModalFinalizar = false;
    this.sesionSeleccionada = null;
    this.sesionComprobante = null;
  }

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
          this.cargarSesiones();
        },
        error: (err: any) => {
          console.error(err);
          this.error = 'No se pudo finalizar la sesión';
          this.loading = false;
        }
      });
  }

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
  // 🔹 Calcula segundos restantes en base a asignados y elapsedSeconds
  remainingSeconds(s: Sesion): number {
    const total = (s.minutosAsignados || 0) * 60;
    const elapsed = s.elapsedSeconds != null
      ? s.elapsedSeconds
      : (s.minutosConsumidos || 0) * 60;

    const remaining = total - elapsed;
    return remaining > 0 ? remaining : 0;
  }

// 🔹 Formato HH:MM:SS (y días) para el tiempo restante
  formatRemaining(s: Sesion): string {
    const total = this.remainingSeconds(s);

    const days = Math.floor(total / 86400); // 24*60*60
    const remDay = total % 86400;
    const hours = Math.floor(remDay / 3600);
    const remHour = remDay % 3600;
    const minutes = Math.floor(remHour / 60);
    const seconds = remHour % 60;

    const hh = (hours < 10 ? '0' : '') + hours;
    const mm = (minutes < 10 ? '0' : '') + minutes;
    const ss = (seconds < 10 ? '0' : '') + seconds;

    if (days > 0) {
      return `${days}d ${hh}:${mm}:${ss}`;
    }
    return `${hh}:${mm}:${ss}`;
  }

}
