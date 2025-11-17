// src/app/pages/nueva-sesion/nueva-sesion.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MaquinasService } from '../../core/maquinas.service';
import { ClientesService } from '../../core/clientes.service';
import { SesionesService } from '../../core/sesiones.service';
import { Maquina } from '../../core/maquina.model';
import { Cliente } from '../../core/cliente.model';

@Component({
  selector: 'app-nueva-sesion',
  standalone: false,
  templateUrl: './nueva-sesion.html',
  styleUrl: './nueva-sesion.scss'
})
export class NuevaSesion implements OnInit {

  maquinas: Maquina[] = [];
  selectedMaquinaCodigo = '';
  minutosAsignados = 60;

  dni = '';
  cliente: Cliente | null = null;
  clienteNoEncontrado = false;

  loading = false;
  error: string | null = null;
  success: string | null = null;

  constructor(
    private maquinasService: MaquinasService,
    private clientesService: ClientesService,
    private sesionesService: SesionesService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarMaquinas();
  }

  cargarMaquinas(): void {
    this.maquinasService.listar().subscribe({
      next: (data) => {
        // solo máquinas LIBRES
        this.maquinas = data.filter(m => m.estado === 'LIBRE');
      },
      error: () => {
        this.error = 'No se pudieron cargar las máquinas';
      }
    });
  }

  buscarCliente(): void {
    this.error = null;
    this.success = null;
    this.cliente = null;
    this.clienteNoEncontrado = false;

    if (!this.dni) {
      this.error = 'Ingrese un DNI para buscar';
      return;
    }

    this.loading = true;
    this.clientesService.buscarPorDni(this.dni).subscribe({
      next: (c) => {
        this.cliente = c;
        this.clienteNoEncontrado = false;
        this.loading = false;
      },
      error: (_err: any) => {
        this.clienteNoEncontrado = true;
        this.loading = false;
      }
    });
  }

  irAGestionClientes(): void {
    this.router.navigate(['/clientes'], { queryParams: { dni: this.dni } });
  }

  iniciarSesion(): void {
    this.error = null;
    this.success = null;

    if (!this.selectedMaquinaCodigo) {
      this.error = 'Seleccione una máquina';
      return;
    }
    if (!this.dni) {
      this.error = 'Ingrese el DNI del cliente';
      return;
    }

    this.loading = true;
    this.sesionesService.iniciar({
      codigoMaquina: this.selectedMaquinaCodigo,
      dniCliente: this.dni,
      minutosAsignados: this.minutosAsignados
    }).subscribe({
      next: () => {
        this.loading = false;
        this.success = 'Sesión iniciada correctamente';
        this.router.navigate(['/dashboard']);
      },
      error: (err: any) => {
        console.error(err);
        this.error = 'No se pudo iniciar la sesión';
        this.loading = false;
      }
    });
  }
}
