import { Component, OnInit } from '@angular/core';
import { PagosService } from '../../core/pagos.service';
import { Pago } from '../../core/pago.model';

@Component({
  selector: 'app-pagos',
  standalone: false,
  templateUrl: './pagos.html',
  styleUrl: './pagos.scss'
})
export class PagosPage implements OnInit {

  pagos: Pago[] = [];
  loading = false;
  error: string | null = null;

  constructor(private pagosService: PagosService) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.loading = true;
    this.pagosService.listar().subscribe({
      next: (data) => {
        this.pagos = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar los pagos';
        this.loading = false;
      }
    });
  }

  total(): number {
    return this.pagos.reduce((acc, p) => acc + p.monto, 0);
  }
}
