import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AlertService, RegistrationService} from "../_services/index";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {User} from "../_models/user";


@Component({
  moduleId: module.id.toString(),
  templateUrl: 'register.component.html'
})

export class RegisterComponent implements OnInit {
  user: User;
  // loading = false;
  registerForm: FormGroup;

  constructor(private router: Router,
              private registrationService: RegistrationService,
              private alertService: AlertService,
              private formBuilder: FormBuilder) {
//    this.registerForm = formBuilder.group({
//      title: formBuilder.control('initial value', Validators.required)
//    });
  }



  ngOnInit(): void {
    this.createForm();
  }

  createForm() {
    this.registerForm = new FormGroup({
      name: new FormControl(this.user.name, [Validators.required, Validators.minLength(3), Validators.maxLength(20)]),
      secondName: new FormControl(this.user.secondName, [Validators.required, Validators.minLength(3), Validators.maxLength(20)]),
      login: new FormControl(this.user.login, [Validators.required, Validators.minLength(3), Validators.maxLength(20)]),
      email: new FormControl(this.user.email, [Validators.required, Validators.email]),
      password: new FormControl(this.user.password, [Validators.required, Validators.minLength(4), Validators.maxLength(20)])
    });
  }

  register(userFromForm: User) {
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

  get name() {
    return this.registerForm.get('name');
  }
}
