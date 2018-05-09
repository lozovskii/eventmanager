import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {AlertService} from "../../_services/alert.service";
import {ActivatedRoute, Router} from "@angular/router";
import {EventService} from "../../_services/event.service";
import {UserService} from "../../_services/user.service";
import {Event} from "../../_models";

@Component({
  selector: 'app-update-event',
  templateUrl: './update-event.component.html',
  styleUrls: ['./update-event.component.css']
})
export class UpdateEventComponent implements OnInit {
  eventForm: FormGroup;
  event: Event;
  canEdit: boolean;

  constructor(private router: Router,
              private eventService: EventService,
              private alertService: AlertService,
              private formBuilder: FormBuilder,
              private userService: UserService,
              private activatedRoute: ActivatedRoute) {
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
      status: ['']
    });

    this.activatedRoute.params.subscribe(params => {
      let eventId = params['id'];
      this.eventService.getEventById(eventId)
        .subscribe((event) => {
          this.event = event;
          console.log(this.event);

          this.canEdit = JSON.parse(sessionStorage.getItem('currentUser')).id == this.event.creatorId;

          if(!this.canEdit) {
            this.alertService.error("You don't have permission to edit this event");
          }
        });
    });
  }

  update(){

  }
}


