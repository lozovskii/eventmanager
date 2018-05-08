import {Injectable} from "@angular/core";
import {UserService} from "./user.service";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {AuthenticationService} from "./authentication.service";
import {User} from "../_models";

@Injectable()
export class FriendsService {

  constructor(private http: HttpClient,
              private userService: UserService) {
  }

  getFriends(login: string): Observable<User[]> {
    const url = '/api/profile/friends?login='+login;
    return this.http.get<User[]>(url, {headers: AuthenticationService.getAuthHeader()});
  }
}
