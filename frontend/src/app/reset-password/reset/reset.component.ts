import { Component, OnInit } from '@angular/core';
import {ResetPasswordService} from "../../_services/reset-password.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AlertService} from "../../_services";

@Component({
  selector: 'app-reset',
  templateUrl: './reset.component.html',
  styleUrls: ['./reset.component.css']
})
export class ResetComponent implements OnInit {
  isTokenValid: boolean;
  token: string;
  newPassword: string;
  constructor(private activatedRouter: ActivatedRoute,
              private router: Router,
              private alertService: AlertService,
    private resetService : ResetPasswordService) { }

  ngOnInit() {
    this.getParams();
    this.resetService.checkToken(this.token)
      .subscribe(() =>{
        this.isTokenValid = true;
      })
  }

  resetPassword() {
    this.resetService.reset(this.newPassword, this.token)
      .subscribe(() => {
        this.alertService.success("Your password successfully changed!");
      },
        (error) => {
        this.alertService.error('Your token is invalid');
        })
  }

  getParams() {
    this.activatedRouter.queryParams.subscribe(params => {
      this.token = params['token'];
    });
  }
}
