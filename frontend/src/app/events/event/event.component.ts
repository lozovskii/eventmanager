import {Component, Input, OnInit} from '@angular/core';
import {EventService} from "../../_services";
import {ActivatedRoute, Router} from "@angular/router";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {AlertService} from "../../_services/alert.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements OnInit {
   @Input('eventId') eventId: string;

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
    const id = this.eventId;
    this.eventService.getEventById(id).subscribe((eventDTO) => {
      this.eventDTO = eventDTO;
      console.log(this.eventDTO);
      console.log(this.eventDTO.additionEvent.startTimeNotification);
      let currentUserId = JSON.parse(sessionStorage.getItem('currentUser')).id;
      this.isCreator = currentUserId == this.eventDTO.event.creatorId;
      console.log('eventId = ' + this.eventDTO.event.id);
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


}
