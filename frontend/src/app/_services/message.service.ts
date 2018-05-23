import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';
import {AuthenticationService} from "./authentication.service";
import {Message} from "../_models/message";
import {MessageDTOModel} from "../_models/dto/messageDTOModel";
import {UserService} from "./user.service";

@Injectable()
export class MessageService {

  constructor(private http: HttpClient,
              private userService: UserService) {
  }

  create(message: MessageDTOModel) {
    console.log('Create Message: ' + JSON.stringify(message));
    return this.http.post<Event>('api/messages', message, {headers: AuthenticationService.getAuthHeader()});
  }

  getAllByChatId(chatId: String): Observable<Message[]>{
    const url = `api/messages/chat_messages?chatId=${chatId}`;
    return this.http.get<Message[]>(url, {headers: AuthenticationService.getAuthHeader()});
  }

}
