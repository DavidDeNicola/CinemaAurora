import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  // Numero di chiamate HTTP ancora in corso (lo aggiorna loadingInterceptor)
  private richiesteInCorso = 0;

  get loading(): boolean {
    return this.richiesteInCorso > 0;
  }

  inizioRichiesta(): void {
    this.richiesteInCorso++;
  }

  fineRichiesta(): void {
    this.richiesteInCorso = Math.max(0, this.richiesteInCorso - 1);
  }
}
