import {Component, OnInit} from '@angular/core';
import {EventService} from "../../_services";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-event-container',
  templateUrl: './event-container.component.html'
})
export class EventContainerComponent implements OnInit {
  isParticipant: boolean;
  eventId:string;

  constructor(private eventService: EventService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
    this.eventId = this.activatedRoute.snapshot.paramMap.get('id');
    let currentUserId = JSON.parse(sessionStorage.getItem('currentUser')).id;
    this.eventService.isParticipant(currentUserId,this.eventId)
      .subscribe(()=>{this.isParticipant=true}, ()=>{this.isParticipant=false});
  }
}
