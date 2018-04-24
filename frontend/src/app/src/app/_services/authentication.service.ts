import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import 'rxjs/add/operator/map'
import {UserAuthParam} from "../_models/userAuthParam.model";



@Injectable()
export class AuthenticationService {

  authenticated = false;
  url = '/api/auth';
  constructor(private http: HttpClient) { }

  // login(username: string, password: string) {
  //   return this.http.post<any>('/api/authenticate', { username: username, password: password })
  //     .map(user => {
  //       // login successful if there's a jwt token in the response
  //       if (user && user.token) {
  //         // store user details and jwt token in local storage to keep user logged in between page refreshes
  //         localStorage.setItem('currentUser', JSON.stringify(user));
  //       }
  //
  //       return user;
  //     });
  // }

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
    // remove user from local storage to log user out
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
}
