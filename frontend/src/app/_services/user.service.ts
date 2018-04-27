import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {User} from '../_models/user';
import {Observable} from "rxjs/Observable";
import {JwtHelper} from "angular2-jwt";

@Injectable()
export class UserService {
  private JwtHelper: JwtHelper = new JwtHelper();

  constructor(private http: HttpClient) {
  }

  getAll() {
    return this.http.get<User[]>('/api/users');
  }

  getByLogin(login: string): Observable<any> {
    let headers = new HttpHeaders({'Authorization': 'Bearer ' + JSON.parse(localStorage.getItem('currentUser')).token});
    let params = new HttpParams();
    params.set('login', JSON.parse(localStorage.getItem('currentUser')).login);

    console.log('login parameter ' + params.get('login'));
    return this.http.get('/api/users/?login=' + JSON.parse(localStorage.getItem('currentUser')).login, {
      headers: headers,
      params: params
    })

    // .map((response: Response) => response.json());
  }

  create(user: User) {
    console.log('here: ' + JSON.stringify(user));
    return this.http.post<User>('/api/register', user);
  }

  update(user: User) {
    return this.http.put('/api/users/' + user.id, user);
  }

  delete(id: number) {
    return this.http.delete('/api/users/' + id);
  }

  getCurrentLogin() {
    let login = this.JwtHelper.decodeToken(localStorage.getItem('currentUser')).login;
    return login;
  }

  getCurrentId() {
    let id = this.JwtHelper.decodeToken(localStorage.getItem('currentUser')).id;
    return id;
  }

}

