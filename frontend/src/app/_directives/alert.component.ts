import { Component, OnInit } from '@angular/core';

import { AlertService } from '../_services/index';

@Component({
  moduleId: module.id.toString(),
  selector: 'alert',
  styleUrls: ['./alert.component.css'],
  templateUrl: 'alert.component.html'
})

export class AlertComponent {
  message: any;

  constructor(private alertService: AlertService) { }

  ngOnInit() {
    this.alertService.getMessage().subscribe(message => { this.message = message; });
  }
}
