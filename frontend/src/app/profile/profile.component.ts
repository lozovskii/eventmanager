import {Component, OnInit} from '@angular/core';
import {User} from "../_models";
import {ProfileService} from "../_services/profile.service";
import {UserService} from "../_services";
import {AlertService} from "../_services/alert.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})

export class ProfileComponent implements OnInit {

  users: User[];
  request = '';
  currentUser: User;
  isSearchUser = false;
  page: number = 1;
  pages: Number[];
  isAvatar: boolean;

  constructor(private profileService: ProfileService,
              private userService: UserService,
              private alertService: AlertService) {
    this.currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    if (this.currentUser.avatar !== '') {
      this.isAvatar = true;
    }
  }

  ngOnInit() {}

  closeUsers() {
    this.isSearchUser = false;
    this.users = null;
  }

  searchUser(request) {
    if (this.request !== '') {
      this.isSearchUser = true;
    }

    this.profileService.search(this.page, 6, request)
      .subscribe((data) => {
        this.users = data['pageItems'];
        this.pages = new Array(data['pagesAvailable']);
    });
  }

  setPage(i,event:any) {
    event.preventDefault();
    this.page=i;
    this.searchUser(this.request);
  }

  onKeyUp(event){
    this.isSearchUser = true;
    this.request = event.target.value;
    this.searchUser(this.request);

  }

  addFriend(login: string) {
    this.profileService.addFriend(login).subscribe(() => {
      this.alertService.info(`Friendship request sent`);
    });
  }
}
