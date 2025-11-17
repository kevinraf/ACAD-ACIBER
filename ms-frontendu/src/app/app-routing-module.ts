import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Dashboard } from './pages/dashboard/dashboard';
import { NuevaSesion } from './pages/nueva-sesion/nueva-sesion';
import { ClientesPage } from './pages/clientes/clientes';
import { MaquinasPage } from './pages/maquinas/maquinas';
import { PagosPage } from './pages/pagos/pagos';
import { authGuard } from './auth/auth-guard';
import {MainLayout} from './pages/layout/main-layout';

const routes: Routes = [
  { path: 'login', component: Login },

  {
    path: '',
    component: MainLayout,
    canActivateChild: [authGuard],
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'sesiones/nueva', component: NuevaSesion },
      { path: 'clientes', component: ClientesPage },
      { path: 'maquinas', component: MaquinasPage },
      { path: 'pagos', component: PagosPage },
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' }
    ]
  },

  { path: '**', redirectTo: 'login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
