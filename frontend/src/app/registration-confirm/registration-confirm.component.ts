import { Component, OnInit } from '@angular/core';
import {RegistrationService} from "../_services/registration.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-registration-confirm',
  templateUrl: './registration-confirm.component.html',
  styleUrls: ['./registration-confirm.component.css']
})
export class RegistrationConfirmComponent implements OnInit {
  token : string;

  constructor( private activatedRouter: ActivatedRoute,
               private router: Router,
               private registrationService : RegistrationService) { }

  ngOnInit() {
    this.getParams();
    this.registrationService.verifyEmail(this.token).subscribe((data) => {
        alert('Your email is verified!');
    },
      (error) => {
        alert(error.error);
        return this.router.navigate(["/"]);

      })
  }

  getParams() {
    this.activatedRouter.queryParams.subscribe(params => {
      this.token = params['token'];
    });
  }

}
