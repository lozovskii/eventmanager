import { Component, OnInit } from '@angular/core';
import {AlertService} from "../../../_services/alert.service";
import {EventService} from "../../../_services/index";
import {ActivatedRoute} from "@angular/router";
import {Event} from "../../../_models/index";

@Component({
  selector: 'app-draft-list',
  templateUrl: './draft-list.component.html',
  styleUrls: ['./draft-list.component.css']
})
export class DraftListComponent implements OnInit {
  events: Event[];
  isEmpty: boolean = false;
  constructor(private eventService: EventService,
              private activatedRoute: ActivatedRoute,
              private alertService: AlertService) { }

  ngOnInit() {
    this.getDraftsByCustId();
  }

  getDraftsByCustId(): void {
    this.eventService.getDraftsByCustId()
      .subscribe((events) => {
        this.events = events;
        console.log(this.events);
        if(events.toString() == ''){
          this.isEmpty = true;
          this.alertService.info('You have no drafts yet.',true);
        }
      });
  }
}
