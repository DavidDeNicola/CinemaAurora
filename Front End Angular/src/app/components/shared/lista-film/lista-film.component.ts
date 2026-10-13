import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ResponseFilmDTO } from '../../../dto/film/response/response-film-dto';
import { DurataPipe } from '../../../pipes/durata.pipe';
import { GeneriPipe } from '../../../pipes/generi.pipe';

@Component({
    selector: 'app-lista-film',
    imports: [CommonModule, FormsModule, RouterLink, DurataPipe, GeneriPipe],
    templateUrl: './lista-film.component.html',
    standalone: true,
    styleUrl: './lista-film.component.css',
})
export class ListaFilmComponent implements OnInit {
    films: ResponseFilmDTO[] = [];
    ricerca = '';
    genereSelezionato = 'Tutti';


    constructor(private route: ActivatedRoute) {}

    ngOnInit(): void {
        this.films = (this.route.snapshot.data['films'] as ResponseFilmDTO[]) ?? [];
    }

    get generi(): string[] {
        const generi = this.films.flatMap((film) => film.nomeGeneri ?? []);
        return ['Tutti', ...Array.from(new Set(generi))];
    }

    get filmFiltrati(): ResponseFilmDTO[] {
        const query = this.ricerca.trim().toLowerCase();

        return this.films.filter((film) => {
            const titolo = (film.titolo ?? '').toLowerCase();
            const descrizione = (film.descrizione ?? '').toLowerCase();
            const attori = (film.attori ?? '').toLowerCase();

            const matchTesto =
                !query || titolo.includes(query) || descrizione.includes(query) || attori.includes(query);

            const matchGenere = this.genereSelezionato === 'Tutti' || film.nomeGeneri?.includes(this.genereSelezionato);

            return matchTesto && matchGenere;
        });
    }

    posterClass(index: number): string {
        return ['poster red', 'poster dark-red', 'poster gray', 'poster'][index % 4];
    }

    handleMissingImage(event: Event) {
        const element = event.target as HTMLImageElement;
        element.src = '/Logo.png';
    }
}
