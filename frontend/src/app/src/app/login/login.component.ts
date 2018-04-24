import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {AuthenticationService} from "../_services/authentication.service";
import {AlertService} from "../_services/alert.service";


@Component({
  moduleId: module.id.toString(),
  templateUrl: 'login.component.html'
})

export class LoginComponent implements OnInit {
  model: any = {};
  loading = false;
  returnUrl: string;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private authenticationService: AuthenticationService,
              private alertService: AlertService) {
  }

  ngOnInit() {
    // reset login status
    this.authenticationService.logout();

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  login() {
    this.loading = true;
    let userAuthParam = {
      login: this.model.username,
      password: this.model.password
    };
    this.authenticationService.login(userAuthParam)
      .subscribe(data => {
        console.log('logged in with token ' + localStorage.getItem('currentUser'));
        this.loading=false;
          return this.router.navigate(['/home'])
        }
//      this.customToastService.setMessage('Welcome on your home page!');

      , () => {
        alert('Invalid credentials');
          this.loading=false;
          return this.router.navigate(['/login']);
        });
  }

  // .subscribe(
  //   data => {
  //     this.router.navigate([this.returnUrl]);
  //   },
  //   error => {
  //     this.alertService.error(error);
  //     this.loading = false;
  //   });

}
