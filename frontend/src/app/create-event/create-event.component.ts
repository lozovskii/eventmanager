import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Event} from "../_models/event";
import {AlertService} from "../_services/alert.service";
import {Router} from "@angular/router";
import {EventService} from "../_services/event.service";
import {UserService} from "../_services/user.service";
import {VISIBILITY} from "../event-visibility";

@Component({
  selector: 'app-event',
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.css']
})
export class CreateEventComponent implements OnInit {
  event: Event;
  loading = false;
  eventForm: FormGroup;
  visibility: any[] = VISIBILITY;
  vis: any;
  selectedPeople: string[] = [];

  constructor(private router: Router,
              private eventService: EventService,
              private alertService: AlertService,
              private formBuilder: FormBuilder,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.eventForm = this.formBuilder.group({
      name: [''],
      groupId: [''],
      folderId: [''],
      creatorId: [''],
      day: [''],
      startTime: [''],
      endTime: [''],
      priority: [''],
      visibility: [''],
      frequencyValue: [''],
      frequency: [''],
      description: [''],
      status: [''],
      people: [''],
      vis: ['']
    });
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

  create(eventFromForm: Event) {
    if ((eventFromForm.day != null) && (eventFromForm.day != '')) {
      eventFromForm.startTime = eventFromForm.day + ' ' + eventFromForm.startTime + ':00';
      eventFromForm.endTime = eventFromForm.day + ' ' + eventFromForm.endTime + ':00';
      eventFromForm.frequency = eventFromForm.frequencyValue + '-' + eventFromForm.frequency;
    }
    let currentId2 = this.userService.getCurrentId();
    eventFromForm.vis = this.vis.name;
    eventFromForm.people = this.selectedPeople;
    console.log(eventFromForm.vis);
    eventFromForm.creatorId = currentId2;
    console.log('event: ' + JSON.stringify(eventFromForm));
    this.loading = true;
    this.eventService.create(eventFromForm)
      .subscribe(
        data => {
          if ((eventFromForm.day != null) && (eventFromForm.day != '')) {
            if (eventFromForm.visibility == 'PUBLIC') {
              this.alertService.success('Public event successfully created! You can invite people to your event.', true);
              this.router.navigate(['/add-event-participants']);
            } else {
              this.alertService.success('Event successfully created!', true);
              this.router.navigate(['/content']);
            }
            // }
          } else {
            this.alertService.success('Note successfully created!', true);
            this.router.navigate(['/content']);
          }
        },
        error => {
          this.alertService.error('Something wrong! Please try again!');
          this.loading = false;
        });
  }

}
