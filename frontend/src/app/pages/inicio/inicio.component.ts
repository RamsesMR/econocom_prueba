import { Component, OnInit } from '@angular/core';

import { Router } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';

import { MatIconModule } from '@angular/material/icon';

import { IdiomaService } from '../../services/idioma.service';

@Component({
  selector: 'app-inicio',
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.scss']
})
export class InicioComponent implements OnInit {

  textos: any = {};

  constructor(
    private router: Router,
    private idiomaService: IdiomaService
  ) {}

  ngOnInit(): void {

    const idioma = this.idiomaService.getIdiomaActual();

    this.idiomaService.cargarIdioma(idioma).subscribe({

      next: (respuesta) => {

        this.textos = respuesta;

      }

    });

  }

  cerrarSesion() {

    localStorage.removeItem('token');

    this.router.navigate(['/login']);
  }

}