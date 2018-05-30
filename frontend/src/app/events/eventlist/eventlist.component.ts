import {Component, OnInit} from '@angular/core';
import {EventService} from "../../_services";
import {Event, User} from "../../_models";
import {ActivatedRoute} from "@angular/router";
import {AlertService} from "../../_services";
import {VISIBILITY} from "../../event-visibility";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {FormControl, FormGroup} from "@angular/forms";
import {UserService} from "../../_services";
import 'rxjs/add/operator/map'

@Component({
  selector: 'app-eventlist',
  templateUrl: './eventlist.component.html',
  styleUrls: ['./eventlist.component.css']
})

export class EventlistComponent implements OnInit {
  events: Event[];
  eventsDTO: EventDTOModel[];
  visibilityList: string[] = VISIBILITY;
  isMy:boolean = false;
  isEmpty:boolean = false;
  pdfForm = new FormGroup({
    startDate:  new FormControl(),
    endDate:  new FormControl()
  });
  checkBoxOrder: number = 1;
  filteredEventsDTO: EventDTOModel[];
  backupEventsDTO: EventDTOModel[];
  currentId: string;
  currentUser: User;
  pager: any = {};
  pagedItems: any[];

  constructor(private eventService: EventService,
              private activatedRoute: ActivatedRoute,
              private userService: UserService,
              private alertService: AlertService) {
    this.backupEventsDTO = [];
    this.filteredEventsDTO = [];
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
        case 'invites' : {
          this.isMy = false;
          this.getInvitesByCustId();
          break;
        }
      }
    });
    this.currentId = this.userService.getCurrentId();
    this.currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
  }

  getEvents(): void {
    this.eventService.getAllEvents()
      .subscribe((events) => {
        this.eventsDTO = events;
        this.setPage(1);
        console.log(this.events);
        console.log(events);
        if(events.toString() == ''){
          this.isEmpty = true;
          this.alertService.info('No events exist yet.',true);
        }
      });
  }

  getEventsByCustId(): void {
    this.eventService.getEventsByCustId()
      .subscribe((eventsDTO) => {
        this.eventsDTO = eventsDTO;
        if(eventsDTO.toString() == ''){
          this.isEmpty = true;
          console.log(this.isEmpty);
          this.alertService.info('You have no events yet.',true);
        }
      });
  }

  getEventsByCustIdSorted(): void {
    this.eventService.getEventsByCustIdSorted()
      .subscribe((eventsDTO) => {
        this.eventsDTO = eventsDTO;
        if(eventsDTO.toString() == ''){
          this.isEmpty = true;
          this.alertService.info('You have no events yet.',true);
        }
      });
  }

  getEventsByCustIdSortedByType(): void {
    this.eventService.getEventsByCustIdSortedType()
      .subscribe((eventsDTO) => {
        this.eventsDTO = eventsDTO;
        if(eventsDTO.toString() == ''){
          this.isEmpty = true;
          this.alertService.info('You have no events yet.',true);
        }
      });
  }

  getEventsByCustIdFilterByType(type:string): void {
    this.eventService.getEventsByCustIdFilterByType(type)
      .subscribe((eventsDTO) => {
        this.eventsDTO = eventsDTO;
        if(event.toString() == ''){
          this.isEmpty = true;
          this.alertService.info('You have no events yet.',true);
        }
      });
  }

  getInvitesByCustId(): void {
    this.eventService.getInvitesByCustId()
      .subscribe((events) => {
        this.events = events;
        if(events.toString() == ''){
          this.isEmpty = true;
          this.alertService.info('You have no invites yet.',true);
        }
      });
  }

  importToPDF(): void {
    this.eventService.importEventsToPDF(this.currentUser.email)
      .subscribe(() => {
        this.alertService.info('You imported your events');
      })
  }

  getCreatedEvents(): void {
    Object.assign(this.backupEventsDTO, this.eventsDTO);
    this.filteredEventsDTO = this.eventsDTO.filter(event =>
      event.event.creatorId === this.currentId
    );
    this.eventsDTO = this.filteredEventsDTO;
  }

  chooseCreatedCheckBox(){
    if (this.checkBoxOrder == 1)
      this.getCreatedEvents();
    else {
      this.eventsDTO = this.backupEventsDTO;
    }
    this.checkBoxOrder = this.checkBoxOrder * (-1);
  }

  setPage(page: number) {
    // get pager object from service
    this.pager = this.eventService.getPager(this.eventsDTO.length, page);

    // get current page of items
    this.pagedItems = this.eventsDTO.slice(this.pager.startIndex, this.pager.endIndex + 1);
  }
}
