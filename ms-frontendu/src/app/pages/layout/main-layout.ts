import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../auth/auth';

@Component({
  selector: 'app-main-layout',
  standalone: false,
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.scss'
})
export class MainLayout {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
