import {Component, NgZone, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AlertService, AuthenticationService, UserService} from "../_services";
import {NavbarService} from "../_services/navbar.service";

declare const gapi: any;

@Component({
  moduleId: module.id.toString(),
  templateUrl: 'login.component.html',
  styleUrls: ['./login.component.css']
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

    this.googleInit();
    // gapi.auth2.render('my-signin2', {
    //   'scope': 'profile email',
    //   'width': 240,
    //   'height': 50,
    //   'longtitle': false,
    //   'theme': 'light',
    //   'onsuccess': param => this.onSignIn(param)
    // });
  }

  public auth2: any;
  public googleInit() {
    gapi.load('auth2', () => {
      this.auth2 = gapi.auth2.init({
        client_id: '882385907365-t3b1b4nieo5c2rna6ejf862eadkho2s2.apps.googleusercontent.com',
        cookiepolicy: 'single_host_origin',
        scope: 'profile email'
      });
      this.attachSignin(document.getElementById('googleBtn'));
    });
  }

  public attachSignin(element) {
    this.auth2.attachClickHandler(element, {},
      (googleUser) => {

        let profile = googleUser.getBasicProfile();
        console.log('Token || ' + googleUser.getAuthResponse().id_token);
        console.log('ID: ' + profile.getId());
        console.log('Name: ' + profile.getName());
        console.log('Image URL: ' + profile.getImageUrl());
        console.log('Email: ' + profile.getEmail());
        //YOUR CODE HERE
        this.loginGoogle(googleUser.getAuthResponse().id_token);

      }, (error) => {
        alert(JSON.stringify(error, undefined, 2));
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

  loginGoogle(token) {
    this.authenticationService.googleLogin(token)
      .subscribe(() => {
          this.userService.getByLogin(JSON.parse(sessionStorage.getItem('currentToken')).login).subscribe(
            user => {
              console.log("In Google Login Subscribe");
              console.log(user);
              sessionStorage.setItem('currentUser', JSON.stringify(user));
//              this.loading = false;
              this.navbarService.setNavBarState(true);
              // history.back();
              return this.router.navigate(['/home']);
            });
        }
        , () => {
          this.alertService.error('Invalid credentials');
          this.loading = false;
          return this.router.navigate(['/login']);
        });
  }
}
