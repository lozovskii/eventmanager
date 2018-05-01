import {Component, OnInit} from '@angular/core';
import {User} from "../_models";
import {Router} from "@angular/router";
import {AuthenticationService} from "../_services";
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

  constructor(private authenticationService: AuthenticationService,
              private navService: NavbarService,
              private router: Router) {
    this.navService.navState$.subscribe((state) => this.navbar = state);

  }

  ngOnInit() {
    this.navbar = this.authenticationService.checkingLog();
    console.log('navbar = ' + this.navbar);
  }

  logout() {
    this.authenticationService.logout();
    this.navService.setNavBarState(false);
    return this.router.navigate(['login']);
  }

}
