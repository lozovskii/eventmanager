import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {User} from "../_models";
import {Observable} from "rxjs/Observable";


const url = '/api/profile';

@Injectable()
export class ProfileService {

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

  // update(customer: User): Observable<User> {
  //   return this.http.put('/profile/edit', customer);
  // }

  update(customer: User) {
    return this.http.put(`${url}/${customer.id}`, customer);
  }

}

