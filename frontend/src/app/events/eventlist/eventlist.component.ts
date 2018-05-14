import {Component, OnInit} from '@angular/core';
import {EventService} from "../../_services";
import {Event} from "../../_models";
import {ActivatedRoute} from "@angular/router";
import {AlertService} from "../../_services/alert.service";

@Component({
  selector: 'app-eventlist',
  templateUrl: './eventlist.component.html',
  styleUrls: ['./eventlist.component.css']
})

export class EventlistComponent implements OnInit {
  events: Event[];

  constructor(private eventService: EventService,
              private activatedRoute: ActivatedRoute,
              private alertService: AlertService) {
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
        case 'drafts' : {
          this.getDraftsByCustId();
          break;
        }
        // case 'notes' : {
        //   this.getNotesByCustId();
        //   break;
        // }
        case 'invites' : {
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

  getDraftsByCustId(): void {
    this.eventService.getDraftsByCustId()
      .subscribe((events) => {
        this.events = events;
        if(events.toString() == ''){
          this.alertService.info('You have no drafts yet.',true);
        }
      });
  }

  // getNotesByCustId(): void {
  //   this.eventService.getNotesByCustId()
  //     .subscribe((events) => {
  //       this.events = events;
  //       if(events.toString() == ''){
  //         this.alertService.info('You have no notes yet.',true);
  //       }
  //     });
  // }

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
