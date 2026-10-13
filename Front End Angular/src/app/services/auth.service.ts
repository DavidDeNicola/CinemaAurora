import { Injectable } from "@angular/core";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { LoginRequestDTO } from "../dto/utente/request/login-request-dto";
import { Observable, tap } from "rxjs";
import { EditPasswordRequest } from "../dto/resetpassword/request/edit-password-request";
import { ResetPasswordResponse } from "../dto/resetpassword/response/reset-password-response";
import { ResetPasswordRequest } from "../dto/resetpassword/request/reset-password-request";
import { jwtDecode } from "jwt-decode";
import { ResponseUtenteDTO } from "../dto/utente/response/response-utente-dto";
import { InsertUtenteDTO } from "../dto/utente/request/insert-utente-dto";
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: "root",
})
export class AuthService {
  private BASE_URL = environment.apiUrl;
  private TOKEN_KEY = "auth_token";

  constructor(private http: HttpClient) {}

  login(dto: LoginRequestDTO): Observable<HttpResponse<null>> {
    return this.http
      .post<null>(`${this.BASE_URL}/login`, dto, { observe: "response" })
      .pipe(
        tap((response) => {
          const token = response.headers.get("Authorization");
          if (token) {
            localStorage.setItem(this.TOKEN_KEY, this.normalizeToken(token));
          }
        }),
      );
  }

  registrazione(dto: InsertUtenteDTO): Observable<ResponseUtenteDTO> {
    return this.http.post<ResponseUtenteDTO>(
      `${this.BASE_URL}/registrazione`,
      dto,
    );
  }

  editPassword(dto: EditPasswordRequest): Observable<ResetPasswordResponse> {
    return this.http.patch<ResetPasswordResponse>(
      `${this.BASE_URL}/edit_password`,
      dto,
    );
  }

  invioResetPassword(email: string): Observable<string> {
    return this.http.get(`${this.BASE_URL}/reset_password/${email}`, {
      responseType: "text",
    });
  }

  resetPassword(dto: ResetPasswordRequest): Observable<ResetPasswordResponse> {
    return this.http.patch<ResetPasswordResponse>(
      `${this.BASE_URL}/reset_password`,
      dto,
    );
  }

  getToken(): string | null {
    const token = localStorage.getItem(this.TOKEN_KEY);
    return token ? this.normalizeToken(token) : null;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  isTokenScaduto(token: string | null = this.getToken()): boolean {
    if (!token) return true;

    try {
      const decoded: any = jwtDecode(token);
      if (!decoded.exp) return true;
      return decoded.exp * 1000 < Date.now();
    } catch {
      return true;
    }
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  getRuolo(): string | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      const decoded: any = jwtDecode(token);
      return decoded.ruolo || null;
    } catch (e) {
      return null;
    }
  }

  getEmail(): string | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      const decoded: any = jwtDecode(token);
      return decoded.sub || null;
    } catch (e) {
      return null;
    }
  }

  private normalizeToken(token: string): string {
    return token.replace(/^Bearer\s+/i, "").trim();
  }
}
