import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Ruolo } from '../enums/ruolo';

// Lascia passare CLIENTE e STAFF (le due parti della chat di assistenza)
export const chatAccessGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const ruolo = authService.getRuolo();

  if (ruolo === Ruolo.CLIENTE || ruolo === Ruolo.STAFF) return true;

  router.navigateByUrl('/home');
  return false;
};
