import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {EventService} from "../../_services";
import {ActivatedRoute} from "@angular/router";
import {EventComponent} from "../event/event.component";

@Component({
  selector: 'app-event-container',
  templateUrl: './event-container.component.html',
  styleUrls: ['../../wishlist/wishlist/wishlist.component.css']
})
export class EventContainerComponent implements OnInit, AfterViewInit {
  isParticipant: boolean = false;
  isCreator: boolean = false;
  eventId: string;

  @ViewChild(EventComponent) eventComponent;

  constructor(private eventService: EventService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
    this.eventId = this.activatedRoute.snapshot.paramMap.get('id');
    let currentUserId = JSON.parse(sessionStorage.getItem('currentUser')).id;
    this.eventService.isParticipant(currentUserId, this.eventId).subscribe(
      () => this.isParticipant = true,
      () => this.isParticipant = false);
  }

  ngAfterViewInit() {
    this.isCreator = this.eventComponent.isCreator;
  }
}
