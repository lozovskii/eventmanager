import {Component, Input, OnInit} from '@angular/core';
import {MessageService} from "../_services/message.service";
import {Message} from "../_models/message";
import {Observable} from "rxjs/Rx";
import {MessageDTOModel} from "../_models/dto/messageDTOModel";

@Component({
  selector: 'chat-feature',
  templateUrl: './chatbox.component.html',
  styleUrls: ['./chatbox.component.css']
})
export class ChatboxComponent implements OnInit {
  @Input('eventId') eventId: string;
  messages: Message[];
  replyMessage = "";

  constructor(private messageService: MessageService) {
  }

  ngOnInit() {
    this.getMessages("a6a09c3a-4fad-11e8-ac60-28d244397e41")

  }

  getMessages(chatId) {
    this.messageService.getAllByChatId(chatId)
      .subscribe((messages) => {
        this.messages = messages;
        console.log('Messages' + this.messages[1].content);
      })
  }

  addMessage(message: string) {
    let m: MessageDTOModel = new MessageDTOModel();
    m.message = {id:'0', content: message, authorId: 'd7638c26-4d85-11e8-bb3c-28d244397e45', chatId: "a6a09c3a-4fad-11e8-ac60-28d244397e41", date: '2018-05-15 10:10:10'};

    this.messageService.create(m).subscribe((data) => this.getMessages("a6a09c3a-4fad-11e8-ac60-28d244397e41"));
  }
}
