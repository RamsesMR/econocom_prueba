import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class IdiomaService {

  private idiomaDefecto = 'es';

  constructor(private http: HttpClient) {}

  cargarIdioma(idioma: string): Observable<any> {

    localStorage.setItem('idioma', idioma);

    return this.http.get<any>(
      `assets/i18n/${idioma}.json`
    );
  }

  getIdiomaActual(): string {

    return localStorage.getItem('idioma') || this.idiomaDefecto;
  }

}