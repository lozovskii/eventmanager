import {Injectable} from "@angular/core";
import {User} from "../_models";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {AuthenticationService} from "./authentication.service";

@Injectable()
export class ProfileService {

  private headers = new Headers({ 'Content-Type': 'application/json' });
  private url = '/api/profile';

  constructor(private http: HttpClient) {
  }

  update(customer: User) {
    console.log('Service update' + JSON.stringify(customer))
    // work with Post
    // return this.http.post(`${this.url}/update`, customer,{headers: AuthenticationService.getAuthHeader()});

    return this.http.put(`${this.url}/update`, customer,{headers: AuthenticationService.getAuthHeader()});
  }

}

