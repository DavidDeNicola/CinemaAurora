import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { ResponseSpettacoloDTO } from '../../../dto/spettacolo/response/response-spettacolo-dto';
import { ResponseFilmDTO } from '../../../dto/film/response/response-film-dto';
import { Ruolo } from '../../../enums/ruolo';
import { ListaSpettacoliResolverData } from '../../../app.resolver';
import { DurataPipe } from '../../../pipes/durata.pipe';
import { OrarioPipe } from '../../../pipes/orario.pipe';
import { GeneriPipe } from '../../../pipes/generi.pipe';

export interface SpettacoloPerFilm {
    filmKey: string;
    film: ResponseFilmDTO;
    spettacoli: ResponseSpettacoloDTO[];
}

export interface GruppoData {
    data: string;
    filmsDelGiorno: SpettacoloPerFilm[];
}

@Component({
    selector: 'app-lista-spettacoli',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink, DurataPipe, OrarioPipe, GeneriPipe],
    templateUrl: './lista-spettacoli.component.html',
    styleUrl: './lista-spettacoli.component.css',
})
export class ListaSpettacoliComponent implements OnInit {
    spettacoli: ResponseSpettacoloDTO[] = [];
    films: ResponseFilmDTO[] = [];
    spettacoliFiltrati: ResponseSpettacoloDTO[] = [];
    spettacoliSelezionati: Record<string, ResponseSpettacoloDTO> = {};

    isStaff: boolean = false;

    dataSelezionata: string = '';
    oggi: string = new Date().toISOString().split('T')[0];

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private authService: AuthService,
    ) {}

    ngOnInit(): void {
        const dati = this.route.snapshot.data['dati'] as ListaSpettacoliResolverData;
        this.films = dati.films ?? [];
        this.spettacoli = dati.spettacoli ?? [];
        const ruolo = this.authService.getRuolo();
        this.isStaff = ruolo === Ruolo.SUPERADMIN || ruolo === Ruolo.STAFF;
        this.spettacoliFiltrati = this.spettacoli;
        this.inizializzaSelezioni();
    }

    filtraPerData(): void {
        if (!this.dataSelezionata) {
            this.spettacoliFiltrati = this.spettacoli;
            return;
        }
        this.spettacoliFiltrati = this.spettacoli.filter((s) => s.data === this.dataSelezionata);
        this.inizializzaSelezioni();
    }

    filtraOggi(): void {
        this.dataSelezionata = this.oggi;
        this.filtraPerData();
    }

    resetFiltro(): void {
        this.dataSelezionata = '';
        this.spettacoliFiltrati = this.spettacoli;
        this.inizializzaSelezioni();
    }

    getFilmById(idFilm?: number): ResponseFilmDTO | undefined {
        if (!idFilm) return undefined;
        return this.films.find((f) => f.id === idFilm);
    }

    getFilmBySpettacolo(spettacolo: ResponseSpettacoloDTO): ResponseFilmDTO | undefined {
        return (
            this.getFilmById(spettacolo.idFilm) ??
            this.films.find((f) => this.normalizzaTesto(f.titolo) === this.normalizzaTesto(spettacolo.nomeFilm))
        );
    }

    selezionaSpettacolo(data: string, filmKey: string, spettacolo: ResponseSpettacoloDTO): void {
        if (spettacolo.postiRimanenti === 0) return;
        this.spettacoliSelezionati[this.selectionKey(data, filmKey)] = spettacolo;
    }

    spettacoloSelezionato(data: string, filmKey: string): ResponseSpettacoloDTO | undefined {
        return this.spettacoliSelezionati[this.selectionKey(data, filmKey)];
    }

    isOrarioSelezionato(data: string, filmKey: string, spettacolo: ResponseSpettacoloDTO): boolean {
        return this.spettacoloSelezionato(data, filmKey)?.id === spettacolo.id;
    }

    prenota(film: ResponseFilmDTO, spettacolo: ResponseSpettacoloDTO): void {
        this.router.navigateByUrl('/acquista-biglietto', {
            state: {
                spettacolo,
                film,
            },
        });
    }

    get gruppiPerData(): GruppoData[] {
        const mappaData = new Map<string, Map<string, ResponseSpettacoloDTO[]>>();

        for (const s of this.spettacoliFiltrati) {
            if (!mappaData.has(s.data)) mappaData.set(s.data, new Map());
            const mappaFilm = mappaData.get(s.data)!;
            const filmKey = this.filmKeyFromSpettacolo(s);
            if (!mappaFilm.has(filmKey)) mappaFilm.set(filmKey, []);
            mappaFilm.get(filmKey)!.push(s);
        }

        return Array.from(mappaData.entries())
            .sort(([a], [b]) => a.localeCompare(b))
            .map(([data, mappaFilm]) => ({
                data,
                filmsDelGiorno: Array.from(mappaFilm.entries()).map(([filmKey, spettacoli]) => ({
                    filmKey,
                    film: this.getFilmBySpettacolo(spettacoli[0]) ?? this.filmFallback(spettacoli[0].nomeFilm),
                    spettacoli: spettacoli.sort((a, b) => a.oraInizio.localeCompare(b.oraInizio)),
                })),
            }));
    }

    // Fallback nel caso il film non sia ancora nel catalogo
    private filmFallback(nomeFilm: string): ResponseFilmDTO {
        return { id: 0, titolo: nomeFilm, descrizione: '', durata: 0, attori: '', urlLocandina: '', nomeGeneri: [] };
    }

    private inizializzaSelezioni(): void {
        for (const gruppo of this.gruppiPerData) {
            for (const entry of gruppo.filmsDelGiorno) {
                const key = this.selectionKey(gruppo.data, entry.filmKey);
                if (this.spettacoliSelezionati[key]) continue;

                const primoDisponibile = entry.spettacoli.find((s) => s.postiRimanenti > 0);
                if (primoDisponibile) {
                    this.spettacoliSelezionati[key] = primoDisponibile;
                }
            }
        }
    }

    private filmKeyFromSpettacolo(spettacolo: ResponseSpettacoloDTO): string {
        return spettacolo.idFilm ? `id-${spettacolo.idFilm}` : `titolo-${this.normalizzaTesto(spettacolo.nomeFilm)}`;
    }

    private normalizzaTesto(value: string): string {
        return (value ?? '').trim().toLowerCase();
    }

    private selectionKey(data: string, filmKey: string): string {
        return `${data}-${filmKey}`;
    }
}
