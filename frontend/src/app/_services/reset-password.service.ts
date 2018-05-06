import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthenticationService} from "./authentication.service";


@Injectable()
export class ResetPasswordService {

  constructor(private http: HttpClient) { }

  sendEmail(email) {
    return this.http.get('/api/reset/sendLink?email='+email, {headers: AuthenticationService.getAuthHeader()});
  }

  checkToken(token) {
    return this.http.get('/api/reset/resetPassword?token='+token, {headers: AuthenticationService.getAuthHeader()});
  }

  reset(newPassword, token) {
    return this.http.post('/api/reset/setNewPassword',{"password": newPassword, "token": token}, {headers: AuthenticationService.getAuthHeader()});
  }

}
