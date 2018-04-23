import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {UserService, AlertService} from "../_services/index";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {User} from "../_models/user";

@Component({
  moduleId: module.id.toString(),
  templateUrl: 'register.component.html'
})

// @Component({
//   // selector: 'app-regform',
//   templateUrl: 'register.component.html',
// })

export class RegisterComponent implements OnInit{
  user: User;
  // loading = false;
  registerForm: FormGroup;

  constructor(private router: Router,
              private userService: UserService,
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
    console.log('onSubmit');
    console.log('user: ' + JSON.stringify(userFromForm));
    // this.loading = true;
    this.userService.create(userFromForm)
      .subscribe(
        data => {
          // this.alertService.success('Registration successful', true);
          this.router.navigate(['/login']);
        },
        error => {
          this.alertService.error(error);
          // this.loading = false;
        });
  }
}
