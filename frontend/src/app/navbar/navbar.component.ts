import { Component, OnInit } from '@angular/core';
import {User} from "../_models/user";
import {UserService} from "../_services/user.service";
import {Router} from "@angular/router";
import {AuthenticationService} from "../_services/authentication.service";

@Component({
  moduleId: module.id.toString(),
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  navbar = false;
  currentUser: User;

  constructor(authenticationService : AuthenticationService) {
    this.navbar = authenticationService.checkingLog();
    console.log('navbar = ' + this.navbar);
  }


  ngOnInit() {

  }

}
