import { Component, OnInit } from '@angular/core';
import { MaquinasService } from '../../core/maquinas.service';
import { Maquina } from '../../core/maquina.model';

@Component({
  selector: 'app-maquinas',
  standalone: false,
  templateUrl: './maquinas.html',
  styleUrl: './maquinas.scss'
})
export class MaquinasPage implements OnInit {

  maquinas: Maquina[] = [];
  loading = false;
  error: string | null = null;

  nueva: Maquina = {
    codigo: '',
    nombre: '',
    estado: 'LIBRE',
    costoHora: 2.50,
    descripcion: ''
  };

  constructor(private maquinasService: MaquinasService) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.loading = true;
    this.maquinasService.listar().subscribe({
      next: (data) => {
        this.maquinas = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar las máquinas';
        this.loading = false;
      }
    });
  }

  guardar(): void {
    if (!this.nueva.codigo || !this.nueva.nombre) {
      this.error = 'Código y nombre son obligatorios';
      return;
    }

    this.maquinasService.crear(this.nueva).subscribe({
      next: () => {
        this.nueva = { codigo: '', nombre: '', estado: 'LIBRE', costoHora: 2.5, descripcion: '' };
        this.cargar();
      },
      error: () => this.error = 'No se pudo guardar la máquina'
    });
  }

  bloquear(m: Maquina): void {
    this.maquinasService.bloquear(m.codigo).subscribe({
      next: () => this.cargar()
    });
  }

  liberar(m: Maquina): void {
    this.maquinasService.liberar(m.codigo).subscribe({
      next: () => this.cargar()
    });
  }
}
