import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {AlertService} from "../../_services/alert.service";
import {ActivatedRoute, Router} from "@angular/router";
import {EventService} from "../../_services/event.service";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {UpdateEventDTO} from "../../_models/dto/UpdateEventDTO";

@Component({
  selector: 'app-update-event',
  templateUrl: './update-event.component.html',
  styleUrls: ['./update-event.component.css']
})
export class UpdateEventComponent implements OnInit {
  eventDTO: EventDTOModel;

  updateEventDTO: UpdateEventDTO = new UpdateEventDTO();
  people: string[] = [];
  newPeople: string[] = [];
  removedPeople: string[] = [];

  canEdit: boolean;
  currentEventId: string;
  eventForm: FormGroup = this.initEventForm();
  additionEventForm: FormGroup = this.initAdditionEventForm();

  eventDTOForm: FormGroup = this.formBuilder.group({
    event: this.eventForm,
    additionEvent: this.additionEventForm
  });

  constructor(private router: Router,
              private eventService: EventService,
              private alertService: AlertService,
              private formBuilder: FormBuilder,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    this.eventService.getEventById(id).subscribe((eventDTO) => {
      this.eventDTO = eventDTO;
      console.log(this.eventDTO);
      if (this.eventDTO.additionEvent.people != null) {
        this.people = eventDTO.additionEvent.people;
      }

    });
  }

  initEventForm(): FormGroup {
    return this.formBuilder.group({
      name: new FormControl(),
      description: new FormControl(),
      day: new FormControl(),
      startTime: new FormControl(),
      endTime: new FormControl(),
      visibility: new FormControl(),
    })
  }

  initAdditionEventForm(): FormGroup {
    return this.formBuilder.group({
      frequencyNumber: new FormControl(),
      frequencyPeriod: new FormControl(),
      priority: new FormControl(),
      people: new FormControl()
    })
  }

  update(eventDTO: EventDTOModel) {
    eventDTO.event.id = this.currentEventId;
    this.updateEventDTO.event = this.eventDTO.event;
    this.updateEventDTO.priority = this.eventDTO.additionEvent.priority;
    if (eventDTO.event.startTime != null) {
      this.updateEventDTO.event.startTime = (eventDTO.event.startTime).slice(0, 10) + ' ' + (eventDTO.event.startTime).slice(11, 16) + ':00';
    }
    if (eventDTO.event.endTime != null) {
      this.updateEventDTO.event.endTime = (eventDTO.event.endTime).slice(0, 10) + ' ' + (eventDTO.event.endTime).slice(11, 16) + ':00';
    }
    if (eventDTO.event.name != null) {
      this.updateEventDTO.event.name = eventDTO.event.name;
    }
    if (eventDTO.event.description != null) {
      this.updateEventDTO.event.description = eventDTO.event.description;
    }
    if (eventDTO.additionEvent.priority != null) {
      this.updateEventDTO.priority = eventDTO.additionEvent.priority;
    }

    this.updateEventDTO.newPeople = this.newPeople;
    this.updateEventDTO.removedPeople = this.removedPeople;
    console.log("before update > " + JSON.stringify(this.updateEventDTO));
    this.eventService.updateEvent(this.updateEventDTO)
      .subscribe(() => {
        this.alertService.info('Event successfully updated!', true);
        this.router.navigate(['../home']);
      });
  }

  addUserToEvent(login) {
    if (!this.people.includes(login)) {
      this.newPeople.push(login);
      this.people.push(login);
    }
  }

  deleteUserFromEvent(login) {
    this.removeElementFromArray(this.people, login);
    this.removeElementFromArray(this.newPeople, login);
    this.removedPeople.push(login);
  }

  private removeElementFromArray(array: any[], value: any) {
    let index = this.people.indexOf(value, 0);
    if (index > -1) {
      array.splice(index, 1);
    }
  }

}
