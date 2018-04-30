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
  isParticipant: boolean;
  constructor(private eventService: EventService,
              private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.isCreator=false;
    this.activatedRoute.params.subscribe(params => {
      let eventId = params['id'];
      console.log(eventId);
      this.eventService.getEventById(eventId)
        .subscribe((event) => {
          this.event = event;
          console.log(this.event);

          let currentUserId = JSON.parse(localStorage.getItem('currentUserObject')).id;

          this.isCreator = currentUserId  == this.event.creatorId;

          this.eventService.isParticipant(currentUserId, this.event.id).subscribe(() => {this.isParticipant = true}, ((error)=> {this.isParticipant=false}));
        });
    });
  }

  addParticipant() {
    this.eventService.addParticipant(this.event.id).subscribe(()=>{this.isParticipant=true});
  }

  removeParticipant() {
    this.eventService.removeParticipant(this.event.id).subscribe(()=>{this.isParticipant=false});
  }
}
