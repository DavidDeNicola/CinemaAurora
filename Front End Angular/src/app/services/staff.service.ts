import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { InsertUtenteDTO } from '../dto/utente/request/insert-utente-dto';
import { ResponseUtenteDTO } from '../dto/utente/response/response-utente-dto';
import { ResponseUtenteDataDTO } from '../dto/utente/response/response-utente-data-dto';
import { environment } from '../../environments/environment';

// Gestione degli account staff: endpoint /admin, riservati al SUPERADMIN
@Injectable({
  providedIn: 'root'
})
export class StaffService {
  private BASE_URL = environment.apiUrl;

  constructor(private http: HttpClient) {}

  insert(dto: InsertUtenteDTO): Observable<ResponseUtenteDTO> {
    return this.http.post<ResponseUtenteDTO>(`${this.BASE_URL}/admin/aggiungi_staff`, dto);
  }

  findAll(): Observable<ResponseUtenteDataDTO[]> {
    return this.http.get<ResponseUtenteDataDTO[]>(`${this.BASE_URL}/admin/lista_staff`);
  }

  removeById(id: number): Observable<ResponseUtenteDTO> {
    return this.http.delete<ResponseUtenteDTO>(`${this.BASE_URL}/admin/staff/${id}`);
  }
}
