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

  //
  // constructor(private profileService: ProfileService,
  //             private userService: UserService,
  //             private alertService: AlertService) {
  //   let login = this.userService.getCurrentLogin();
  //   this.profileService.getCustomer(login)
  //     .subscribe(
  //       currentUser => {
  //         this.currentUser = currentUser;
  //       }
  //     )
  // }

  constructor(private profileService: ProfileService,
              private userService: UserService,
              private alertService: AlertService) {
    // let login = JSON.parse(localStorage.getItem('currentUser')).login;
    //    this.userService.getByLogin(login).subscribe(
    //         user => {
    //          this.currentUser = user;
    //           localStorage.setItem('currentUserObject', JSON.stringify(this.currentUser));
    // }
    // );
    // if(sessionStorage.getItem('currentUser')==null) {
    //   let login = JSON.parse(sessionStorage.getItem('currentToken')).login;
    //   this.userService.getByLogin(login).subscribe(
    //     user => {
    //       console.log(user.name);
    //       this.currentUser = user;
    //       sessionStorage.setItem('currentUser', JSON.stringify(this.currentUser));
    //       console.log(this.currentUser.name);
    //     }
    //   );
    // } else {
      this.currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
  }

  ngOnInit() {}

  closeUsers() {
    this.isSearchUser = false;
    this.users = null;
  }

  // searchUser(request) {
  //   // if(this.request !== '') {
  //   //   this.isSearchUser = true
  //   // }
  //   console.log(this.request)
  //   this.profileService.search(request)
  //     .subscribe(
  //       users => {
  //         this.users = users;
  //         if (users.toString() == '') {
  //           this.alertService.info('This user does not exist', true);
  //         }
  //       }
  //     )
  // }

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
