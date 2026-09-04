import { Component, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { CommonModule } from '@angular/common';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

import { AuthService } from '../../services/auth.service';
import { IdiomaService } from '../../services/idioma.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  mensajeError = '';
  verPassword = false;
  idiomaActual = 'es';
  textos: any = {};

  formulario = new FormGroup({
    email: new FormControl('', [
      Validators.required,
      Validators.email
    ]),
    password: new FormControl('', [
      Validators.required
    ])
  });

  constructor(
    private authService: AuthService,
    private idiomaService: IdiomaService,
    private router: Router
  ) { }

  ngOnInit(): void {

    this.idiomaActual = this.idiomaService.getIdiomaActual();

    this.cargarIdioma(this.idiomaActual);
  }

  cargarIdioma(idioma: string) {

    this.idiomaService.cargarIdioma(idioma).subscribe({
      next: (respuesta) => {

        this.idiomaActual = idioma;

        this.textos = respuesta;
      }
    });
  }

  cambiarIdioma(evento: Event) {

    const selector = evento.target as HTMLSelectElement;

    this.cargarIdioma(selector.value);
  }

  login() {

    if (this.formulario.invalid) {

      this.formulario.markAllAsTouched();

      return;
    }

    const datos = {
      email: this.formulario.value.email || '',
      password: this.formulario.value.password || ''
    };

    this.authService.login(datos).subscribe({
      next: (respuesta) => {

        localStorage.setItem('token', respuesta.token);

        console.log('Login correcto', respuesta);

        this.mensajeError = '';
        this.router.navigate(['/inicio']);
      },

      error: () => {

        this.mensajeError = this.textos.login?.credencialesInvalidas;
      }
    });
  }

  iniciarSso() {

    this.authService.iniciarSso();
  }

  togglePassword() {

    this.verPassword = !this.verPassword;
  }

}