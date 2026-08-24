import { Component, DestroyRef, inject } from '@angular/core';
import { LongOmdbResponseApiDto } from "../../../dto/omdbapi/response/long-omdb-response-api-dto";
import { FilmService } from "../../../services/film.service";
import { InsertFilmDTO } from "../../../dto/film/request/insert-film-dto";
import { ResponseFilmDTO } from "../../../dto/film/response/response-film-dto";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { ConfirmDialogService } from "../../../services/confirm-dialog.service";
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-ricerca',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './ricerca.component.html',
  styleUrl: './ricerca.component.css'
})
export class RicercaComponent {
  private destroyRef = inject(DestroyRef);

  stringaRicerca = '';
  filmTrovati: LongOmdbResponseApiDto[] = [];

  constructor(
      private filmService: FilmService,
      private confirmDialogService: ConfirmDialogService
  ) {}

  ricerca(): void {
    if (!this.stringaRicerca.trim()) {
      return;
    }

    this.filmService.findByTitolo(this.stringaRicerca).pipe(
        takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: (res: any) => {
        console.log('Dati ricevuti dal server:', res);

        if (Array.isArray(res) && res.length > 0) {
          this.filmTrovati = res;
        } else if (res && res.Title) {
          this.filmTrovati = [res];
        } else {
          this.filmTrovati = [];
          this.confirmDialogService.notifyInfo(
              'Film non trovato nel database globale di OMDb.',
              'Nessun risultato'
          ).pipe(takeUntilDestroyed(this.destroyRef)).subscribe();
        }
      },
      error: (err) => {
        console.error('Errore durante la chiamata al backend:', err);
        this.filmTrovati = [];
        this.confirmDialogService.notifyError(
            'Impossibile recuperare il film. Controlla i servizi backend e riprova.',
            'Errore ricerca'
        ).pipe(takeUntilDestroyed(this.destroyRef)).subscribe();
      }
    });
  }

  private eseguiInserimento(filmScelto: any): void {
    const insertFilm: InsertFilmDTO = {
      titolo: filmScelto.Title,
      descrizione: filmScelto.Plot || '',
      durata: filmScelto.Runtime && filmScelto.Runtime !== 'N/A' ? Number(filmScelto.Runtime.replace(' min', '')) : 0,
      attori: filmScelto.Actors || '',
      urlLocandina: filmScelto.Poster && filmScelto.Poster !== 'N/A' ? filmScelto.Poster : '',
      imdbID: filmScelto.imdbID && filmScelto.imdbID !== 'N/A' ? filmScelto.imdbID : '',
      idGeneri: [1]
    };

    this.filmService.insert(insertFilm).pipe(
        takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: (res: ResponseFilmDTO) => {
        this.confirmDialogService.notifySuccess(
            `"${res.titolo}" salvato con successo.`,
            'Film importato'
        ).pipe(takeUntilDestroyed(this.destroyRef)).subscribe();
      },
      error: (err) => {
        console.error('Errore durante il salvataggio del film.', err);
        this.confirmDialogService.notifyError(
            'Impossibile salvare il film selezionato. Riprova tra un attimo.',
            'Errore importazione'
        ).pipe(takeUntilDestroyed(this.destroyRef)).subscribe();
      }
    });
  }

  aggiungi(filmScelto: any): void {
    const imdbId = filmScelto.imdbID;

    this.filmService.existsByImdbId(imdbId).pipe(
        takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: (esiste: boolean) => {
        if (esiste) {
          this.confirmDialogService.notifyInfo(
              'Questo film è già presente nel database.',
              'Film già presente'
          ).pipe(takeUntilDestroyed(this.destroyRef)).subscribe();
        } else {
          this.eseguiInserimento(filmScelto);
        }
      },
      error: (err) => {
        console.error('Errore durante il controllo di esistenza', err);
        this.confirmDialogService.notifyError(
            'Impossibile verificare se il film è già presente nel database.',
            'Errore controllo'
        ).pipe(takeUntilDestroyed(this.destroyRef)).subscribe();
      }
    });
  }
}