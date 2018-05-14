import {Component, OnInit, ViewChild, AfterViewInit} from '@angular/core';
import {EventService} from "../../_services";
import {ActivatedRoute, Router} from "@angular/router";
import {EventComponent} from "../event/event.component";

@Component({
  selector: 'app-event-container',
  templateUrl: './event-container.component.html'
})
export class EventContainerComponent implements OnInit, AfterViewInit {
  isParticipant: boolean = true;
  isCreator: boolean = false;
  eventId: string;

  @ViewChild(EventComponent) eventComponent;

  constructor(private eventService: EventService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
     this.eventId = this.activatedRoute.snapshot.paramMap.get('id');
    // let currentUserId = JSON.parse(sessionStorage.getItem('currentUser')).id;
    // this.eventService.isParticipant(currentUserId,this.eventId)
    //   .subscribe(()=>{this.isParticipant=true}, ()=>{this.isParticipant=false});
  }

  ngAfterViewInit() {
    this.isParticipant = this.eventComponent.isParticipant;
    this.isCreator = this.eventComponent.isCreator;
  }
}
