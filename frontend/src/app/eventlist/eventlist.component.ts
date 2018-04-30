import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {EventService} from "../_services/event.service";
import {Event} from "../_models/event";
import {UserService} from "../_services/user.service";

@Component({
  selector: 'app-eventlist',
  templateUrl: './eventlist.component.html',
  styleUrls: ['./eventlist.component.css']
})

export class EventlistComponent implements OnInit {
  events: Event[];
  eventsForm: FormGroup;
  constructor(private eventService: EventService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.eventsForm = this.formBuilder.group({});
  }

  getEvents(): void {
    this.eventService.getAllEvents()
      .subscribe((events) => this.events = events);
  }

  getEventsByCustId(): void {
    this.eventService.getEventsByCustId()
      .subscribe((events) => {
      this.events = events;
    });
  }
}
