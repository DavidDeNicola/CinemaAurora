import { Component, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';

import { AuthService } from '../../../services/auth.service';
import { MatListItem, MatNavList } from '@angular/material/list';
import { MatSidenav, MatSidenavContainer, MatSidenavContent } from '@angular/material/sidenav';
import { Router, NavigationEnd, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { filter, map } from 'rxjs/operators';
import { MatTooltip } from '@angular/material/tooltip';
import { Subscription } from 'rxjs';
import { ChatService } from '../../../services/chat.service';
import { BreakpointObserver } from '@angular/cdk/layout';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
    selector: 'app-dashboard',
    imports: [
        MatNavList,
        MatSidenav,
        MatSidenavContent,
        RouterOutlet,
        CommonModule,
        MatSidenavContainer,
        RouterLink,
        MatListItem,
        RouterLinkActive,
        MatTooltip,
    ],
    templateUrl: './dashboard.component.html',
    styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit, OnDestroy {
    @ViewChild(MatSidenavContent) sidenavContent!: MatSidenavContent;
    @ViewChild(MatSidenav) sidenav!: MatSidenav;

    private breakpointObserver = inject(BreakpointObserver);

    // true sotto i 576px (telefono), false sopra
    isMobile = toSignal(
        this.breakpointObserver.observe('(max-width: 576px)').pipe(map((risultato) => risultato.matches)),
        { initialValue: false },
    );

    userRole = '';
    nuoviMessaggi: boolean = false;
    private subs = new Subscription();

    constructor(
        private authService: AuthService,
        private chatService: ChatService,
        private router: Router,
    ) {}

    ngOnInit(): void {
        this.userRole = this.authService.getRuolo();

        // Ad ogni cambio rotta: scroll in cima, chiusura del menu su telefono, aggiornamento notifiche
        this.subs.add(
            this.router.events.pipe(filter((event) => event instanceof NavigationEnd)).subscribe(() => {
                if (this.sidenavContent) {
                    this.sidenavContent.scrollTo({ top: 0 });
                }
                if (this.isMobile() && this.sidenav) {
                    this.sidenav.close();
                }
                this.aggiornaNotificheChat();
            }),
        );

        this.aggiornaNotificheChat();
    }

    // Apre/chiude il menu a scomparsa (usato solo su telefono)
    toggleMenu(): void {
        this.sidenav.toggle();
    }

    private aggiornaNotificheChat(): void {
        this.subs.add(
            this.chatService.chatNonLette().subscribe({
                next: (numeroChatNonLette) => (this.nuoviMessaggi = numeroChatNonLette > 0),
                error: () => (this.nuoviMessaggi = false),
            }),
        );
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }
}
