import {throwError} from "rxjs/index";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {BehaviorSubject} from "rxjs/Rx";
import {Injectable} from "@angular/core";
import {UserService} from "./user.service";
import {AuthenticationService} from "./authentication.service";
import {Location} from "../_models/location";

@Injectable()
export class LocationService {

  private locationUrl = '/api/locations';

  constructor(private http: HttpClient) {
  }


  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.');
  };


  createLocation(location : Location ) {
    const url = `${this.locationUrl}/create`;
    console.log('In LocalService');
    console.log(JSON.stringify(location));
    return this.http.post<Location>(url, location, {headers: AuthenticationService.getAuthHeader()});

  }

  updatelocation(location : Location) {
    const url = `${this.locationUrl}/update`;

    return this.http.put<Location>(url, location, {headers: AuthenticationService.getAuthHeader()})
  }

  deleteLocation(locationId : String) {
    const url = `${this.locationUrl}/delete`;
    return this.http.post<String>(url, locationId, {headers: AuthenticationService.getAuthHeader()})
  }

  getLocationByEventId(event_Id : String) {
    const url = `${this.locationUrl}/getByEventId${event_Id}`;
    return this.http.get<Location>(url, {headers: AuthenticationService.getAuthHeader()})
  }


}
