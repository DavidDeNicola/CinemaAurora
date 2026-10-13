import { Routes } from '@angular/router';
import { HomeComponent } from './components/shared/home/home.component';
import { PageNotFoundComponent } from './components/shared/page-not-found/page-not-found.component';
import { ListaFilmComponent } from './components/shared/lista-film/lista-film.component';
import { DettaglioFilmComponent } from './components/shared/dettaglio-film/dettaglio-film.component';
import { GestioneFilmComponent } from './components/staff/gestione-film/gestione-film.component';
import { InserisciFilmComponent } from './components/staff/inserisci-film/inserisci-film.component';
import { GestioneGenereComponent } from './components/staff/gestione-generi/gestione-generi.component';
import { RegistrazioneComponent } from './components/shared/registrazione/registrazione.component';
import { LoginComponent } from './components/shared/login/login.component';
import { ListaSpettacoliComponent } from './components/shared/lista-spettacoli/lista-spettacoli.component';
import { ForgotPasswordComponent } from './components/shared/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './components/shared/reset-password/reset-password.component';
import { ProfiloComponent } from './components/loggato/profilo/profilo.component';
import { staffGuard } from './guards/staff.guard';
import { authGuard } from './guards/auth.guard';
import { DashboardComponent } from './components/loggato/dashboard/dashboard.component';
import { CreaStaffComponent } from './components/superadmin/crea-staff/crea-staff.component';
import { adminGuard } from './guards/admin.guard';
import { BigliettiUtenteComponent } from './components/staff/biglietti-utente/biglietti-utente.component';
import { GestioneSpettacoliComponent } from './components/staff/gestione-spettacoli/gestione-spettacoli.component';
import { RicercaComponent } from './components/staff/ricerca/ricerca.component';
import { IMieiBigliettiComponent } from './components/cliente/imiei-biglietti/imiei-biglietti.component';
import {
  chatResolver,
  dettaglioFilmResolver,
  filmListResolver,
  gestioneFilmResolver,
  gestioneGeneriResolver,
  gestioneSalaResolver,
  gestioneSpettacoliResolver,
  homeResolver,
  iMieiBigliettiResolver,
  inserisciFilmResolver,
  listaSpettacoliResolver,
  salaResolver,
} from './app.resolver';
import { StatisticheComponent } from './components/superadmin/statistiche/statistiche.component';
import { FaqSectionComponent } from './components/shared/faq/faq.component';
import { ChatDashboardComponent } from './components/shared/chat-dashboard/chat-dashboard.component';
import { GestioneSalaComponent } from './components/staff/gestione-sala/gestione-sala.component';
import { AcquistoBigliettoComponent } from './components/shared/acquisto-biglietto/acquisto-biglietto.component';
import { chatAccessGuard } from './guards/chat-access.guard';
import { clienteGuard } from "./guards/cliente.guard";


export const routes: Routes = [
  // ── Pagine pubbliche ────────────────────────────────────────────
  { path: '', pathMatch: 'full', redirectTo: 'home' },
  { path: 'home', title: 'Home', component: HomeComponent, resolve: { dati: homeResolver } },
  { path: 'faq', title: 'FAQ', component: FaqSectionComponent },
  { path: 'lista-film', title: 'Lista film', component: ListaFilmComponent, resolve: { films: filmListResolver } },
  { path: 'dettaglio-film/:id', title: 'Dettaglio film', component: DettaglioFilmComponent, resolve: { dati: dettaglioFilmResolver } },
  { path: 'lista-spettacoli', title: 'Lista spettacoli', component: ListaSpettacoliComponent, resolve: { dati: listaSpettacoliResolver } },
  { path: 'login', title: 'Login', component: LoginComponent },
  { path: 'registrazione', title: 'Registrazione', component: RegistrazioneComponent },
  { path: 'password-dimenticata', title: 'Password dimenticata', component: ForgotPasswordComponent },
  { path: 'password-reset', title: 'Reimposta password', component: ResetPasswordComponent },

  // ── Pagine a tutto schermo per un solo ruolo ────────────────────
  { path: 'acquista-biglietto', title: 'Acquista biglietto', component: AcquistoBigliettoComponent, canActivate: [authGuard, clienteGuard], resolve: { sala: salaResolver } },
  { path: 'inserisci-film', title: 'Inserisci film', component: InserisciFilmComponent, canActivate: [authGuard, staffGuard], resolve: { generi: inserisciFilmResolver } },

  // ── Area riservata: la dashboard con la barra laterale ──────────
  // authGuard vale per tutte le pagine figlie; ogni figlia controlla poi il suo ruolo.
  {
    path: '',
    component: DashboardComponent,
    canActivate: [authGuard],
    children: [
      // tutti i ruoli
      { path: 'profilo', title: 'Profilo', component: ProfiloComponent },

      // CLIENTE
      { path: 'i-miei-biglietti', title: 'I miei biglietti', component: IMieiBigliettiComponent, canActivate: [clienteGuard], resolve: { dati: iMieiBigliettiResolver } },

      // CLIENTE e STAFF
      { path: 'assistenza-ticket', title: 'Assistenza', component: ChatDashboardComponent, canActivate: [chatAccessGuard], resolve: { chats: chatResolver }, runGuardsAndResolvers: 'always' },

      // STAFF
      { path: 'gestione-film', title: 'Gestione film', component: GestioneFilmComponent, canActivate: [staffGuard], resolve: { films: gestioneFilmResolver } },
      { path: 'gestione-generi', title: 'Gestione generi', component: GestioneGenereComponent, canActivate: [staffGuard], resolve: { generi: gestioneGeneriResolver } },
      { path: 'gestione-sala', title: 'Gestione sale', component: GestioneSalaComponent, canActivate: [staffGuard], resolve: { sale: gestioneSalaResolver } },
      { path: 'gestione-spettacoli', title: 'Gestione spettacoli', component: GestioneSpettacoliComponent, canActivate: [staffGuard], resolve: { dati: gestioneSpettacoliResolver } },
      { path: 'biglietti-utente', title: 'Biglietti utente', component: BigliettiUtenteComponent, canActivate: [staffGuard] },
      { path: 'ricerca', title: 'Ricerca catalogo', component: RicercaComponent, canActivate: [staffGuard], resolve: { generi: inserisciFilmResolver } },

      // SUPERADMIN
      { path: 'crea-staff', title: 'Gestione staff', component: CreaStaffComponent, canActivate: [adminGuard] },
      { path: 'statistiche', title: 'Statistiche', component: StatisticheComponent, canActivate: [adminGuard] },
    ]
  },

  // ── Qualsiasi altro URL → pagina 404 (deve restare l'ultima) ────
  { path: '**', title: 'Pagina non trovata', component: PageNotFoundComponent }
];
