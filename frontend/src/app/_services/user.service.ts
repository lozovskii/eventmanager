import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../_models/user';
import {Observable} from "rxjs";
import {AuthenticationService} from "./authentication.service";

@Injectable()
export class UserService {

  constructor(private http: HttpClient) {
  }

  getAll() {
    return this.http.get<User[]>('/api/users');
  }

  getByLogin(login: string): Observable<any> {
    return this.http.get('/api/users/?login='+login, {headers: AuthenticationService.getAuthHeader()})
  }

  update(user: User) {
    return this.http.put('/api/users/' + user.id, {headers: AuthenticationService.getAuthHeader()});
  }

  delete(id: number) {
    return this.http.delete('/api/users/' + id, {headers: AuthenticationService.getAuthHeader()});
  }

  getCurrentLogin() {
    return JSON.parse(sessionStorage.getItem('currentUser')).login;
  }

  getCurrentId() {
    return JSON.parse(sessionStorage.getItem('currentUser')).id;
  }



}

