import {Component, OnInit} from '@angular/core';
import {AlertService, RegistrationService} from "../_services";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-registration-confirm',
  templateUrl: './registration-confirm.component.html',
  styleUrls: ['./registration-confirm.component.css']
})
export class RegistrationConfirmComponent implements OnInit {
  token: string;

  constructor(private activatedRouter: ActivatedRoute,
              private router: Router,
              private alertService: AlertService,
              private registrationService: RegistrationService) {
  }

  ngOnInit() {
    this.getParams();
    this.registrationService.verifyEmail(this.token).subscribe(() => {
        this.alertService.success('Your email is verified! Please, login with your credentials');
      },
      (error) => {
        this.alertService.error(error.error, true);
      });
  }

  getParams() {
    this.activatedRouter.queryParams.subscribe(params => {
      this.token = params['token'];
    });
  }

}
