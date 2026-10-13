import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import { ResponseFilmDTO } from '../../../dto/film/response/response-film-dto';
import { ResponseSpettacoloDTO } from '../../../dto/spettacolo/response/response-spettacolo-dto';
import { HomeResolverData } from '../../../app.resolver';
import { DurataPipe } from '../../../pipes/durata.pipe';
import { OrarioPipe } from '../../../pipes/orario.pipe';
import { GeneriPipe } from '../../../pipes/generi.pipe';

@Component({
    selector: 'app-home',
    imports: [CommonModule, RouterLink, DurataPipe, OrarioPipe, GeneriPipe],
    templateUrl: './home.component.html',
    standalone: true,
    styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit, OnDestroy {
    films: ResponseFilmDTO[] = [];
    spettacoli: ResponseSpettacoloDTO[] = [];
    adesso = new Date();

    private timerAggiornamento?: ReturnType<typeof setInterval>;

    constructor(private route: ActivatedRoute) {}

    ngOnInit(): void {
        const dati = this.route.snapshot.data['dati'] as HomeResolverData;
        this.films = dati.films ?? [];
        this.spettacoli = dati.spettacoli ?? [];

        this.timerAggiornamento = setInterval(() => {
            this.adesso = new Date();
        }, 60000);
    }

    ngOnDestroy(): void {
        if (this.timerAggiornamento) {
            clearInterval(this.timerAggiornamento);
        }
    }

    get filmInEvidenza(): ResponseFilmDTO[] {
        return this.asArray(this.films).slice(0, 4);
    }

    get spettacoliOggi(): ResponseSpettacoloDTO[] {
        const oggi = this.dataLocaleOggi();
        return this.asArray(this.spettacoli)
            .filter((spettacolo) => spettacolo.data === oggi)
            .sort(
                (a, b) => this.timestampSpettacolo(a.oraInizio, a.data) - this.timestampSpettacolo(b.oraInizio, b.data),
            );
    }

    spettacoloInCorso(spettacolo: ResponseSpettacoloDTO): boolean {
        const ora = this.adesso.getTime();
        const inizio = this.timestampSpettacolo(spettacolo.oraInizio, spettacolo.data);
        const fine = this.timestampSpettacolo(spettacolo.oraFine, spettacolo.data);
        return ora >= inizio && ora <= fine;
    }

    posterClass(index: number): string {
        return ['poster red', 'poster dark-red', 'poster gray', 'poster'][index % 4];
    }

    private dataLocaleOggi(): string {
        const anno = this.adesso.getFullYear();
        const mese = String(this.adesso.getMonth() + 1).padStart(2, '0');
        const giorno = String(this.adesso.getDate()).padStart(2, '0');
        return `${anno}-${mese}-${giorno}`;
    }

    private timestampSpettacolo(value: string, data: string): number {
        if (!value) return 0;
        const normalized = value.includes('T') || value.includes(' ') ? value.replace(' ', 'T') : `${data}T${value}`;
        return new Date(normalized).getTime();
    }

    private asArray<T>(value: T[] | null | undefined): T[] {
        return Array.isArray(value) ? value : [];
    }

    handleMissingImage(event: Event) {
        const element = event.target as HTMLImageElement;
        element.src = '/Logo.png';
    }
}
