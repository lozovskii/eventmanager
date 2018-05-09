import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AlertService, RegistrationService} from "../_services";
import {FormBuilder, Validators} from "@angular/forms";
import {User} from "../_models";


@Component({
  moduleId: module.id.toString(),
  templateUrl: 'register.component.html'
})

export class RegisterComponent implements OnInit {
  user: User;
  // loading = false;
  registerForm = this.formBuilder.group({
    name : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
    secondName : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
    login : ['', [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
    email : ['', [Validators.email]],
    password : ['', [Validators.required, Validators.minLength(4), Validators.maxLength(20)]]
  });

  isValidFormSubmitted = null;
  loading = false;

  constructor(private router: Router,
              private registrationService: RegistrationService,
              private alertService: AlertService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
  }

  register(userFromForm: User) {

    this.isValidFormSubmitted = false;

    if (this.registerForm.invalid) {
      return;
    }
    this.isValidFormSubmitted = true;
    this.loading = true;
    console.log('user: ' + JSON.stringify(userFromForm));
    // this.loading = true;
    this.registrationService.create(userFromForm)
      .subscribe(() => {
          this.alertService.success('Registration successful! Please, check your email for confirmation link.', true);
          setTimeout(() => this.router.navigate(["/"]), 5000);
          this.loading = false;
        },
        (error) => {
          this.alertService.error(error.error);
          this.loading = false;
        });
  }

  get name() {
    return this.registerForm.get('name');
  }

  get secondName() {
    return this.registerForm.get('secondName');
  }

  get login() {
    return this.registerForm.get('login');
  }

  get email() {
    return this.registerForm.get('email');
  }

  get password() {
    return this.registerForm.get('password');
  }
}
