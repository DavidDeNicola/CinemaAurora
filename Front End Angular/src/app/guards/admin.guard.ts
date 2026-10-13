import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Ruolo } from '../enums/ruolo';

// Lascia passare solo il SUPERADMIN
export const adminGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.getRuolo() === Ruolo.SUPERADMIN) return true;

  router.navigateByUrl('/home');
  return false;
};
