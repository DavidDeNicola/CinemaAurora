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

  // Il contatore si aggiorna in un microtask (Promise.resolve().then):
  // se una chiamata parte mentre Angular sta disegnando la pagina (es. dentro ngOnInit),
  // cambiarlo subito causerebbe l'errore NG0100 "ExpressionChangedAfterItHasBeenChecked".
  // Le Promise vengono eseguite nell'ordine in cui sono create, quindi +1 arriva sempre prima del -1.
  inizioRichiesta(): void {
    Promise.resolve().then(() => this.richiesteInCorso++);
  }

  fineRichiesta(): void {
    Promise.resolve().then(() => this.richiesteInCorso = Math.max(0, this.richiesteInCorso - 1));
  }
}
