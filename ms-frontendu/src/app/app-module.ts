// src/app/app-module.ts
import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Login } from './auth/login/login';
import { Dashboard } from './pages/dashboard/dashboard';
import { authInterceptor } from './core/auth-interceptor';
import {NuevaSesion} from './pages/nueva-sesion/nueva-sesion';
import {ClientesPage} from './pages/clientes/clientes';
import {MaquinasPage} from './pages/maquinas/maquinas';
import {PagosPage} from './pages/pagos/pagos';
import {MainLayout} from './pages/layout/main-layout';

@NgModule({
  declarations: [
    App,
    Login,
    Dashboard,
    NuevaSesion,
    ClientesPage,
    MaquinasPage,
    MainLayout,
    PagosPage
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideHttpClient(withInterceptors([authInterceptor]))
  ],
  bootstrap: [App]
})
export class AppModule { }
