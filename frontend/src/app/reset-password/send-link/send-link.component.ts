import { Component, OnInit } from '@angular/core';
import {ResetPasswordService} from "../../_services/reset-password.service";
import {AlertService} from "../../_services";

@Component({
  selector: 'app-send-link',
  templateUrl: './send-link.component.html',
  styleUrls: ['./send-link.component.css']
})
export class SendLinkComponent implements OnInit {
  model: any = {};
  loading = false;

  constructor(private alertService: AlertService,
              private resetService: ResetPasswordService) { }

  ngOnInit() {
  }

  sendLink() {
    this.loading=true;
    this.resetService.sendEmail(this.model.email)
      .subscribe(() => {
        console.log('success');
        this.alertService.success("Email with link for resetting password sent.");
        },
        (error) => {
        console.log(error.error);
        this.alertService.error(error.error);
      });
    this.loading=false;
  }
}
