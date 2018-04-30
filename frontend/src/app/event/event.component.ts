import { Component, OnInit } from '@angular/core';
import {EventService} from "../_services/event.service";
import {ActivatedRoute} from "@angular/router";
import {Event} from "../_models";

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements OnInit {
  event: Event;
  isCreator: boolean;
  constructor(private eventService: EventService,
              private activatedRoute: ActivatedRoute) { }

  ngOnInit() {

    this.activatedRoute.params.subscribe(params => {
      let eventId = params['id'];
      console.log(eventId);
      this.eventService.getEventById(eventId)
        .subscribe((event) => {
          this.event = event;
          console.log(this.event);

          this.isCreator = JSON.parse(localStorage.getItem('currentUserObject')).id == this.event.creatorId;
        });
    });


  }
}
