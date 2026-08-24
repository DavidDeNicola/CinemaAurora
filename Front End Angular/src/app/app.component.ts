import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {NavbarComponent} from './components/shared/navbar/navbar.component';
import {FooterComponent} from './components/shared/footer/footer.component';
import {SpinnerComponent} from "./components/shared/spinner/spinner.component";
import {SharedService} from "./services/shared.service";
import {ChatButtonComponent} from "./components/shared/chat-button/chat-button.component";
import {AuthService} from "./services/auth.service";
import {Ruolo} from "./enums/ruolo";


@Component({
  selector: 'app-root',
    imports: [RouterOutlet, NavbarComponent, FooterComponent, SpinnerComponent, ChatButtonComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'CinemaFront';


  constructor(
      private sharedService: SharedService,
      private authService: AuthService
  ) {}

  get isLoading() {
    return this.sharedService.loading;
  }

  get isAuthenticated() {
    const ruolo = this.authService.getRuolo();
    return this.authService.isLoggedIn()
        && (ruolo === Ruolo.CLIENTE);
  }
}
