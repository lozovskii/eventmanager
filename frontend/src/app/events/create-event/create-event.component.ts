import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {AlertService, EventService, UserService} from "../../_services";
import {ActivatedRoute, Router} from "@angular/router";
import {VISIBILITY} from "../../event-visibility";
import {Location} from "../../_models/location";
import {LocationService} from "../../_services/location.service";

@Component({
  selector: 'app-event-create',
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.css']
})
export class CreateEventComponent implements OnInit {
  @Input('noteId') noteId: string;

  eventDTO: EventDTOModel;
  eventForm: FormGroup;
  additionEventForm: FormGroup;
  eventDTOForm: FormGroup;
  isNote:boolean = false;

  nameNote: string;
  descriptionNote:string;

  isValidFormSubmitted = null;

  visibilityList: any[] = VISIBILITY;
  visibility: string;
  selectedPeople: string[] = [];
  eventLocation: Location;

  constructor(private router: Router,
              private eventService: EventService,
              private activatedRoute: ActivatedRoute,
              private alertService: AlertService,
              private formBuilder: FormBuilder,
              private userService: UserService,
              private locationService : LocationService) {
  }

  ngOnInit(): void {
    this.eventForm = this.initEventForm();
    this.additionEventForm = this.initAdditionEventForm();
    this.eventDTOForm = this.formBuilder.group({
      event: this.eventForm,
      additionEvent: this.additionEventForm
    });
    this.activatedRoute.params.subscribe(params => {
      let type = params['type'];
      switch (type) {
        case 'note' : {
          this.isNote = true;
          this.noteId = this.activatedRoute.snapshot.paramMap.get('id');
          this.getNoteById();
          break;
        }
      }
    });
  }

  initEventForm(): FormGroup {
    return this.formBuilder.group({
      name : new FormControl(),
      description: new FormControl(),
      day: new FormControl(),
      startTime: new FormControl(),
      endTime: new FormControl(),
      visibility: new FormControl()
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
    if(eventDTO.event.name == null){
      eventDTO.event.name = this.eventDTO.event.name;
    }
    if(eventDTO.event.description == null){
      eventDTO.event.description = this.eventDTO.event.description;
    }
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
    eventDTO.additionEvent.location = this.eventLocation;
    let customerId = this.userService.getCurrentId();
    eventDTO.event.creatorId = customerId;
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
    if(eventDTO.event.name == null){
      eventDTO.event.name = this.eventDTO.event.name;
    }
    if(eventDTO.event.description == null){
      eventDTO.event.description = this.eventDTO.event.description;
    }
    this.isValidFormSubmitted = true;
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

  getNoteById(): void {
    this.eventService.getNoteById(this.noteId)
      .subscribe((eventDTO) => {
        this.eventDTO = eventDTO;
        this.nameNote = this.eventDTO.event.name;
        this.descriptionNote = this.eventDTO.event.description;
      });
  }

  addLocation(location: Location) {
    this.eventLocation = location;
    console.log('create-event ' + this.eventLocation.street);
    console.log('create-event ' + this.eventLocation.house);

  }

}
