import {Component, Input, OnInit} from '@angular/core';
import {EventDTOModel} from "../../../_models/dto/eventDTOModel";
import {EventService} from "../../../_services";
import {ActivatedRoute, Router} from "@angular/router";
import {AlertService} from "../../../_services/alert.service";

@Component({
  selector: 'app-draft-content',
  templateUrl: './draft-content.component.html',
  styleUrls: ['./draft-content.component.css']
})
export class DraftContentComponent implements OnInit {
  @Input('eventId') eventId: string;

  eventDTO: EventDTOModel;
  isCreator: boolean;
  zoom: number = 16;

  constructor(private eventService: EventService,
              private activatedRoute: ActivatedRoute,
              private alertService: AlertService,
              private router: Router) {
  }

  ngOnInit() {
    this.eventId = this.activatedRoute.snapshot.paramMap.get('id');
    this.isCreator = false;
    const id = this.eventId;
    this.eventService.getEventById(id).subscribe((eventDTO : EventDTOModel) => {
      this.eventDTO = eventDTO;
      let currentUserId = JSON.parse(sessionStorage.getItem('currentUser')).id;
      this.isCreator = currentUserId == this.eventDTO.event.creatorId;
    });
  }

  delete(){
    this.eventService.deleteEvent(this.eventDTO.event.id).subscribe(() => {
      this.alertService.info('Draft successfully deleted!', true);
      this.router.navigate(['../draft-list']);
    });
  }

}
