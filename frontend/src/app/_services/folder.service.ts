import {Injectable} from "@angular/core";
import {UserService} from "./user.service";
import {HttpClient} from "@angular/common/http";
import {AuthenticationService} from "./authentication.service";
import {Folder} from "../_models/folder";

@Injectable()
export class FolderService {
  private foldersUrl = 'api/folders';

  constructor(private http: HttpClient,
              private userService: UserService) {
  }

  create(folder: Folder) {
    console.log('here: ' + JSON.stringify(folder));
    return this.http.post<Folder>(this.foldersUrl, folder, {headers: AuthenticationService.getAuthHeader()});
  }
}
