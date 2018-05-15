import {Component, OnInit} from '@angular/core';
import {EventService} from "../../_services";
import {Event} from "../../_models";
import {ActivatedRoute} from "@angular/router";
import {AlertService} from "../../_services/alert.service";
import {VISIBILITY} from "../../event-visibility";

@Component({
  selector: 'app-eventlist',
  templateUrl: './eventlist.component.html',
  styleUrls: ['./eventlist.component.css']
})

export class EventlistComponent implements OnInit {
  events: Event[];
  visibilityList: string[] = VISIBILITY;
  isMy:boolean = false;

  constructor(private eventService: EventService,
              private activatedRoute: ActivatedRoute,
              private alertService: AlertService) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      let type = params['type'];
      switch (type) {
        case 'all' : {
          this.isMy = false;
          this.getEvents();
          break;
        }
        case 'my' : {
          this.isMy = true;
          this.getEventsByCustId();
          break;
        }
        case 'my/sorted/title' : {
          this.isMy = true;
          this.getEventsByCustIdSorted();
          break;
        }
        case 'my/sorted/type' : {
          this.isMy = true;
          this.getEventsByCustIdSortedByType();
          break;
        }
        case 'my/filter/private' : {
          this.isMy = true;
          this.getEventsByCustIdFilterByType(this.visibilityList[2]);
          break;
        }
        case 'my/filter/public' : {
          this.isMy = true;
          this.getEventsByCustIdFilterByType(this.visibilityList[0]);
          break;
        }
        case 'my/filter/friends' : {
          this.isMy = true;
          this.getEventsByCustIdFilterByType(this.visibilityList[1]);
          break;
        }
        case 'drafts' : {
          this.isMy = false;
          this.getDraftsByCustId();
          break;
        }
        case 'invites' : {
          this.isMy = false;
          this.getInvitesByCustId();
          break;
        }
      }
    });
  }

  getEvents(): void {
    this.eventService.getAllEvents()
      .subscribe((events) => {
        this.events = events;
        if(events.toString() == ''){
          this.alertService.info('No events exist yet.',true);
        }
          });
  }

  getEventsByCustId(): void {
    this.eventService.getEventsByCustId()
      .subscribe((events) => {
        this.events = events;
        if(events.toString() == ''){
          this.alertService.info('You have no events yet.',true);
        }
      });
  }

  getEventsByCustIdSorted(): void {
    this.eventService.getEventsByCustIdSorted()
      .subscribe((events) => {
        this.events = events;
        if(events.toString() == ''){
          this.alertService.info('You have no events yet.',true);
        }
      });
  }

  getEventsByCustIdSortedByType(): void {
    this.eventService.getEventsByCustIdSortedType()
      .subscribe((events) => {
        this.events = events;
        if(events.toString() == ''){
          this.alertService.info('You have no events yet.',true);
        }
      });
  }

  getEventsByCustIdFilterByType(type:string): void {
    this.eventService.getEventsByCustIdFilterByType(type)
      .subscribe((events) => {
        this.events = events;
        console.log('events = ' + events);
        if(events.toString() == ''){
          this.alertService.info('You have no events yet.',true);
        }
      });
  }

  getDraftsByCustId(): void {
    this.eventService.getDraftsByCustId()
      .subscribe((events) => {
        this.events = events;
        if(events.toString() == ''){
          this.alertService.info('You have no drafts yet.',true);
        }
      });
  }

  getInvitesByCustId(): void {
    this.eventService.getInvitesByCustId()
      .subscribe((events) => {
        this.events = events;
        if(events.toString() == ''){
          this.alertService.info('You have no invites yet.',true);
        }
      });
  }

}
