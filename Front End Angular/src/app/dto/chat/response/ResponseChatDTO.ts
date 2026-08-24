import {ResponseInfoChatDTO} from "./ResponseInfoChatDTO";
import {ResponseMessaggioDTO} from "../../message/response/ResponseMessaggioDTO";

export interface ResponseChatDTO {
    chat: ResponseInfoChatDTO;
    messaggi: ResponseMessaggioDTO[];
}