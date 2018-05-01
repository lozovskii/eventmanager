import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {EventDTOModel} from "../_models/dto/eventDTOModel";
import {AlertService, EventService, UserService} from "../_services";
import {Router} from "@angular/router";
import {VISIBILITY} from "../event-visibility";

@Component({
  selector: 'app-event',
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.css']
})
export class CreateEventComponent implements OnInit {
  eventDTOForm: FormGroup;
  eventForm: FormGroup;
  additionEventForm: FormGroup;

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
    this.eventDTOForm = this.formBuilder.group({
      event: this.initEventForm(),
      additionEvent: this.initAdditionEventForm()
    });
  }

  initEventForm(): FormGroup {
    return this.eventForm = this.formBuilder.group({
      name: new FormControl(''),
      description: new FormControl(''),
      day: new FormControl(''),
      startTime: new FormControl(''),
      endTime: new FormControl(''),
      visibility: new FormControl('')
    })
  }

  initAdditionEventForm(): FormGroup {
    return this.additionEventForm = this.formBuilder.group({
      frequencyNumber: new FormControl(''),
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
    if ((eventDTO.event.day != null) && (eventDTO.event.day != '')) {
      eventDTO.event.startTime = eventDTO.event.day + ' ' + eventDTO.event.startTime + ':00';
      eventDTO.event.endTime = eventDTO.event.day + ' ' + eventDTO.event.endTime + ':00';
    }
    eventDTO.additionEvent.people = this.selectedPeople;
    console.log(JSON.stringify(eventDTO));
    this.eventService.create(eventDTO).subscribe(
      data => {
        if ((eventDTO.event.day != null) && (eventDTO.event.day != '')) {
          if (eventDTO.event.visibility == 'PUBLIC') {
            this.alertService.success('Public event successfully created! You can invite people to your event.', true);
            this.router.navigate(['/add-event-participants']);
          } else {
            this.alertService.success('Event successfully created!', true);
            this.router.navigate(['/content']);
          }
        } else {
          this.alertService.success('Note successfully created!', true);
          this.router.navigate(['/content']);
        }
      },
      error => {
        this.alertService.error('Something wrong! Please try again!');
      });
  }
}
