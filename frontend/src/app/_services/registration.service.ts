import {Injectable} from '@angular/core';
import {User} from "../_models/user";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class RegistrationService {

  constructor(private http: HttpClient) { }

  create(user: User) {
    console.log('here: ' + JSON.stringify(user));
    return this.http.post<User>('/api/register', user );
  }

  verifyEmail(token)  {
      return this.http.get('/api/registrationConfirm?token='+token);
  }

  // googleRegister(user: User) {
  //   return this.http.post<User>('/api/googleRegister', user )
  //     .map(userParam => {
  //       if (userParam && userParam.token) {
  //         let h = new JwtHelper();
  //         let login = h.decodeToken(userParam.token).login;
  //         sessionStorage.setItem('currentToken', JSON.stringify({login: login, token: userParam.token}));
  //       }
  //       console.log(sessionStorage.getItem('currentToken'));
  //       return userParam;
  //     });
  // }

}
