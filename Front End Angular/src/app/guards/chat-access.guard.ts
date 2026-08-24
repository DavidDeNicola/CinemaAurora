import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Ruolo } from '../enums/ruolo';

export const chatAccessGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const ruolo = auth.getRuolo();

  if (ruolo === Ruolo.CLIENTE || ruolo === Ruolo.STAFF) {
    return true;
  }

  router.navigateByUrl('/home');
  return false;
};
