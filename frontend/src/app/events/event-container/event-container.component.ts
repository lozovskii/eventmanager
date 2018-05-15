import {Component, OnInit, ViewChild, AfterViewInit} from '@angular/core';
import {EventService} from "../../_services";
import {ActivatedRoute, Router} from "@angular/router";
import {EventComponent} from "../event/event.component";
import {WishListComponent} from "../../wishlist/wishlist/wishlist.component";

@Component({
  selector: 'app-event-container',
  templateUrl: './event-container.component.html'
})
export class EventContainerComponent implements OnInit, AfterViewInit {
  isParticipant: boolean = false;
  isCreator: boolean = false;
  eventId: string;

  @ViewChild(EventComponent) eventComponent;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
     this.eventId = this.activatedRoute.snapshot.paramMap.get('id');
  }

  ngAfterViewInit() {
    this.isParticipant = this.eventComponent.isParticipant;
    this.isCreator = this.eventComponent.isCreator;
  }
}
