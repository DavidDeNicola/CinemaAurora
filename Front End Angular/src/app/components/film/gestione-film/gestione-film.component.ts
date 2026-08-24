import { Component, OnInit, DestroyRef, inject } from '@angular/core'; // 👈 Aggiunti DestroyRef e inject
import { RouterLink } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { FilmService } from "../../../services/film.service";
import { GenereService } from "../../../services/genere.service";
import { ResponseFilmDTO } from "../../../dto/film/response/response-film-dto";
import { ResponseGenereDTO } from "../../../dto/genere/response/response-genere-dto";
import { EditFilmDTO } from "../../../dto/film/request/edit-film-dto";
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { ConfirmDialogService } from "../../../services/confirm-dialog.service";
import { finalize } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-gestione-film',
  standalone: true,
  imports: [RouterLink, CommonModule, ReactiveFormsModule],
  templateUrl: './gestione-film.component.html',
  styleUrl: './gestione-film.component.css'
})
export class GestioneFilmComponent implements OnInit {
  private destroyRef = inject(DestroyRef);

  films: ResponseFilmDTO[] = [];
  filmInserito = false;

  filmInModifica: ResponseFilmDTO | null = null;
  generi: ResponseGenereDTO[] = [];
  generiSelezionati: number[] = [];
  erroreModifica = '';
  modificaInCorso = false;

  formModifica = new FormGroup({
    titolo: new FormControl('', Validators.required),
    descrizione: new FormControl('', Validators.required),
    durata: new FormControl(0, [Validators.required, Validators.min(1)]),
    attori: new FormControl('', Validators.required),
    urlLocandina: new FormControl('', Validators.pattern(/^https?:\/\/.+/i)),
    idGeneri: new FormControl<number[]>([], Validators.required)
  });

  constructor(
      private filmService: FilmService,
      private genereService: GenereService,
      private route: ActivatedRoute,
      private confirmDialogService: ConfirmDialogService
  ) {}

  ngOnInit(): void {
    this.films = (this.route.snapshot.data['films'] as ResponseFilmDTO[]) ?? [];

    this.route.queryParams.pipe(
        takeUntilDestroyed(this.destroyRef)
    ).subscribe(params => {
      if (params['filmInserito'] === 'true') {
        this.filmInserito = true;
        setTimeout(() => this.filmInserito = false, 4000);
      }
    });
  }

  apriModifica(film: ResponseFilmDTO): void {
    this.filmInModifica = film;
    this.erroreModifica = '';
    this.modificaInCorso = false;

    if (this.generi.length === 0) {
      this.genereService.findAll().subscribe(generi => {
        this.generi = generi;
        this.popolaForm(film);
      });
    } else {
      this.popolaForm(film);
    }
  }

  private popolaForm(film: ResponseFilmDTO): void {
    this.generiSelezionati = this.generi
        .filter(g => film.nomeGeneri.includes(g.nome))
        .map(g => g.id);

    this.formModifica.setValue({
      titolo: film.titolo,
      descrizione: film.descrizione,
      durata: film.durata,
      attori: film.attori,
      urlLocandina: film.urlLocandina ?? '',
      idGeneri: this.generiSelezionati
    });
    this.formModifica.updateValueAndValidity();
  }

  chiudiModifica(): void {
    this.filmInModifica = null;
    this.formModifica.reset();
    this.generiSelezionati = [];
    this.erroreModifica = '';
  }

  toggleGenere(id: number): void {
    this.erroreModifica = '';
    if (this.generiSelezionati.includes(id)) {
      this.generiSelezionati = this.generiSelezionati.filter(gId => gId !== id);
    } else {
      this.generiSelezionati = [...this.generiSelezionati, id];
    }
    this.formModifica.controls.idGeneri.setValue(this.generiSelezionati);
    this.formModifica.controls.idGeneri.markAsTouched();
  }

  isGenereSelezionato(id: number): boolean {
    return this.generiSelezionati.includes(id);
  }

  salvaModifica(): void {
    this.formModifica.patchValue({
      titolo: this.formModifica.value.titolo?.trim() ?? '',
      descrizione: this.formModifica.value.descrizione?.trim() ?? '',
      attori: this.formModifica.value.attori?.trim() ?? '',
      urlLocandina: this.formModifica.value.urlLocandina?.trim() ?? ''
    }, { emitEvent: false });

    this.formModifica.updateValueAndValidity();

    if (this.formModifica.invalid || this.generiSelezionati.length === 0) {
      this.formModifica.markAllAsTouched();
      this.erroreModifica = 'Compila tutti i campi obbligatori e seleziona almeno un genere.';
      return;
    }

    if (!this.filmInModifica) return;

    this.erroreModifica = '';
    this.modificaInCorso = true;

    const dto: EditFilmDTO = {
      titolo: this.formModifica.value.titolo!.trim(),
      descrizione: this.formModifica.value.descrizione!.trim(),
      durata: Number(this.formModifica.value.durata!),
      attori: this.formModifica.value.attori!.trim(),
      urlLocandina: this.formModifica.value.urlLocandina?.trim() || 'https://placehold.co/600x900/1e1e1e/ffffff?text=Locandina',
      idGeneri: this.generiSelezionati,
      idSpettacoli: []
    };

    this.filmService.editById(this.filmInModifica.id, dto)
        .pipe(finalize(() => this.modificaInCorso = false))
        .subscribe({
          next: (filmAggiornato) => {
            this.films = this.films.map(f =>
                f.id === filmAggiornato.id ? filmAggiornato : f
            );
            this.chiudiModifica();
          },
          error: (err) => {
            console.error('Errore modifica:', err);
            this.erroreModifica = this.costruisciMessaggioErrore(err);
          }
        });
  }

  elimina(id: number): void {
    this.confirmDialogService.confirm({
      title: 'Elimina film',
      message: 'Sei sicuro di voler eliminare questo film?',
      confirmText: 'Elimina'
    }).subscribe(conferma => {
      if (!conferma) return;

      this.filmService.removeById(id).subscribe({
        next: () => this.films = this.films.filter(film => film.id !== id),
        error: (err) => console.error('Errore eliminazione:', err)
      });
    });
  }

  private costruisciMessaggioErrore(err: any): string {
    const erroriValidazione = err?.error?.errori;

    if (erroriValidazione && typeof erroriValidazione === 'object') {
      const etichette: Record<string, string> = {
        titolo: 'Titolo',
        descrizione: 'Descrizione',
        durata: 'Durata',
        attori: 'Attori',
        urlLocandina: 'URL locandina',
        idGeneri: 'Generi'
      };
      return Object.entries(erroriValidazione)
          .map(([campo, messaggio]) => `${etichette[campo] ?? campo}: ${messaggio}`)
          .join(' | ');
    }

    if (typeof err?.error?.message === 'string' && err.error.message.trim()) {
      return err.error.message;
    }

    return 'Modifica non riuscita. Controlla i dati inseriti e riprova.';
  }
}