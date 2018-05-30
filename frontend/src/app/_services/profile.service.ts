import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {User} from "../_models";
import {Observable} from "rxjs";
import {AuthenticationService} from "./authentication.service";
import {UserService} from "./user.service";
import {Friends} from "../_models/friends";

@Injectable()
export class ProfileService {

  private url = '/api/profile';

  constructor(private http: HttpClient,
              private userService: UserService) {
  }

  getCustomer(login: string): Observable<User> {
    return this.http.get<User>(
      `${this.url}/${login}`,
      {headers: AuthenticationService.getAuthHeader()});
  }

  getFriends(login: string): Observable<User[]> {
    return this.http.get<User[]>(
      `${this.url}/friends?login=${login}`,
      {headers: AuthenticationService.getAuthHeader()});
  }

  search(page: number, size: number, search: string) {
    return this.http.get(
      `${this.url}/search?page=${page}&size=${size}&search=${search}`,
      {headers: AuthenticationService.getAuthHeader()});
  }

  update(customer: User) {
    return this.http.put(`${this.url}/update`, customer,{headers: AuthenticationService.getAuthHeader()});
  }

  addFriend(login) {
    return this.http.get(
      `${this.url}/add?login=${login}`,
      {headers: AuthenticationService.getAuthHeader()});
  }

  cancelRequest(login) {
    return this.http.get(
      `${this.url}/cancel?login=${login}`,
      {headers: AuthenticationService.getAuthHeader()});
  }

  acceptFriend(token) {
    return this.http.get(
      `${this.url}/accept?token=${token}`,
      {headers: AuthenticationService.getAuthHeader()});
  }

  rejectFriend(token) {
    return this.http.get(
      `${this.url}/reject?token=${token}`,
      {headers: AuthenticationService.getAuthHeader()});
  }

  deleteFriend(login) {
    return this.http.delete(
      `${this.url}/delete/${login}`,
      {headers: AuthenticationService.getAuthHeader()});
  }

  isFriend(customerId) {
    return this.http.get(
      `${this.url}/isFriends?currentCustomerId=${this.userService.getCurrentId()}&customerId=${customerId}`,
      {headers: AuthenticationService.getAuthHeader()});
  }

  friends(currentUser): Observable<Friends[]> {
    return this.http.get<Friends[]>(
      `${this.url}/friendOrRequest?login=${currentUser}`,
      {headers: AuthenticationService.getAuthHeader()});
  }
}
