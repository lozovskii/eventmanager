import {Component, OnInit} from '@angular/core';
import {EventService} from "../_services";
import {Event} from "../_models";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-eventlist',
  templateUrl: './eventlist.component.html',
  styleUrls: ['./eventlist.component.css']
})

export class EventlistComponent implements OnInit {
  events: Event[];

  constructor(private eventService: EventService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      let type = params['type'];
      switch (type) {
        case 'all' : {
          this.getEvents();
          break;
        }
        case 'my' : {
          this.getEventsByCustId();
          break;
        }
      }
    });
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
