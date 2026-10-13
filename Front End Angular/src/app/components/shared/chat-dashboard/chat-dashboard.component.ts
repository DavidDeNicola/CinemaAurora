import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { DatePipe, NgClass } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { ChatService } from '../../../services/chat.service';
import { ResponseInfoChatDTO } from '../../../dto/chat/response/ResponseInfoChatDTO';
import { ResponseChatDTO } from '../../../dto/chat/response/ResponseChatDTO';
import { ResponseMessaggioDTO } from '../../../dto/message/response/ResponseMessaggioDTO';
import { InsertMessaggioDTO } from '../../../dto/message/request/InsertMessaggioDTO';

@Component({
  selector: 'app-chat-dashboard',
  standalone: true,
  imports: [ReactiveFormsModule, NgClass, DatePipe],
  templateUrl: './chat-dashboard.component.html',
  styleUrl: './chat-dashboard.component.css'
})
export class ChatDashboardComponent implements OnInit, OnDestroy {
  chat: ResponseInfoChatDTO[] = [];
  messaggi: ResponseMessaggioDTO[] = [];
  idChatSelezionata: number | null = null;
  chatSelezionata: ResponseChatDTO | null = null;

  formMessaggio = new FormGroup({
    messaggio: new FormControl('', [Validators.required, Validators.minLength(3)])
  });

  @ViewChild('chatHistory') chatHistory: ElementRef<HTMLDivElement>;

  private subs = new Subscription();

  constructor(private chatService: ChatService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    // Uso route.data (e non snapshot) perché, grazie a runGuardsAndResolvers: 'always',
    // quando si torna su questa pagina il componente viene riusato e il resolver
    // ricarica la lista: così compare anche un ticket appena creato.
    this.subs.add(
      this.route.data.subscribe(data => {
        this.chat = data['chats'] ?? [];
      })
    );
  }

  get messaggio() {
    return this.formMessaggio.get('messaggio');
  }

  selezionaChat(idChat: number): void {
    this.idChatSelezionata = idChat;

    this.chatService.singolaChat(idChat).subscribe(chat => this.mostraChat(chat));
  }

  inviaMessaggio(): void {
    const testo = this.messaggio?.value?.trim();
    if (!testo || !this.chatSelezionata) return;

    const body: InsertMessaggioDTO = {
      idChat: this.chatSelezionata.chat.id,
      messaggio: testo
    };

    this.chatService.inviaMessaggio(body).subscribe(chat => this.mostraChat(chat));
    this.messaggio?.reset();
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  // Mostra i messaggi della chat e scorre fino all'ultimo
  private mostraChat(chat: ResponseChatDTO): void {
    this.chatSelezionata = chat;
    this.messaggi = chat.messaggi || [];

    // aspetta che Angular abbia disegnato i nuovi messaggi, poi scorre in fondo
    setTimeout(() => {
      if (this.chatHistory) {
        this.chatHistory.nativeElement.scrollTop = this.chatHistory.nativeElement.scrollHeight;
      }
    }, 200);
  }
}
