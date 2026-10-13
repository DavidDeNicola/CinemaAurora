import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LongOmdbResponseApiDto } from "../../../dto/omdbapi/response/long-omdb-response-api-dto";
import { FilmService } from "../../../services/film.service";
import { InsertFilmDTO } from "../../../dto/film/request/insert-film-dto";
import { ResponseFilmDTO } from "../../../dto/film/response/response-film-dto";
import { ResponseGenereDTO } from '../../../dto/genere/response/response-genere-dto';
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { ConfirmDialogService } from "../../../services/confirm-dialog.service";

// Generi di OMDb (in inglese) → nomi dei generi del catalogo
const MAPPA_GENERI: Record<string, string> = {
  'Action': 'Azione',
  'Sci-Fi': 'Fantascienza',
  'Drama': 'Drammatico',
  'Thriller': 'Thriller',
  'Animation': 'Animazione',
  'Comedy': 'Commedia',
  'Horror': 'Horror',
  'Adventure': 'Avventura',
  'Biography': 'Biografico',
  'Crime': 'Crime',
  'Romance': 'Romantico',
  'History': 'Storico'
};

@Component({
  selector: 'app-ricerca',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './ricerca.component.html',
  styleUrl: './ricerca.component.css'
})
export class RicercaComponent implements OnInit {

  stringaRicerca = '';
  filmTrovati: LongOmdbResponseApiDto[] = [];
  generi: ResponseGenereDTO[] = [];

  constructor(
    private route: ActivatedRoute,
      private filmService: FilmService,
      private confirmDialogService: ConfirmDialogService
  ) {}

  ngOnInit(): void {
    this.generi = (this.route.snapshot.data['generi'] as ResponseGenereDTO[]) ?? [];
  }

  ricerca(): void {
    if (!this.stringaRicerca.trim()) {
      return;
    }

    this.filmService.findByTitolo(this.stringaRicerca).subscribe({
      next: (risultati) => {
        this.filmTrovati = risultati;
        if (risultati.length === 0) {
          this.confirmDialogService.notifyInfo(
              'Film non trovato nel database globale di OMDb.',
              'Nessun risultato'
          ).subscribe();
        }
      },
      error: (err) => {
        console.error('Errore durante la chiamata al backend:', err);
        this.filmTrovati = [];
        this.confirmDialogService.notifyError(
            'Impossibile recuperare il film. Controlla i servizi backend e riprova.',
            'Errore ricerca'
        ).subscribe();
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
      idGeneri: this.convertiGeneri(filmScelto.Genre)
    };

    this.filmService.insert(insertFilm).subscribe({
      next: (res: ResponseFilmDTO) => {
        this.confirmDialogService.notifySuccess(
            `"${res.titolo}" salvato con successo.`,
            'Film importato'
        ).subscribe();
      },
      error: (err) => {
        console.error('Errore durante il salvataggio del film.', err);
        this.confirmDialogService.notifyError(
            'Impossibile salvare il film selezionato. Riprova tra un attimo.',
            'Errore importazione'
        ).subscribe();
      }
    });
  }

  // "Animation, Adventure, Comedy" → id di Animazione, Avventura, Commedia
  private convertiGeneri(genreOmdb: string | undefined): number[] {
    if (!genreOmdb || genreOmdb === 'N/A') {
      return this.genereDiRiserva();
    }

    const ids = genreOmdb
      .split(',')
      .map(g => MAPPA_GENERI[g.trim()])
      .filter((nome): nome is string => !!nome)
      .map(nome => this.generi.find(g => g.nome.toLowerCase() === nome.toLowerCase())?.id)
      .filter((id): id is number => id !== undefined);

    const idUnici = [...new Set(ids)];
    return idUnici.length > 0 ? idUnici : this.genereDiRiserva();
  }

  // Se nessun genere OMDb corrisponde al catalogo, serve comunque almeno un genere
  private genereDiRiserva(): number[] {
    return this.generi.length > 0 ? [this.generi[0].id] : [1];
  }

  aggiungi(filmScelto: any): void {
    const imdbId = filmScelto.imdbID;

    this.filmService.existsByImdbId(imdbId).subscribe({
      next: (esiste: boolean) => {
        if (esiste) {
          this.confirmDialogService.notifyInfo(
              'Questo film è già presente nel database.',
              'Film già presente'
          ).subscribe();
        } else {
          this.eseguiInserimento(filmScelto);
        }
      },
      error: (err) => {
        console.error('Errore durante il controllo di esistenza', err);
        this.confirmDialogService.notifyError(
            'Impossibile verificare se il film è già presente nel database.',
            'Errore controllo'
        ).subscribe();
      }
    });
  }
}