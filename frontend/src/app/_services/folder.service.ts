import {Injectable} from "@angular/core";
import {UserService} from "./user.service";
import {HttpClient} from "@angular/common/http";
import {AuthenticationService} from "./authentication.service";
import {Folder} from "../_models/folder";
import {Observable} from "rxjs/index";

@Injectable()
export class FolderService {
  private foldersUrl = '/api/folders';

  constructor(private http: HttpClient,
              private userService: UserService) {
  }

  create(folder: Folder) {
    const url = `${this.foldersUrl}`;
    return this.http.post<Folder>(url, folder, {headers: AuthenticationService.getAuthHeader()});
  }

  getAllFolders(): Observable<Folder[]> {
    let customerId = this.userService.getCurrentId();
    const url = `${this.foldersUrl}/all?customerId=${customerId}`;
    return this.http.get<Folder[]>(url, {headers: AuthenticationService.getAuthHeader()});
  }
}
