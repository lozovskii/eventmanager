import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AlertService, AuthenticationService} from "../_services";
import {NavbarService} from "../_services/navbar.service";


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
              private alertService: AlertService,
              private navbarService: NavbarService) {
  }

  ngOnInit() {
    this.authenticationService.logout();

    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home';
    console.log(this.returnUrl);
  }

  login() {
    this.loading = true;
    let userAuthParam = {
      login: this.model.username,
      password: this.model.password
    };
    this.authenticationService.login(userAuthParam)
      .subscribe(() => {
          console.log('logged in with token ' + sessionStorage.getItem('currentToken'));
          this.loading = false;
          this.navbarService.setNavBarState(true);
          return this.router.navigate([this.returnUrl]);
        }

        , () => {
          this.alertService.error('Invalid credentials');
          this.loading = false;
          return this.router.navigate(['/login']);
        });
  }

}
