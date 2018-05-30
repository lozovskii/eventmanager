import {Component, OnInit} from '@angular/core';
import {AlertService, RegistrationService} from "../../_services";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-registration-confirm',
  template:''
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
        this.alertService.success('Your email is verified! Please, login with your credentials',true);
        return this.router.navigate(['/']);
      },
      (error) => {
        this.alertService.error(error.error, true);
        return this.router.navigate(['/']);
      });
  }

  getParams() {
    this.activatedRouter.queryParams.subscribe(params => {
      this.token = params['token'];
    });
  }

}
