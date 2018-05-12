import { Component, OnInit } from '@angular/core';
import {AdditionEventModel} from "../../_models/additionEvent.model";
import {AlertService} from "../../_services/alert.service";
import {ActivatedRoute, Router} from "@angular/router";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {EventService} from "../../_services";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-event-notification',
  templateUrl: './event-notification.component.html',
  styleUrls: ['./event-notification.component.css']
})
export class EventNotificationComponent implements OnInit {
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
