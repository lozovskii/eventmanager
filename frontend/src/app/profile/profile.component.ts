import {Component, OnInit} from '@angular/core';
import {User} from "../_models";
import {ProfileService} from "../_services/profile.service";
import {UserService} from "../_services";
import {AlertService} from "../_services";
import {Friends} from "../_models/friends";

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
  friendsArray: Friends[];

  constructor(private profileService: ProfileService,
              private userService: UserService,
              private alertService: AlertService) {
    this.currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
  }

  ngOnInit() {
    this.friends();
  }

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
    location.reload();
    this.searchUser(this.request);
  }

  cancelRequest(login: string) {
    this.profileService.cancelRequest(login).subscribe(() => {
      this.alertService.info(`Friendship request canceled`)
    });
    location.reload();
    this.searchUser(this.request);
  }

  deleteFriend(login: string) {
    this.profileService.deleteFriend(login).subscribe(() => {
      this.alertService.info(`Friend was removed from your friends list`);
    });
    location.reload();
    this.searchUser(this.request);
  }

  friends() {
    this.profileService.friends(this.currentUser.login).subscribe((data) => {
      this.friendsArray = data;
    });
  }

  getIsFriends(login): boolean {
    for (let i of this.friendsArray) {
      if (i.another === login) {
        return i.isfriends;
      }
    }
  }

  getIsRequest(login): boolean {
    for (let i of this.friendsArray) {
      if (i.another === login) {
        return i.isrequest;
      }
    }
  }
}
