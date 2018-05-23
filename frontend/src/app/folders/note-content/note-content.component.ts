import {Component, Input, OnInit} from '@angular/core';
import {AlertService} from "../../_services/alert.service";
import {EventService} from "../../_services";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {ActivatedRoute, Router} from "@angular/router";

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
              private router: Router) { }

  ngOnInit() {
    this.noteId = this.activatedRoute.snapshot.paramMap.get('id');
    this.getNoteById();
  }

  getNoteById(): void {
    this.eventService.getNoteById(this.noteId)
      .subscribe((eventDTO) => {
        this.eventDTO = eventDTO;
        if(eventDTO.toString() == ''){
          this.alertService.info('This note is empty.',true);
        }
      });
  }

  delete(){
    this.eventService.deleteEvent(this.eventDTO.event.id).subscribe(() => {
      this.alertService.info('Note successfully deleted!', true);
      this.router.navigate(['../folder-list', 'all']);
    });
  }
}
