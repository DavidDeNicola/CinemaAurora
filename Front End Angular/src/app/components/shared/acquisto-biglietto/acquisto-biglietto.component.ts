import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ResponseSpettacoloDTO } from '../../../dto/spettacolo/response/response-spettacolo-dto';
import { ResponseFilmDTO } from '../../../dto/film/response/response-film-dto';
import { ResponseBigliettoDTO } from '../../../dto/biglietto/response/response-biglietto-dto';
import { BigliettoService } from '../../../services/biglietto.service';
import { AuthService } from '../../../services/auth.service';
import { Tipo } from '../../../enums/tipo';
import {ActivatedRoute, Router} from "@angular/router";
import {ResponseSalaDTO} from "../../../dto/sala/response/response-sala-dto";
import { ResponsePostoSpettacoloDto} from "../../../dto/post/response/response-posto-dto";
import {PostoService} from "../../../services/posto.service";
import { InsertBigliettoDTO } from "../../../dto/biglietto/request/insert-biglietto-dto";
import { ConfirmDialogService } from "../../../services/confirm-dialog.service";
import { DurataPipe } from '../../../pipes/durata.pipe';
import { OrarioPipe } from '../../../pipes/orario.pipe';

const PREZZI_TIPO_SALA: Record<Tipo, number> = {
  [Tipo.NORMALE]: 7,
  [Tipo.TRED]: 15,
  [Tipo.IMAX]: 10,
};

@Component({
  selector: 'app-acquisto-biglietto',
  standalone: true,
  imports: [CommonModule, FormsModule, DurataPipe, OrarioPipe],
  templateUrl: './acquisto-biglietto.component.html',
  styleUrl: './acquisto-biglietto.component.css'
})
export class AcquistoBigliettoComponent implements OnInit {
  film: ResponseFilmDTO;
  spettacolo: ResponseSpettacoloDTO;
  sala: ResponseSalaDTO;

  mappaPosti: { [key: string]: any[] } = {};
  postiSelezionati: ResponsePostoSpettacoloDto[] = [];

  numeroBiglietti = 0;

  emailAcquirente: string | null = null;

  loading = false;
  errore: string | null = null;

  constructor(
      private bigliettoService: BigliettoService,
      private authService: AuthService,
      private postoService: PostoService,
      private route: ActivatedRoute,
      private router: Router,
      private confirmDialogService: ConfirmDialogService,
  ) {
    const extras = this.router.getCurrentNavigation()?.extras?.state;
    if (!extras || (extras && !extras?.['film'])) {
      this.router.navigateByUrl('/lista-spettacoli');
    }
    this.film = extras?.['film'];
    this.spettacolo = extras?.['spettacolo'];
  }

  ngOnInit(): void {
    this.emailAcquirente = this.authService.getEmail();

    this.sala = this.route.snapshot.data['sala'];
    if (this.spettacolo) {
      this.caricaMappaPosti();
    }
  }

  get prezzoUnitario(): number {
    if (!this.sala.tipo) {
      return 0;
    }
    return PREZZI_TIPO_SALA[this.sala.tipo];
  }

  caricaMappaPosti(): void {
    this.postoService.findBySpettacolo(this.spettacolo.id).subscribe({
      next: (posti) => {
        this.costruisciGriglia(posti);
      },
      error: (err) => this.errore = 'Errore nel caricamento della mappa dei posti.'
    });
  }

  private costruisciGriglia(posti: ResponsePostoSpettacoloDto[]): void{
    if (!posti || posti.length === 0) return;

    const grigliaTemporanea: { [key: string]: any[] } = {};

    posti.forEach(posto => {
      const rigaLettera = posto.fila ? posto.fila.toString().trim() : 'Inca';

      if (!grigliaTemporanea[rigaLettera]) {
        grigliaTemporanea[rigaLettera] = [];
      }

      grigliaTemporanea[rigaLettera].push(posto);
    });

    Object.keys(grigliaTemporanea).forEach(lettera => {
      grigliaTemporanea[lettera].sort((a, b) => Number(a.colonna) - Number(b.colonna));
    });
    this.mappaPosti = grigliaTemporanea;
  }

  get postiDisponibili(): number {
    return this.spettacolo?.postiRimanenti ?? 0;
  }

  get totale(): number {
    return this.prezzoUnitario * this.numeroBiglietti;
  }


  chiudiModale(): void {
    this.router.navigateByUrl('/lista-spettacoli');
  }

  getLettereFile(): string[] {
    if (!this.mappaPosti) return [];

    return Object.keys(this.mappaPosti)
        .map(lettera => lettera.trim())
        .filter(lettera => lettera !== '' && lettera !== 'undefined' && lettera !== 'null')
        .sort((a, b) => a.localeCompare(b));
  }

  gestisciSelezionePosto(posto: ResponsePostoSpettacoloDto): void{
    if (posto.occupato) return;

    const chiavePostoCorrente = posto.id ? posto.id.toString() : `${posto.fila}-${posto.colonna}`;

    const index = this.postiSelezionati.findIndex(p => {
      const chiaveP = p.id ? p.id.toString() : `${p.fila}-${p.colonna}`;
      return chiaveP === chiavePostoCorrente;
    });

    if (index !== -1) {
      this.postiSelezionati.splice(index, 1);
    } else {
      this.postiSelezionati.push(posto);
    }

    this.numeroBiglietti = this.postiSelezionati.length;
  }

  isPostoSelezionato(posto: any): boolean {
    if (!this.postiSelezionati || this.postiSelezionati.length === 0) return false;

    const chiavePostoCorrente = posto.id ? posto.id.toString() : `${posto.fila}-${posto.colonna}`;

    return this.postiSelezionati.some(p => {
      const chiaveP = p.id ? p.id.toString() : `${p.fila}-${p.colonna}`;
      return chiaveP === chiavePostoCorrente;
    });
  }

  confermaAcquisto(): void {
    if (this.loading) return;

    if (this.numeroBiglietti === 0) {
      this.errore = 'Seleziona almeno un posto sulla mappa per procedere.';
      return;
    }

    this.loading = true;
    this.errore = null;

    const payload: InsertBigliettoDTO = {
      idSpettacolo: this.spettacolo.id,
      idPosti: this.postiSelezionati.map(p => p.id)
    };

    this.bigliettoService.insert(payload).subscribe({
      next: (biglietti: ResponseBigliettoDTO[]) => {
        this.loading = false;

        this.spettacolo.postiRimanenti = Math.max(0, this.spettacolo.postiRimanenti - this.numeroBiglietti);

        this.confirmDialogService.notifySuccess(
          biglietti.length === 1
            ? 'Il tuo biglietto è stato acquistato con successo.'
            : `I tuoi ${biglietti.length} biglietti sono stati acquistati con successo.`
        ).subscribe();
        this.router.navigateByUrl('/i-miei-biglietti');
      },
      error: () => {
        this.loading = false;
        this.errore = 'Impossibile completare l\'acquisto. Uno o più posti scelti potrebbero essere già stati riservati.';
      }
    });
  }
}