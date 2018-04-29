import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";


@Injectable()
export class ResetPasswordService {

  constructor(private http: HttpClient) { }

  sendEmail(email) {
    return this.http.get('/api/reset/sendLink?email='+email);
  }

  checkToken(token) {
    return this.http.get('/api/reset/resetPassword?token='+token);
  }

  reset(newPassword, token) {
    return this.http.post('/api/reset/setNewPassword',{"password": newPassword, "token": token});
  }

}
