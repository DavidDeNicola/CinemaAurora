import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { ActivatedRoute, Router, RouterLink } from "@angular/router";
import { AuthService } from "../../../services/auth.service";
import { LoginRequestDTO } from "../../../dto/utente/request/login-request-dto";

@Component({
  selector: "app-login",
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: "./login.component.html",
  styleUrl: "./login.component.css",
})
export class LoginComponent implements OnInit {
  dto: LoginRequestDTO = { email: "", password: "" };
  loading = false;
  errore: string | null = null;
  avviso: string | null = null;
  mostraPassword = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    if (this.route.snapshot.queryParamMap.get("sessione") === "scaduta") {
      this.avviso =
        "La tua sessione è scaduta. Accedi di nuovo per continuare.";
    }
  }

  login(): void {
    if (!this.dto.email || !this.dto.password) {
      this.errore = "Compila tutti i campi.";
      return;
    }
    this.loading = true;
    this.errore = null;

    this.authService.login(this.dto).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(["/home"]);
      },
      error: (err) => {
        this.loading = false;
        if (err.status === 401) {
          this.errore = "Email o password non corretti.";
        } else {
          this.errore = "Errore durante il login. Riprova più tardi.";
        }
      },
    });
  }
}
