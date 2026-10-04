import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Ruolo } from '../enums/ruolo';

export const clienteGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.getRuolo() === Ruolo.CLIENTE) return true;

  router.navigateByUrl('/home');
  return false;
};