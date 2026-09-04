import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { Login } from '../models/login.model';
import { AuthResponse } from '../models/auth-response.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  login(datos: Login): Observable<AuthResponse> {

    return this.http.post<AuthResponse>(
      `${this.apiUrl}/login`,
      datos
    );
  }

  validarToken(token: string): Observable<string> {

    return this.http.post(
      `${this.apiUrl}/validate`,
      {},
      {
        headers: {
          Authorization: `Bearer ${token}`
        },
        responseType: 'text'
      }
    );
  }

  iniciarSso() {

    window.location.href = `${this.apiUrl}/sso`;
  }

  callbackSso(codigo: string, email: string): Observable<AuthResponse> {

    return this.http.get<AuthResponse>(
      `${this.apiUrl}/sso/callback?code=${codigo}&email=${email}`
    );
  }

}