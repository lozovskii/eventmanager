import {Component, OnInit} from '@angular/core';
import {EventService} from "../../_services";
import {ActivatedRoute, Router} from "@angular/router";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {AlertService} from "../../_services/alert.service";

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements OnInit {
  eventDTO: EventDTOModel;
  isCreator: boolean;
  isParticipant: boolean;

  constructor(private eventService: EventService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private alertService : AlertService) {
  }

  ngOnInit() {
    this.isCreator = false;
    this.activatedRoute.params.subscribe(params => {
      let eventId = params['id'];
      this.eventService.getEventById(eventId)
        .subscribe((event) => {
          this.eventDTO = new EventDTOModel();
          this.eventDTO.event = event;

          let currentUserId = JSON.parse(sessionStorage.getItem('currentUser')).id;
          this.isCreator = currentUserId == this.eventDTO.event.creatorId;
          console.log(this.eventDTO.event.creatorId);
          this.eventService.isParticipant(currentUserId,this.eventDTO.event.id).subscribe(()=>{this.isParticipant=true}, ()=>{this.isParticipant=false})
        });
    });
  }

  addParticipant() {
    this.eventService.addParticipant(this.eventDTO.event.id).subscribe(() => {
      this.isParticipant = true;
      //this.eventDTO.people.push(JSON.parse(sessionStorage.getItem('currentUser')).id);
    });
  }

  removeParticipant() {
    this.eventService.removeParticipant(this.eventDTO.event.id).subscribe(() => {
      this.isParticipant = false;
      // const index = this.eventDTO.people.indexOf(JSON.parse(sessionStorage.getItem('currentUser')).id);
      // if (index>-1) {
      //   this.eventDTO.people.slice(index);
      // }
    });
  }

  delete() {
    this.eventService.deleteEvent(this.eventDTO.event.id).subscribe(() => {
      this.alertService.info('Event successfully deleted!',true);
      this.router.navigate(['../home']);
    });
  }
}
