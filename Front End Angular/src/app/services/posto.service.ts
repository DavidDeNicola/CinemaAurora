import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {SharedService} from "./shared.service";
import {ResponsePostoSpettacoloDto} from "../dto/post/response/response-posto-dto";

@Injectable({
  providedIn: 'root'
})
export class PostoService {
  private BASE_URL = 'http://localhost:8080';

  constructor(private http: HttpClient, private sharedService: SharedService) {}

  findBySpettacolo(id:number): Observable<ResponsePostoSpettacoloDto[]> {
    return this.http.get<ResponsePostoSpettacoloDto[]>(`${this.BASE_URL}/cliente/posto/${id}`);
  }
}
