import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {AlertService, EventService, UserService} from "../../_services";
import {Router} from "@angular/router";
import {VISIBILITY} from "../../event-visibility";

@Component({
  selector: 'app-event-create',
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.css']
})
export class CreateEventComponent implements OnInit {


  eventForm: FormGroup = this.initEventForm();
  additionEventForm: FormGroup = this.initAdditionEventForm();

  eventDTOForm: FormGroup = this.formBuilder.group({
    event: this.eventForm,
    additionEvent: this.additionEventForm
  });
  isValidFormSubmitted = null;

  visibilityList: any[] = VISIBILITY;
  visibility: string;
  selectedPeople: string[] = [];

  constructor(private router: Router,
              private eventService: EventService,
              private alertService: AlertService,
              private formBuilder: FormBuilder,
              private userService: UserService) {
  }

  ngOnInit(): void {
    console.log(this.eventForm);
    console.log(this.additionEventForm);
    console.log(this.eventDTOForm);
  }

  initEventForm(): FormGroup {
    return this.formBuilder.group({
      name : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      description: new FormControl(''),
      day: new FormControl(''),
      startTime: new FormControl(''),
      endTime: new FormControl(''),
      visibility: new FormControl('')
    })
  }

  initAdditionEventForm(): FormGroup {
    return this.formBuilder.group({
      frequencyNumber: ['', [Validators.min(0)]],
      frequencyPeriod: new FormControl(''),
      priority: new FormControl(''),
      people: new FormControl('')
    })
  }

  addUserToEvent(manLogin: string) {
    if (!this.selectedPeople.includes(manLogin)) {
      this.selectedPeople.push(manLogin);
    }
  }

  deleteUserFromEvent(login: string) {
    if (this.selectedPeople.includes(login)) {
      const index = this.selectedPeople.indexOf(login);
      this.selectedPeople.splice(index, 1);
    }
  }

  createEventForm(eventDTO: EventDTOModel) {

    this.isValidFormSubmitted = false;

    if (this.eventForm.invalid || this.additionEventForm.invalid) {
      return;
    }
    this.isValidFormSubmitted = true;

    if ((eventDTO.event.day != null) && (eventDTO.event.day != '')) {
      eventDTO.event.startTime = eventDTO.event.day + ' ' + eventDTO.event.startTime + ':00';
      eventDTO.event.endTime = eventDTO.event.day + ' ' + eventDTO.event.endTime + ':00';
    }else{
      eventDTO.event.startTime = null;
      eventDTO.event.endTime = null;
    }
    eventDTO.additionEvent.people = this.selectedPeople;
    let customerId = this.userService.getCurrentId();
    eventDTO.event.creatorId = customerId;
    console.log(JSON.stringify(eventDTO));
    this.eventService.create(eventDTO).subscribe(
      data => {
        if ((eventDTO.event.day != null) && (eventDTO.event.day != '')) {
          if (eventDTO.event.visibility == 'PUBLIC') {
            this.alertService.success('Public event successfully created! You can invite people to your event.', true);
            this.router.navigate(['../home']);
          } else {
            this.alertService.success('Event successfully created!', true);
            this.router.navigate(['../home']);
          }
        } else {
          this.alertService.success('Note successfully created!', true);
          this.router.navigate(['../folder-list']);
        }
      },
      error => {
        this.alertService.error('Something wrong! Please try again!');
      });
  }

  saveAsADraft(eventDTO: EventDTOModel){
    if ((eventDTO.event.day != null) && (eventDTO.event.day != '')) {
      eventDTO.event.startTime = eventDTO.event.day + ' ' + eventDTO.event.startTime + ':00';
      eventDTO.event.endTime = eventDTO.event.day + ' ' + eventDTO.event.endTime + ':00';
    }else{
      eventDTO.event.startTime = null;
      eventDTO.event.endTime = null;
    }
    eventDTO.event.status = 'DRAFT';
    eventDTO.additionEvent.people = this.selectedPeople;
    let customerId = this.userService.getCurrentId();
    eventDTO.event.creatorId = customerId;
    this.eventService.create(eventDTO).subscribe(data => {
        this.alertService.success('Draft successfully saved!', true);
        this.router.navigate(['../home']);
      },
      error => {
        this.alertService.error('Not saved! We working.. please try again');
      });
  }

  get name() {
    return this.eventForm.get('name');
  }

  get frequencyNumber() {
    return this.additionEventForm.get('frequencyNumber');
  }
}
