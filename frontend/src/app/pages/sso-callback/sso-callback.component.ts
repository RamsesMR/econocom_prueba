import { Component, OnInit } from '@angular/core';

import { ActivatedRoute, Router } from '@angular/router';

import { MatIconModule } from '@angular/material/icon';

import { AuthService } from '../../services/auth.service';

import { IdiomaService } from '../../services/idioma.service';

@Component({

  selector: 'app-sso-callback',

  standalone: true,

  imports: [

    MatIconModule

  ],

  templateUrl: './sso-callback.component.html',

  styleUrls: ['./sso-callback.component.scss']

})

export class SsoCallbackComponent implements OnInit {

  mensaje = '';

  textos: any = {};

  constructor(

    private route: ActivatedRoute,

    private authService: AuthService,

    private idiomaService: IdiomaService,

    private router: Router

  ) {}

  ngOnInit(): void {

    const idioma = this.idiomaService.getIdiomaActual();

    this.idiomaService.cargarIdioma(idioma).subscribe({

      next: (respuesta) => {

        this.textos = respuesta;

        this.mensaje = this.textos.sso.procesando;

        this.procesarSso();

      }

    });

  }

  procesarSso() {

    const codigo = this.route.snapshot.queryParamMap.get('code');

    const email = this.route.snapshot.queryParamMap.get('email');

    if (!codigo || !email) {

      this.mensaje = this.textos.sso.sinCodigo;

      return;

    }

    this.authService.callbackSso(codigo, email).subscribe({

      next: (respuesta) => {

        localStorage.setItem('token', respuesta.token);

        this.mensaje = this.textos.sso.correcto;

        console.log('SSO correcto', respuesta);

        setTimeout(() => {

          this.router.navigate(['/inicio']);

        }, 3000);

      },

      error: () => {

        this.mensaje = this.textos.sso.error;

      }

    });

  }

}