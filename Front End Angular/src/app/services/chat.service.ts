import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {InsertMessaggioDTO} from "../dto/message/request/InsertMessaggioDTO";
import {map, Observable, switchMap} from "rxjs";
import {ResponseMessaggioDTO} from "../dto/message/response/ResponseMessaggioDTO";
import {InsertChatDTO} from "../dto/chat/request/InsertChatDTO";
import {ResponseChatDTO} from "../dto/chat/response/ResponseChatDTO";
import {ResponseInfoChatDTO} from "../dto/chat/response/ResponseInfoChatDTO";

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private BASE_URL = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  creaChat(chat: InsertChatDTO): Observable<ResponseChatDTO>{
      return this.http.post<ResponseChatDTO>(`${this.BASE_URL}/cliente/chat`, chat);
  }

  inviaMessaggio(dto: InsertMessaggioDTO): Observable<ResponseChatDTO>{
      return this.http.post<any>(`${this.BASE_URL}/user/inviamessaggio`, dto).pipe(
          switchMap(() => this.singolaChat(dto.idChat))
      );
  }

  listaChat(): Observable<ResponseInfoChatDTO[]>{
    return this.http.get<ResponseInfoChatDTO[]>(`${this.BASE_URL}/user/chats`);
  }

  singolaChat(id: number): Observable<ResponseChatDTO>{
    return this.http.get<ResponseChatDTO>(`${this.BASE_URL}/user/chat?id=${id}`).pipe(
        map(chat => {
          return {
            ...chat,
            messaggi: chat.messaggi.sort((m1, m2) => {
              const d1 = Date.parse(m1.createdAt);
              const d2 = Date.parse(m2.createdAt);
              return d1 - d2;
            })
          }
        })
    );
  }

  cambioStato(idChat: number){
    return this.http.patch(`${this.BASE_URL}/staff/cambioStato`, idChat);
  }

  chatNonLette(): Observable<number>{
    return this.http.get<number>(`${this.BASE_URL}/user/contatore`);
  }

  checkMessaggiNonLetti(): Observable<number>{
    return this.http.get<number>(`${this.BASE_URL}/user/contatoreChatNonLette`);
  }
  
}
