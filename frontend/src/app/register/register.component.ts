import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {RegistrationService, AlertService} from "../_services/index";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {User} from "../_models/user";


@Component({
  moduleId: module.id.toString(),
  templateUrl: 'register.component.html'
})

export class RegisterComponent implements OnInit{
  user: User;
  // loading = false;
  registerForm: FormGroup;

  constructor(private router: Router,
              private registrationService: RegistrationService,
              private alertService: AlertService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      name: [''],
      secondName: [''],
      login: [''],
      email: [''],
      password: ['']
    });
  }

  register(userFromForm: User){
    console.log('user: ' + JSON.stringify(userFromForm));
    // this.loading = true;
    this.registrationService.create(userFromForm)
      .subscribe((data) => {
         this.alertService.success('Registration successful! Please, check your email for confirmation link.', true);
         setTimeout(() => this.router.navigate(["/"]), 5000)
        },
        (error) => {
          this.alertService.error(error.error);
        });
  }
}
