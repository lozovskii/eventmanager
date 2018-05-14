import {Component, Input, OnInit} from '@angular/core';
import {AlertService} from "../../_services/alert.service";
import {EventService, UserService} from "../../_services";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-note-content',
  templateUrl: './note-content.component.html',
  styleUrls: ['./note-content.component.css']
})
export class NoteContentComponent implements OnInit {
  @Input('noteId') noteId: string;

  eventDTO: EventDTOModel;
  constructor(private alertService: AlertService,
              private eventService: EventService,
              private activatedRoute: ActivatedRoute,
              private userService: UserService) { }

  ngOnInit() {
    this.noteId = this.activatedRoute.snapshot.paramMap.get('id');
    this.getNotesById();
  }

  getNotesById(): void {
    this.eventService.getNoteById(this.noteId)
      .subscribe((eventDTO) => {
        this.eventDTO = eventDTO;
        if(eventDTO.toString() == ''){
          this.alertService.info('This note is empty.',true);
        }
      });
  }
}
