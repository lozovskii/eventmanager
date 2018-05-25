import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AlertService, AuthenticationService, RegistrationService, UserService} from "../_services";
import {FormBuilder, Validators} from "@angular/forms";
import {User} from "../_models";
import {NavbarService} from "../_services/navbar.service";
import GoogleUser = gapi.auth2.GoogleUser;

declare const gapi: any;

@Component({
  selector: 'registration',
  templateUrl: 'register.component.html',
  styleUrls: ['../login/login.component.css']
})

export class RegisterComponent implements OnInit, OnChanges {
  @Input('modal') inModal : boolean = false;

  user: User;
  // loading = false;
  registerForm = this.formBuilder.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
    secondName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
    login: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
    email: ['', [Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(20)]]
  });

  isValidFormSubmitted = null;
  loading = false;
  isModal: boolean = false;
  public auth2: any;

  constructor(private router: Router,
              private registrationService: RegistrationService,
              private alertService: AlertService,
              private formBuilder: FormBuilder,
              private userService: UserService,
              private navbarService: NavbarService,
              private activatedRoute : ActivatedRoute,
              private authService: AuthenticationService) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.isModal = changes['inModal'].currentValue;
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

  ngOnInit(): void {
  }

  ngAfterViewInit() {

    this.googleInit();
  }

  public googleInit() {
    console.log('Google Inin gapi ' + gapi);
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
        console.log('Google user ' + googleUser);
        this.signInGoogle(googleUser);

      }, (error) => {
        alert(JSON.stringify(error, undefined, 2));
      });
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

  public signInGoogle(googleUser: GoogleUser) {
    this.authService.signInGoogle(googleUser)
      .subscribe(() => {
          this.userService.getByLogin(JSON.parse(sessionStorage.getItem('currentToken')).login).subscribe(
            user => {
              this.alertService.success('Registration successful!', true);
              console.log(user);
              this.navbarService.setNavBarState(true);
              sessionStorage.setItem('currentUser', JSON.stringify(user));
              this.loading = false;
              return this.router.navigate(['/home']);
            });
        }
        , () => {
          this.alertService.error('Something wrong during signing in with your Google account');
          this.loading = false;
          return this.router.navigate(['/register']);
        });
  }
}
