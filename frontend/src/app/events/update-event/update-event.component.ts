import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {AlertService} from "../../_services/alert.service";
import {ActivatedRoute, Router} from "@angular/router";
import {EventService} from "../../_services/event.service";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {UpdateEventDTO} from "../../_models/dto/UpdateEventDTO";
import {Location} from "../../_models/location";
import {tap} from "rxjs/operators";

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
  eventLocation: Location;
  isLocationExist = false;

  isValidFormSubmitted: boolean;

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

  get startTime() {
    return this.eventForm.get('startTime');
  }

  get endTime() {
    return this.eventForm.get('endTime');
  }

  get name() {
    return this.eventForm.get('name');
  }

  get description() {
    return this.eventForm.get('description');
  }

  ngOnInit() {
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    this.eventService.getEventById(id)
      .pipe(tap(eventDTO => this.eventDTOForm.patchValue(eventDTO)))
      .subscribe((eventDTO) => {
      this.eventDTO = eventDTO;
      if (this.eventDTO.additionEvent.people != null) {
        this.people = eventDTO.additionEvent.people;
      }
      this.checkLocationUpdate();
    });
  }

  initEventForm(): FormGroup {
    return this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(40)]],
      description: ['', [Validators.maxLength(2048)]],
      day: new FormControl(),
      startTime: new FormControl(),
      endTime: new FormControl(),
      visibility: new FormControl(),
      eventLocation: new FormControl()
    }, {validator: [this.dateLessThan('startTime', 'endTime'), this.bothOrNone('startTime', 'endTime'), this.dateBeforeNow('startTime')]})
  }

  initAdditionEventForm(): FormGroup {
    return this.formBuilder.group({
      priority: new FormControl(),
      people: new FormControl(),
      eventLocation: new FormControl()
    })
  }

  update(eventDTO: EventDTOModel) {
    this.isValidFormSubmitted = false;
    if (this.eventForm.invalid || this.additionEventForm.invalid) {
      this.alertService.error("You input is wrong. Please, check and try again", false);
      return;
    }
    this.isValidFormSubmitted = true;
    eventDTO.event.id = this.currentEventId;
    this.updateEventDTO.event = this.eventDTO.event;
    this.updateEventDTO.priority = this.eventDTO.additionEvent.priority;
    if (eventDTO.event.startTime != null) {
      this.updateEventDTO.event.startTime = (eventDTO.event.startTime).slice(0, 10) + ' ' + (eventDTO.event.startTime).slice(11, 16) + ':00';
    }
    if (eventDTO.event.endTime != null) {
      this.updateEventDTO.event.endTime = (eventDTO.event.endTime).slice(0, 10) + ' ' + (eventDTO.event.endTime).slice(11, 16) + ':00';
    }
    if (eventDTO.event.name == "") {
      this.updateEventDTO.event.name = this.eventDTO.event.name;
    } else {
      this.updateEventDTO.event.name = eventDTO.event.name;
    }
    if (eventDTO.event.description == "") {
      this.updateEventDTO.event.description = this.eventDTO.event.description;
    } else {
      this.updateEventDTO.event.description = eventDTO.event.description;
    }
    if (eventDTO.additionEvent.priority != null) {
      this.updateEventDTO.priority = eventDTO.additionEvent.priority;
    }
    this.updateEventDTO.newPeople = this.newPeople;
    this.updateEventDTO.removedPeople = this.removedPeople;
    this.eventLocation.event_id = this.eventDTO.event.id;
    this.updateEventDTO.location = this.eventLocation;
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

  addLocation(location: Location) {
    this.eventLocation = location;
  }

  dateBeforeNow(start: string) {
    return (group: FormGroup): {
      [key: string]: any
    } => {
      let startTime = group.controls[start].value;
      return Date.parse(startTime) < Date.now() ? {dateBeforeNow: "Event cannot starts in the past"} : {}
    }
  }

  bothOrNone(start: string, end: string) {
    return (group: FormGroup): {
      [key: string]: any
    } => {
      let startTime = group.controls[start].value;
      let endTime = group.controls[end].value;
      return Boolean(startTime) !== Boolean(endTime) ? {onlyOne: "You must specify both dates or none of them (for note)"} : {}
    }

  }

  dateLessThan(from: string, to: string) {
    return (group: FormGroup): { [key: string]: any } => {
      let startTime = group.controls[from].value;
      let endTime = group.controls[to].value;
      if (Boolean(startTime) && Boolean(endTime) && startTime >= endTime) {
        return {
          endLessThanStart: "Start time should be less than end time"
        };
      }
      return {};
    }
  }

  private removeElementFromArray(array: any[], value: any) {
    let index = this.people.indexOf(value, 0);
    if (index > -1) {
      array.splice(index, 1);
    }
  }

  addLocation(location: Location) {
    this.eventLocation = location;
  }

  get name() {
    return this.eventForm.get('name');
  }

  get description() {
    return this.eventForm.get('description');
  }

  checkLocationUpdate() {
    console.log('in ngInin is Locatoin: '+this.eventDTO.additionEvent.location);
    if(this.eventDTO.additionEvent.location !== null) {
      this.isLocationExist = true;
    }
  }

}
