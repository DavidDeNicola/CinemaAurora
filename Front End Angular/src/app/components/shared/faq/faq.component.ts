import { Component } from '@angular/core';
import {Faq} from "../../../enums/faq";


@Component({
  selector: 'app-faq-section',
  templateUrl: './faq.component.html',
  styleUrl: './faq.component.css'
})
export class FaqSectionComponent {

  faqList: Faq[] = [
    {
      id: 1,
      domanda: 'Devo registrarmi per prenotare?',
      risposta: 'Sì, la registrazione è necessaria per completare una prenotazione e consultare lo storico dei biglietti.'
    },
    {
      id: 2,
      domanda: 'Quanti biglietti posso prenotare?',
      risposta: 'Non esiste un numero limite.'
    },
    {
      id: 3,
      domanda: 'Come ricevo il mio biglietto?',
      risposta: 'Al termine dell acquisto riceverai un il tuo biglietto conm il QrCode direttamente nel tuo profilo.'
    },
    {
      id: 4,
      domanda: 'Posso vedere le prenotazioni effettuate?',
      risposta: 'Sì, nella tua area personale è disponibile uno storico completo delle prenotazioni.'
    },
    {
      id: 5,
      domanda: 'Dove posso consultare i dettagli di un film?',
      risposta: 'Ogni film dispone di una scheda dettagliata con informazioni complete, raggiumgibile cliccando sul film interessato.'
    }];
}