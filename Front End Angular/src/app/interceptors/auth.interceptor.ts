import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";
import { AuthService } from "../services/auth.service";
import { inject } from "@angular/core";
import { Router } from "@angular/router";
import { EMPTY, catchError, throwError } from "rxjs";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const token = localStorage
    .getItem("auth_token")
    ?.replace(/^Bearer\s+/i, "")
    .trim();
  const richiedeAuth =
    req.url.includes("/staff/") ||
    req.url.includes("/admin/") ||
    req.url.includes("/cliente/") ||
    req.url.includes("/user/") ||
    req.url.endsWith("/edit_password");

  if (!token || !richiedeAuth) {
    return next(req);
  }

  // token scaduto (lo capiamo già dal browser)
  if (authService.isTokenScaduto(token)) {
    authService.logout();
    router.navigate(["/login"], { queryParams: { sessione: "scaduta" } });
    return EMPTY;
  }

  const copy = req.clone({
    setHeaders: { Authorization: `Bearer ${token}` },
  });

  // token rifiutato dal backend (es. firmato con un'altra chiave)
  return next(copy).pipe(
    catchError((errore: HttpErrorResponse) => {
      if (errore.status === 401) {
        authService.logout();
        router.navigate(["/login"], { queryParams: { sessione: "scaduta" } });
        return EMPTY;
      }
      return throwError(() => errore);
    }),
  );
};
