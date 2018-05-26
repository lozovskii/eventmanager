import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {AlertService, EventService, UserService} from "../../_services";
import {ActivatedRoute, Router} from "@angular/router";
import {VISIBILITY} from "../../event-visibility";
import {Location} from "../../_models/location";

@Component({
  selector: 'app-event-create',
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.css']
})
export class CreateEventComponent implements OnInit {
  @Input('noteId') noteId: string;
  @Input('eventId') eventId: string;

  eventDTO: EventDTOModel;
  eventForm: FormGroup;
  additionEventForm: FormGroup;
  eventDTOForm: FormGroup;
  isNote:boolean = false;
  isDraft:boolean = false;

  nameNote: string;
  descriptionNote:string;
  startDateDraft:string;
  endDateDraft:string;
  visibilityDraft:string;
  priorityDraft:string;
  peopleDraft:string[];

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
              private userService: UserService) {
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
        case 'draft' : {
          this.isDraft = true;
          this.getDraftById();
          break;
        }
      }
    });
  }

  initEventForm(): FormGroup {
    return this.formBuilder.group({
      name : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(40)]],
      description: ['', [Validators.maxLength(2048)]],
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
    // this.isValidFormSubmitted = false;
    // if (this.eventForm.invalid || this.additionEventForm.invalid) {
    //   return;
    // }
    // this.isValidFormSubmitted = true;
    if (eventDTO.event.startTime != null) {
      eventDTO.event.startTime = (eventDTO.event.startTime).slice(0, 10) + ' ' + (eventDTO.event.startTime).slice(11, 16) + ':00';
    }
    if (eventDTO.event.endTime != null) {
      eventDTO.event.endTime = (eventDTO.event.endTime).slice(0, 10) + ' ' + (eventDTO.event.endTime).slice(11, 16) + ':00';
    }else{
      eventDTO.event.startTime = this.startDateDraft;
      eventDTO.event.endTime = this.endDateDraft;
    }
    if((eventDTO.event.visibility ==null) && (this.visibilityDraft !=null)){
      eventDTO.event.visibility = this.visibilityDraft;
    }
    if((eventDTO.additionEvent.priority ==null) && (this.priorityDraft !=null)){
      eventDTO.additionEvent.priority = this.priorityDraft;
    }
    if((eventDTO.additionEvent.people ==null) && (this.peopleDraft !=null)){
      eventDTO.additionEvent.people = this.peopleDraft;
    }
    eventDTO.additionEvent.people = this.selectedPeople;
    eventDTO.additionEvent.location = this.eventLocation;
    let customerId = this.userService.getCurrentId();
    eventDTO.event.creatorId = customerId;
    this.eventService.create(eventDTO).subscribe(
      data => {
        if ((eventDTO.event.startTime != null) && (eventDTO.event.startTime != '')) {
          if (eventDTO.event.visibility == 'PUBLIC') {
            this.alertService.info('Public event successfully created! You can invite people to your event.', true);
            this.router.navigate(['../home']);
          } else {
            this.alertService.info('Event successfully created!', true);
            this.router.navigate(['../home']);
          }
        } else {
          this.alertService.info('Note successfully created!', true);
          this.router.navigate(['../folder-list', 'all']);
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
    if (eventDTO.event.startTime != null) {
      this.eventDTO.event.startTime = (eventDTO.event.startTime).slice(0, 10) + ' ' +
        (eventDTO.event.startTime).slice(11, 16) + ':00';
    }
    if (eventDTO.event.endTime != null) {
      this.eventDTO.event.endTime = (eventDTO.event.endTime).slice(0, 10) + ' ' +
        (eventDTO.event.endTime).slice(11, 16) + ':00';
    }else{
      eventDTO.event.startTime = this.startDateDraft;
      eventDTO.event.endTime = this.endDateDraft;
    }
    eventDTO.event.status = 'DRAFT';
    eventDTO.additionEvent.people = this.selectedPeople;
    eventDTO.additionEvent.location = this.eventLocation;
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

  get description() {
    return this.eventForm.get('description');
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

  getDraftById(): void {
    this.eventId = this.activatedRoute.snapshot.paramMap.get('id');
    const id = this.eventId;
    this.eventService.getEventById(id).subscribe((eventDTO : EventDTOModel) => {
      this.eventDTO = eventDTO;
      this.isNote = true;
      this.startDateDraft = this.eventDTO.event.startTime;
      this.endDateDraft = this.eventDTO.event.endTime;
      this.visibilityDraft = this.eventDTO.event.visibility;
    });
  }

  addLocation(location: Location) {
    this.eventLocation = location;
    console.log('create-event ' + this.eventLocation.street);
    console.log('create-event ' + this.eventLocation.house);
  }

}
