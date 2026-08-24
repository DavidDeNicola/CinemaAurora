import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";

import { DatePipe, NgClass } from "@angular/common";

import {catchError, map, Observable, Subscription, throwError} from "rxjs";

import {ActivatedRoute, Router, RouterOutlet} from "@angular/router";

import {Component, OnInit, OnDestroy, ViewChild, ElementRef} from '@angular/core';

import {ChatService} from "../../../services/chat.service";

import {ResponseInfoChatDTO} from "../../../dto/chat/response/ResponseInfoChatDTO";

import {ResponseChatDTO} from "../../../dto/chat/response/ResponseChatDTO";

import {ResponseMessaggioDTO} from "../../../dto/message/response/ResponseMessaggioDTO";
import {InsertMessaggioDTO} from "../../../dto/message/request/InsertMessaggioDTO";



@Component({

  selector: 'app-chat-dashboard',

  standalone: true,

  imports: [

    ReactiveFormsModule,

    NgClass,

    DatePipe,



  ],

  templateUrl: './chat-dashboard.component.html',

  styleUrl: './chat-dashboard.component.css'

})

export class ChatDashboardComponent implements OnInit, OnDestroy {



  chat: ResponseInfoChatDTO[] = [];

  messaggi: ResponseMessaggioDTO[] = [];

  formMessaggio: FormGroup;

  idChatSelezionata: number | null = null;

  chatSelezionata: ResponseChatDTO | null = null;

  @ViewChild('chatHistory') chatHistory: ElementRef<HTMLDivElement>;



  private subs = new Subscription();



  constructor(private chatService: ChatService, private route: Router, private activatedRoute: ActivatedRoute) { }



  ngOnInit(): void {

    this.formMessaggio = new FormGroup({

      messaggio: new FormControl('', [Validators.required, Validators.minLength(3)])

    });

    // Ci si iscrive a route.data (non solo snapshot) perché con
    // onSameUrlNavigation: 'reload' + runGuardsAndResolvers: 'always'
    // il componente viene riutilizzato (non ricreato) quando si ritorna
    // sulla stessa rotta: senza subscribe, la lista chat resterebbe
    // quella iniziale anche dopo la creazione di una nuova chat.
    this.subs.add(
      this.activatedRoute.data.subscribe(data => {
        this.chat = data['chats'] ?? [];
      })
    );

    /* if (this.chat && this.chat.length > 0) {

      this.selezionaChat(this.chat[0].id);

    } */

  }


  inviaMessaggio() {
    if (!this.messaggio.value || (this.messaggio.value && !this.messaggio.value.trim())) {
      return;
    }
    const body: InsertMessaggioDTO = {
      idChat: this.chatSelezionata.chat.id,
      messaggio: this.messaggio.value.trim()
    }
    this.subs.add(
        this.chatService.inviaMessaggio(body).subscribe({
          next: (response) => {
            this.chatSelezionata = response;
            this.messaggi = response.messaggi || [];

            setTimeout(() => {
              if (this.chatHistory) {
                this.chatHistory.nativeElement.scrollTop = this.chatHistory.nativeElement.scrollHeight;
              }
            }, 200)
          }
        })
    );

    this.messaggio.reset();

  }



  selezionaChat(idChat: number): void {

    this.idChatSelezionata = idChat;



    this.subs.add(

        this.chatService.singolaChat(idChat).subscribe({

          next: (response: ResponseChatDTO) => {

            this.chatSelezionata = response;

            this.messaggi = response.messaggi || [];

            setTimeout(() => {
              if (this.chatHistory) {
                this.chatHistory.nativeElement.scrollTop = this.chatHistory.nativeElement.scrollHeight;
              }
            }, 200)

          },

          error: (err) => console.error("Errore nel caricamento della singola chat", err)

        })

    );

  }


  get messaggio() {

    return this.formMessaggio.get('messaggio');

  }

  ngOnDestroy(): void {

    this.subs.unsubscribe();

  }

}