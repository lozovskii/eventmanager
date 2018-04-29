import { Component, OnInit } from '@angular/core';
import {User} from "../_models/user";
import {UserService} from "../_services/user.service";
import {Router} from "@angular/router";
import {AuthenticationService} from "../_services/authentication.service";
import {NavbarService} from "../_services/navbar.service";

@Component({
  moduleId: module.id.toString(),
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  navbar = false;
  currentUser: User;

  //
  // defaultLinks = [
  //   {name: 'Log In', ref:'login', title: 'Log In'},
  //   {name: 'Sign Up', ref: 'register', title: 'Sign Up'},
  // ];
  //
  // loggedInLinks = [
  //   {name: 'Home', ref:'home', title: 'Home page'},
  //   {name: 'Log out', ref: 'logout', title: 'Logout'},
  //   ];

  constructor(private authenticationService : AuthenticationService,
              private navService : NavbarService,
              private router: Router) {
    this.navService.navState$.subscribe( (state)=> this.navbar = state );

  }


  ngOnInit() {
    this.navbar = this.authenticationService.checkingLog();
    console.log('navbar = ' + this.navbar);
  }

  logout() {
    this.authenticationService.logout();
    this.navService.setNavBarState( false );
    return this.router.navigate(['login']);
  }

}
