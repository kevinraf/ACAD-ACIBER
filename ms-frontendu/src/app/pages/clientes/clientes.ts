import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ClientesService } from '../../core/clientes.service';
import { Cliente } from '../../core/cliente.model';

@Component({
  selector: 'app-clientes',
  standalone: false,
  templateUrl: './clientes.html',
  styleUrl: './clientes.scss'
})
export class ClientesPage implements OnInit {

  clientes: Cliente[] = [];
  loading = false;
  error: string | null = null;

  nuevo: Cliente = {
    dni: '',
    nombreCompleto: '',
    telefono: '',
    correo: '',
    horasAcumuladas: 0
  };

  constructor(
    private clientesService: ClientesService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.cargar();

    // Si viene el dni desde "Nueva sesión", lo precargamos
    this.route.queryParams.subscribe(params => {
      if (params['dni']) {
        this.nuevo.dni = params['dni'];
      }
    });
  }

  cargar(): void {
    this.loading = true;
    this.clientesService.listar().subscribe({
      next: (data) => {
        this.clientes = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar los clientes';
        this.loading = false;
      }
    });
  }

  guardar(): void {
    if (!this.nuevo.dni || !this.nuevo.nombreCompleto) {
      this.error = 'DNI y nombre son obligatorios';
      return;
    }

    this.clientesService.registrar(this.nuevo).subscribe({
      next: () => {
        this.nuevo = { dni: '', nombreCompleto: '', telefono: '', correo: '', horasAcumuladas: 0 };
        this.cargar();
      },
      error: () => this.error = 'No se pudo guardar el cliente'
    });
  }
}
