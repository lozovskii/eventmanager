import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import 'rxjs/add/operator/map';
import {UserAuthParam} from "../_models/userAuthParam.model";
import {JwtHelper} from "angular2-jwt";

declare const gapi: any;
@Injectable()
export class AuthenticationService {
  navbar = false;
  variable = null;
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

  googleLogin(token: any) {
    return this.http.post<any>(`${this.url}/google`, token)
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
    sessionStorage.clear();
    if(gapi.auth2) {
      console.log("Trying logout");
      gapi.auth2.getAuthInstance().signOut();
    }
    // let cookies = document.cookie.split(";");
    //
    // for (let i = 0; i < cookies.length; i++) {
    //   let cookie = cookies[i];
    //   let eqPos = cookie.indexOf("=");
    //   let name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
    //   document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    // }
  }

  checkingLog() {
    this.variable = JSON.parse(sessionStorage.getItem('currentToken'));
    console.log('variable = ' + this.variable);
    if (this.variable != null) {
      this.navbar = true;
    }
    return this.navbar;
  }

  static getAuthHeader() {
    return new HttpHeaders({'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem('currentToken')).token});
  }

}
