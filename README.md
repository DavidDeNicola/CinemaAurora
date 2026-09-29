🎬 Cinema Aurora

[![CI](https://github.com/DavidDeNicola/CinemaAurora/actions/workflows/ci.yml/badge.svg)](https://github.com/DavidDeNicola/CinemaAurora/actions/workflows/ci.yml)

## Demo online

L'applicazione è online su **https://cinema-aurora.duckdns.org**

Per esplorare tutte le funzionalità puoi usare questi account di prova:

| Ruolo | Email | Password | Cosa puoi provare |
|---|---|---|---|
| Cliente | marco.rossi@gmail.com | Marco123! | Acquisto dei biglietti con scelta del posto, gestione e annullamento dei propri biglietti, chat con l'assistenza |
| Staff | staff@gmail.com | Staff123! | Gestione di film (con ricerca tramite OMDb), generi e spettacoli, consultazione dei biglietti venduti, risposta alle chat dei clienti |
| Amministratore | admin@gmail.com | Admin123! | Statistiche su fatturato, biglietti venduti e occupazione (per film, spettacolo e sala), creazione ed eliminazione dei membri dello staff |

> I dati sono di prova e condivisi tra tutti i visitatori.

**Deploy:** AWS EC2 · Docker Compose · Nginx (reverse proxy) · HTTPS con Let's Encrypt



Un'applicazione web full-stack per la gestione degli spettacoli e l’acquisto di biglietti di un cinema. Questo progetto è suddiviso in due componenti principali: un frontend sviluppato in Angular e un backend realizzato con Spring Boot.

Tecnologie Utilizzate
⚬	Frontend: Angular, TypeScript, HTML/CSS
⚬	Backend: Java, Spring Boot
⚬	Database: MysQL

Struttura del Progetto

Il progetto è strutturato in due cartelle principali:

⚬	[Front End Angular] - Contiene il codice dell'interfaccia utente in Angular.
⚬	[Back End Spring] - Contiene il codice dell'API REST in Spring Boot.

Come avviare il progetto in locale

Prerequisiti

Assicurati di avere installato sul tuo PC/Mac:
⚬	Node.js
⚬	Angular CLI (installabile con il comando: npm install -g @angular/cli)
⚬	Java JDK 
⚬	Un IDE per Java (es. SpringBootTools o IntelliJ IDEA)

1. Avviare il Backend (Spring Boot)

	1.	Apri il tuo IDE per Java.
	2.	Importa la cartella del backend come progetto Maven esistente.
	3.	Attendi il download delle dipendenze e avvia la classe principale (quella con l'annotazione @SpringBootApplication).
	(Di default il server partirà all'indirizzo http://localhost:8080)

2. Avviare il Frontend (Angular)
	1.	Apri il terminale del Mac.
	2.	Entra nella cartella del frontend con il comando:
		cd percorso/della/tua/cartella/frontend
	3.	Installa le dipendenze (da fare solo la prima volta):
		npm install
	4.	Avvia il server locale di Angular:
		ng serve
	5.	Apri il browser e vai all'indirizzo http://localhost:4200.
