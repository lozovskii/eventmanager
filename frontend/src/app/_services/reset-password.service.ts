import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";


@Injectable()
export class ResetPasswordService {

  constructor(private http: HttpClient) { }

  sendEmail(email) {
    return this.http.get('/api/reset/sendLink?email='+email);
  }

}
