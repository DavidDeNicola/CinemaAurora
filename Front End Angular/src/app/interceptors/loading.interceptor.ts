import { HttpInterceptorFn } from "@angular/common/http";
import { inject } from "@angular/core";
import { finalize } from "rxjs";
import { SharedService } from "../services/shared.service";

// Mostra lo spinner globale per tutta la durata di ogni chiamata HTTP
export const loadingInterceptor: HttpInterceptorFn = (req, next) => {
  const sharedService = inject(SharedService);

  sharedService.inizioRichiesta();

  return next(req).pipe(
    finalize(() => sharedService.fineRichiesta())
  );
};
