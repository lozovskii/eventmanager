import {Injectable} from '@angular/core';
import {User} from "../_models";
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

}
