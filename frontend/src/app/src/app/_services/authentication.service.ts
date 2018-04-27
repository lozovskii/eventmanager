import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import 'rxjs/add/operator/map'
import {UserAuthParam} from "../_models/userAuthParam.model";
import {UserService} from "./user.service";

@Injectable()
export class AuthenticationService {
  navbar = false;
  variable = null;
  url = '/api/auth';

  constructor(private http: HttpClient,
              private userService: UserService) { }

  login(userAuthParam : UserAuthParam) {
    return this.http.post<any>(this.url, userAuthParam)
      .map(userParam => {
        if(userParam && userParam.token) {
          localStorage.setItem('currentUser', JSON.stringify( {login :userAuthParam.login, token: userParam.token}));
        }
        return userParam;
      });
  }

  logout() {
    localStorage.removeItem('currentUser');
  }

  // currentUser(): Observable<User> {
  //   let token = localStorage.getItem("currentUser");
  //   let userLogin = +this.JwtHelper.decodeToken(token).login;
  //   console.log('login: ' + userLogin);
  //
  //   return this.userService.getUser(userLogin).map((user: User) => {//map
  //     console.log('user1: ' + JSON.stringify(user));
  //     return user;
  //   })
  // }

  checkingLog(){
    this.variable = JSON.parse(localStorage.getItem('currentUser'));
    console.log('variable = ' + this.variable);
    if(this.variable != null) {
          this.navbar = true;
    }
    return this.navbar;
  }

}
