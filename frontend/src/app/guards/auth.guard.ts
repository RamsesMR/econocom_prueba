import { Injectable } from '@angular/core';

import {
    ActivatedRouteSnapshot,
    CanActivate,
    Router,
    RouterStateSnapshot,
    UrlTree
} from '@angular/router';

import {
    catchError,
    map,
    Observable,
    of
} from 'rxjs';

import { AuthService } from '../services/auth.service';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {

    constructor(
        private authService: AuthService,
        private router: Router
    ) { }

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean | UrlTree> | boolean | UrlTree {

        const token = localStorage.getItem('token');

        if (!token) {

            return this.router.createUrlTree(['/login']);
        }

        return this.authService.validarToken(token).pipe(

            map(() => true),

            catchError(() => {

                localStorage.removeItem('token');

                return of(
                    this.router.createUrlTree(['/login'])
                );
            })
        );
    }

}