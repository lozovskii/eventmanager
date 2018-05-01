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
  isParticipant: boolean;

  constructor(private eventService: EventService,
              private activatedRoute: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit() {
    this.isCreator = false;
    this.activatedRoute.params.subscribe(params => {
      let eventId = params['id'];
      this.eventService.getEventById(eventId)
        .subscribe((event) => {
          this.event = event;
          let currentUserId = JSON.parse(localStorage.getItem('currentUserObject')).id;
          this.isCreator = currentUserId == this.event.creatorId;
          // this.isParticipant = this.event.people.includes(currentUserId)
        });
    });
  }

  addParticipant() {
    this.eventService.addParticipant(this.event.id).subscribe(() => {
      this.isParticipant = true;
      // this.event.people.push(JSON.parse(localStorage.getItem('currentUserObject')).id);
    });
  }

  removeParticipant() {
    this.eventService.removeParticipant(this.event.id).subscribe(() => {
      this.isParticipant = false;
      // const index = this.event.people.indexOf(JSON.parse(localStorage.getItem('currentUserObject')).id);
      // if (index>-1) {
      //   this.event.people.slice(index);
      // }
    });
  }

  delete() {
    this.eventService.deleteEvent(this.event.id).subscribe(() => {
      this.router.navigate(['/eventlist']);
    });
  }
}
