import {Component, OnInit} from '@angular/core';
import {EventService} from "../../_services";
import {ActivatedRoute, Router} from "@angular/router";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {AlertService} from "../../_services/alert.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Event} from '../../_models/event'
import {AdditionEventModel} from "../../_models/additionEvent.model";
import {UpdateEventComponent} from "../update-event/update-event.component";

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements OnInit {
  updateEventDTO: UpdateEventComponent;
  eventDTO: EventDTOModel;
  isCreator: boolean;
  isParticipant: boolean;
  additionEventForm: FormGroup;

  constructor(private eventService: EventService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private alertService : AlertService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.initAdditionEventForm();
    this.isCreator = false;
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    this.eventService.getEventById(id).subscribe((eventDTO) => {
      this.eventDTO = eventDTO;
      console.log(this.eventDTO);
      console.log(this.eventDTO.additionEvent.startTimeNotification);
      let currentUserId = JSON.parse(sessionStorage.getItem('currentUser')).id;
      this.isCreator = currentUserId == this.eventDTO.event.creatorId;
      console.log('eventId = ' + this.eventDTO.event.id);
      this.eventService.isParticipant(currentUserId,this.eventDTO.event.id).subscribe(()=>{this.isParticipant=true}, ()=>{this.isParticipant=false})

    });
  }

  initAdditionEventForm(): FormGroup {
    return this.additionEventForm = this.formBuilder.group({
      startTimeNotification: new FormControl()
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

  changeNotStartTime(additionEvent:AdditionEventModel){
    console.log('additionEvent = ' + JSON.stringify(additionEvent));
    if (additionEvent.startTimeNotification != null) {
      this.eventDTO.additionEvent.startTimeNotification = (additionEvent.startTimeNotification).slice(0, 10) + ' ' + (additionEvent.startTimeNotification).slice(11, 16) + ':00';
    }
    console.log('additionEvent = ' + JSON.stringify(this.eventDTO.additionEvent));
    this.eventService.updateEventNotif(this.eventDTO).subscribe(() => {
      this.alertService.info('Notification time successfully set!',true);
      this.router.navigate(['event','my'])});
  }
}
