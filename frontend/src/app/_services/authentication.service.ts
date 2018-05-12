import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import 'rxjs/add/operator/map';
import {UserAuthParam} from "../_models/userAuthParam.model";
import {Observable} from "rxjs/Rx";

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

  logout() {
    sessionStorage.clear();
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
