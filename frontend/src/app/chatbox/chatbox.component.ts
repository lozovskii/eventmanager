import {Component, Input, OnInit} from '@angular/core';
import {MessageService} from "../_services/message.service";
import {Message} from "../_models/message";
import {MessageDTOModel} from "../_models/dto/messageDTOModel";
import Stomp from 'stompjs';
import SockJS from 'sockjs.min.js';
import {UserService} from "../_services/user.service";
import {User} from "../_models";
import {Router} from "@angular/router";

@Component({
  selector: 'chat-feature',
  templateUrl: './chatbox.component.html',
  styleUrls: ['./chatbox.component.scss']
})
export class ChatboxComponent implements OnInit {
  @Input('eventId') eventId: string;

  messages: Message[];
  addingMessage = "";
  stompClient;
  currentUser: User;

  constructor(private messageService: MessageService,
              private userService: UserService,
              private router: Router) {
    this.currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
  }

  ngOnInit() {
    this.getMessages(this.eventId);
    this.initializeWebSocketConnection();
  }

  initializeWebSocketConnection() {
    let ws = new SockJS("https://pre-release.herokuapp.com/socket");
    this.stompClient = Stomp.over(ws);
    let that = this;

    this.stompClient.connect({}, function (frame) {
      that.stompClient.subscribe("/chat", (message) => {
        let m: Message = JSON.parse(message.body);
        that.messages.push(m);
      });
    });
  }

  getMessages(chatId) {
    this.messageService.getAllByChatId(chatId)
      .subscribe((messages) => {
        this.messages = messages;
      })
  }

  addMessage(message: string) {
    let thisMoment = new Date();
    let customerId = this.userService.getCurrentId();
    let m: MessageDTOModel = new MessageDTOModel();
    m.message = {
      id: '0',
      content: message,
      authorId: customerId,
      authorName:this.currentUser.login,
      chatId: this.eventId,
      date: thisMoment.getFullYear() + '-' + ('0' + (thisMoment.getMonth() + 1)).slice(-2) + '-' + ('0' + thisMoment.getDate()).slice(-2) + ' ' + ('0' + (thisMoment.getHours() + 1)).slice(-2) + ':' + ('0' + thisMoment.getMinutes()).slice(-2) + ':' + ('0' + thisMoment.getSeconds()).slice(-2)

    };
    let that = this;

    that.stompClient.send('/api/messages', {}, JSON.stringify(m));

  }
}
