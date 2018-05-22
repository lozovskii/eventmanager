import {Component, NgZone, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AlertService, AuthenticationService, UserService} from "../_services";
import {NavbarService} from "../_services/navbar.service";
// import { AuthService } from "angularx-social-login";
// import { FacebookLoginProvider, GoogleLoginProvider, LinkedInLoginProvider } from "angularx-social-login";

declare const gapi: any;

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
              private navbarService: NavbarService,
              private userService: UserService,
              private ngZone: NgZone,
              /*private authService: AuthService*/) {
  }

  ngOnInit() {

    this.authenticationService.logout();

    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home';
    console.log(this.returnUrl);
  }

  ngAfterViewInit() {
    gapi.signin2.render('my-signin2', {
      'scope': 'profile email',
      'width': 240,
      'height': 50,
      'longtitle': false,
      'theme': 'light',
      'onsuccess': param => this.onSignIn(param)
    });
  }

  login() {
    this.loading = true;
    let userAuthParam = {
      login: this.model.username,
      password: this.model.password
    };
    this.authenticationService.login(userAuthParam)
      .subscribe(() => {
          this.userService.getByLogin(JSON.parse(sessionStorage.getItem('currentToken')).login).subscribe(
            user => {
              sessionStorage.setItem('currentUser', JSON.stringify(user));
              this.loading = false;
              this.navbarService.setNavBarState(true);
              return this.router.navigate([this.returnUrl]);
            });
        }
        , () => {
          this.alertService.error('Invalid credentials');
          this.loading = false;
          return this.router.navigate(['/login']);
        });
  }

  public onSignIn(googleUser) {
    console.log("fucking there");
    let token = googleUser.getAuthResponse().id_token;
    this.authenticationService.googleLogin(token)
      .subscribe(() => {
          this.userService.getByLogin(JSON.parse(sessionStorage.getItem('currentToken')).login).subscribe(
            user => {
              console.log("In Subscribe");
              sessionStorage.setItem('currentUser', JSON.stringify(user));
              this.loading = false;
              this.navbarService.setNavBarState(true);
              return this.router.navigate([this.returnUrl]);
            });
        }
        , () => {
          this.alertService.error('Invalid credentials');
          this.loading = false;
          return this.router.navigate(['/login']);
        });
  }
}
