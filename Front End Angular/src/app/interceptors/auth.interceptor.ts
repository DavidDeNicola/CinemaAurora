import { HttpInterceptorFn } from '@angular/common/http';
import {AuthService} from "../services/auth.service";
import {inject} from "@angular/core";
import {Router} from "@angular/router";
import {EMPTY} from "rxjs";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);


  const token = localStorage.getItem('auth_token')?.replace(/^Bearer\s+/i, '').trim();
  const richiedeAuth = req.url.includes('/staff/') ||
    req.url.includes('/admin/') ||
    req.url.includes('/cliente/') ||
    req.url.includes('/user/') ||
    req.url.endsWith('/edit_password');

  if (!token || !richiedeAuth) {
    return next(req);
  }

  //token scaduto
  if(authService.isTokenScaduto(token)){
    const router = inject(Router);

    authService.logout();
    router.navigateByUrl('/login');
    return EMPTY;
  }


  const copy = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  });
  return next(copy);
};
