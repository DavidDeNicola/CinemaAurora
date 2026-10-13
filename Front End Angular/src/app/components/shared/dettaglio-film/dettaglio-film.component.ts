import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import { ResponseFilmDTO } from '../../../dto/film/response/response-film-dto';
import { ResponseSpettacoloDTO } from '../../../dto/spettacolo/response/response-spettacolo-dto';
import { AuthService } from '../../../services/auth.service';
import { Ruolo } from '../../../enums/ruolo';
import {DettaglioFilmResolverData} from "../../../app.resolver";
import { DurataPipe } from '../../../pipes/durata.pipe';
import { OrarioPipe } from '../../../pipes/orario.pipe';
import { GeneriPipe } from '../../../pipes/generi.pipe';

@Component({
  selector: 'app-dettaglio-film',
  imports: [CommonModule, RouterLink, DurataPipe, OrarioPipe, GeneriPipe],
  templateUrl: './dettaglio-film.component.html',
  standalone: true,
  styleUrl: './dettaglio-film.component.css'
})
export class DettaglioFilmComponent implements OnInit {
  film?: ResponseFilmDTO;
  spettacoli: ResponseSpettacoloDTO[] = [];

  isStaff = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const dati = this.route.snapshot.data['dati'] as DettaglioFilmResolverData;
    this.film = dati.film;
    this.spettacoli = dati.spettacoli ?? [];
    const ruolo = this.authService.getRuolo();
    this.isStaff = ruolo === Ruolo.SUPERADMIN || ruolo === Ruolo.STAFF;
  }

  prenota(film: ResponseFilmDTO, spettacolo: ResponseSpettacoloDTO): void {
    this.router.navigateByUrl('/acquista-biglietto', {
      state: {
        spettacolo,
        film
      }
    });
  }

  handleMissingImage(event: Event) {
    const element = event.target as HTMLImageElement;
    element.src = '/Logo.png';
  }

}
