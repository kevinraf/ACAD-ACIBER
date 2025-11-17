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

@NgModule({
  declarations: [
    App,
    Login,
    Dashboard
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideHttpClient(
      withInterceptors([authInterceptor]) // 👈 aquí va la función
    )
  ],
  bootstrap: [App]
})
export class AppModule { }
