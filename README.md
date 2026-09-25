🎬 Cinema Aurora

[![CI](https://github.com/DavidDeNicola/CinemaAurora/actions/workflows/ci.yml/badge.svg)](https://github.com/DavidDeNicola/CinemaAurora/actions/workflows/ci.yml)

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
