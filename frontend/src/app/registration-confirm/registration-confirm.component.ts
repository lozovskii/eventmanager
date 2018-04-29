import { Component, OnInit } from '@angular/core';
import {RegistrationService} from "../_services/registration.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AlertService} from "../_services";

@Component({
  selector: 'app-registration-confirm',
  templateUrl: './registration-confirm.component.html',
  styleUrls: ['./registration-confirm.component.css']
})
export class RegistrationConfirmComponent implements OnInit {
  token : string;

  constructor( private activatedRouter: ActivatedRoute,
               private router: Router,
               private alertService: AlertService,
               private registrationService : RegistrationService) { }

  ngOnInit() {
    this.getParams();
    this.registrationService.verifyEmail(this.token).subscribe((data) => {
      this.alertService.success('Your email is verified! Please, login with your credentials');
//        return this.router.navigate(["/login"]);
    },
      (error) => {

        this.alertService.error(error.error, true);
//        return this.router.navigate(["/"]);

      });
  }

  getParams() {
    this.activatedRouter.queryParams.subscribe(params => {
      this.token = params['token'];
    });
  }

}
