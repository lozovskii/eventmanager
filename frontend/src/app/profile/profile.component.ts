import {Component, OnInit} from '@angular/core';
import {UserService} from "../_services";
import {User} from "../_models";
import {ProfileService} from "../_services/profile.service";
import {AlertService} from "../_services/alert.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  currentUser: User;

  constructor(private profileService: ProfileService,
              private userService: UserService) {
    let login = JSON.parse(localStorage.getItem('currentUser')).login;
    this.userService.getByLogin(login).subscribe(
      user => {
        console.log(user.name);
        this.currentUser = user;
        localStorage.setItem('currentUserObject', JSON.stringify(this.currentUser));
        console.log(this.currentUser.name);
      }
    );
  }

  ngOnInit() {
  }
}
