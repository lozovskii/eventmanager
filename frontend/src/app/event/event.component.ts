import {Component, OnInit} from '@angular/core';
import {EventService} from "../_services/event.service";
import {ActivatedRoute, Router} from "@angular/router";
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
              private activatedRoute: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      let eventId = params['id'];
      this.eventService.getEventById(eventId)
        .subscribe((event) => {
          this.event = event;
          this.isCreator = JSON.parse(localStorage.getItem('currentUserObject')).id == this.event.creatorId;
        });
    });
  }

  delete() {
    this.eventService.deleteEvent(this.event.id).subscribe(() => {
      this.router.navigate(['/eventlist']);
    });
  }
}
