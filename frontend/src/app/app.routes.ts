import { Routes } from '@angular/router';

import { LoginComponent } from './pages/login/login.component';
import { SsoCallbackComponent } from './pages/sso-callback/sso-callback.component';
import { InicioComponent } from './pages/inicio/inicio.component';

import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [

  {
    path: 'login',
    component: LoginComponent
  },

  {
    path: 'sso/callback',
    component: SsoCallbackComponent
  },

  {
    path: 'inicio',
    component: InicioComponent,
    canActivate: [AuthGuard]
  },

  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },

  {
    path: '**',
    redirectTo: '/inicio'
  }

];