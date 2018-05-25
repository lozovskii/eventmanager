import {AfterViewInit, Component, Input, NgZone, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AlertService, AuthenticationService, UserService} from "../_services";
import {NavbarService} from "../_services/navbar.service";

declare const gapi: any;

@Component({
  selector: 'login',
  templateUrl: 'login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit, OnChanges {

  @Input('modal') inModal;
  model: any = {};
  loading = false;
  returnUrl: string;
  isModal: boolean = false;

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

    // this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home';
    this.returnUrl = '/home';
    console.log(this.returnUrl);
  }

  ngOnChanges(changes: SimpleChanges): void {
      this.isModal = changes['inModal'].currentValue;
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
        this.signInGoogle(googleUser);
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
              document.getElementById('loginCloseBtn').click();
              return this.router.navigate(['/home']);
            });
        }
        , () => {
          this.alertService.error('Invalid credentials');
          this.loading = false;
          return this.router.navigate(['/login']);
        });
  }

  signInGoogle(googleUser) {
    this.authenticationService.signInGoogle(googleUser)
      .subscribe(() => {
          this.userService.getByLogin(JSON.parse(sessionStorage.getItem('currentToken')).login).subscribe(
            user => {
              console.log("In Google Login Subscribe");
              console.log(user);
              sessionStorage.setItem('currentUser', JSON.stringify(user));
              this.loading = false;
              this.navbarService.setNavBarState(true);
              // history.back();
              return this.router.navigate(['/home']);
            });
        }
        , () => {
          this.alertService.error('Something wrong during signing in with your Google account');
          this.loading = false;
          return this.router.navigate(['/login']);
        });
  }
}
