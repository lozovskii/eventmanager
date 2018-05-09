import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from "../_services/authentication.service";
import {AlertService} from "../_services/alert.service";
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
        console.log('logged in with token ' + sessionStorage.getItem('currentToken'));
        this.loading=false;
          this.navbarService.setNavBarState( true );
          return this.router.navigate(['/home'])
        }

      , () => {
        alert('Invalid credentials');
          this.loading=false;
          return this.router.navigate(['/login']);
        });
  }

}
