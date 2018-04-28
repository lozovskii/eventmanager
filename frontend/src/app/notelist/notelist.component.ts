import { Component, OnInit } from '@angular/core';
import {EventService} from "../_services/event.service";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-notelist',
  templateUrl: './notelist.component.html',
  styleUrls: ['./notelist.component.css']
})
export class NotelistComponent implements OnInit {
  notes: Event[];
  notesForm: FormGroup;
  constructor(private eventService: EventService,
              private formBuilder: FormBuilder) { }

  ngOnInit() {
  }

  // getNotesByCustId(): void {
  //   this.eventService.getNotesByCustId()
  //     .subscribe((notes) => this.notes = notes);
  // }

}
