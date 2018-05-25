import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import 'rxjs/add/operator/map';
import {UserAuthParam} from "../_models/userAuthParam.model";
import {JwtHelper} from "angular2-jwt";
import {User} from "../_models";
import GoogleUser = gapi.auth2.GoogleUser;

declare const gapi: any;
@Injectable()
export class AuthenticationService {
  url = '/api/auth';

  constructor(private http: HttpClient) {
  }

  login(userAuthParam: UserAuthParam) {
    return this.http.post<any>(this.url, userAuthParam)
      .map(userParam => {
        if (userParam && userParam.token) {
          sessionStorage.setItem('currentToken', JSON.stringify({login: userAuthParam.login, token: userParam.token}));
        }
        return userParam;
      });
  }

  signInGoogle(googleUser: GoogleUser) {
    let profile = googleUser.getBasicProfile();
    console.log('Profile ' + profile);
    let user = new User();
    user.email = profile.getEmail();
    user.login = profile.getEmail().substring(0, profile.getEmail().indexOf('@', 0));
    user.name = profile.getGivenName();
    user.secondName = profile.getFamilyName();
    user.avatar = profile.getImageUrl();
    user.token = googleUser.getAuthResponse().id_token;
    return this.http.post<any>(`${this.url}/google`, user)
      .map(userParam => {
      if (userParam && userParam.token) {
        let h = new JwtHelper();
        let login = h.decodeToken(userParam.token).login;
        sessionStorage.setItem('currentToken', JSON.stringify({login: login, token: userParam.token}));
      }
      console.log(sessionStorage.getItem('currentToken'));
      return userParam;
    });
  }

  logout() {
    localStorage.setItem('clearSessionStorage', '');
    console.log('In Logout');
    sessionStorage.clear();

    if(gapi.auth2 != undefined) {
      gapi.auth2.getAuthInstance().disconnect();
    }

    localStorage.removeItem('clearSessionStorage');
  }

  checkingLog() {
    let token = JSON.parse(sessionStorage.getItem('currentToken'));
    return token != null;
  }

  static getAuthHeader() {
    return new HttpHeaders({'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem('currentToken')).token});
  }

}
