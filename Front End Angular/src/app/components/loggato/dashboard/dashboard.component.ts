import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';

import {AuthService} from "../../../services/auth.service";
import {MatListItem, MatNavList} from "@angular/material/list";
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from "@angular/material/sidenav";
import {Router, NavigationEnd, RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {CommonModule} from "@angular/common";
import {filter} from 'rxjs/operators';
import {MatTooltip} from "@angular/material/tooltip";
import {Subscription} from "rxjs";
import {ChatService} from "../../../services/chat.service";

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
  styleUrl: './dashboard.component.css'
})

export class DashboardComponent implements OnInit, OnDestroy {
  @ViewChild(MatSidenavContent) sidenavContent!: MatSidenavContent;

  userRole = '';
  nuoviMessaggi: boolean = false;
  private subs = new Subscription();


  constructor(
    private authService: AuthService,
    private chatService: ChatService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userRole = this.authService.getRuolo();

    // Reset dello scroll della Sidenav ad ogni cambio rotta riuscito
    this.subs.add(
      this.router.events.pipe(
        filter(event => event instanceof NavigationEnd)
      ).subscribe(() => {
        if (this.sidenavContent) {
          this.sidenavContent.scrollTo({ top: 0 });
        }
        this.aggiornaNotificheChat();
      })
    );

    this.aggiornaNotificheChat();
  }

  private aggiornaNotificheChat(): void {
    this.subs.add(
      this.chatService.chatNonLette().subscribe({
        next: numeroChatNonLette => this.nuoviMessaggi = numeroChatNonLette > 0,
        error: () => this.nuoviMessaggi = false
      })
    );
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

}
