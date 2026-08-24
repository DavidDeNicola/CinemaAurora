import {ResponseMessaggioDTO} from "../../message/response/ResponseMessaggioDTO";

export interface ResponseInfoChatDTO {
    id: number;
    oggetto: string;
    stato: string;
    messaggiInSospeso: boolean;
    nome: string;
    cognome: string;
    ultimoMessaggio: ResponseMessaggioDTO;
}