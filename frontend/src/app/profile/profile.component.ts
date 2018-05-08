import {Component, OnInit} from '@angular/core';
import {UserService} from "../_services";
import {User} from "../_models";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  currentUser: User;

  constructor(private userService: UserService
             ) {
    let login = JSON.parse(localStorage.getItem('currentUser')).login;
    this.userService.getByLogin(login).subscribe(
      user => {
        this.currentUser = user;
        localStorage.setItem('currentUserObject', JSON.stringify(this.currentUser));

      }
    );
  }

  ngOnInit() {
  }

}
