import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {User} from "../_models";
import {Observable} from "rxjs";
import {AuthenticationService} from "./authentication.service";

@Injectable()
export class ProfileService {

  private url = '/api/profile';

  constructor(private http: HttpClient) {
  }

  getByLogin(login: string): Observable<any> {
    // let headers = new HttpHeaders({'Authorization': 'Bearer ' + JSON.parse(localStorage.getItem('currentUser')).token});
    // let params = new HttpParams();
    // params.set('login', JSON.parse(localStorage.getItem('currentUser')).login);
    //
    // console.log('login parameter ' + params.get('login'));
    return this.http.get('/profile/edit/?login=' + JSON.parse(localStorage.getItem('currentUser')).login, {
      // headers: headers,
      // params: params
    })
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


  search(request): Observable<User[]> {
    return this.http.get<User[]>(
      `${this.url}/search?request=${request}`,
      {headers: AuthenticationService.getAuthHeader()});
  }

  update(customer: User) {
    // work with Post
    // return this.http.post(`${this.url}/update`, customer,{headers: AuthenticationService.getAuthHeader()});

    return this.http.put(`${this.url}/update`, customer,{headers: AuthenticationService.getAuthHeader()});
  }

  addFriend(login) {
    return this.http.get(
      `${this.url}/add?login=${login}`,
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
}
