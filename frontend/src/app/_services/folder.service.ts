import {Injectable} from "@angular/core";
import {UserService} from "./user.service";
import {HttpClient} from "@angular/common/http";
import {AuthenticationService} from "./authentication.service";
import {Folder} from "../_models/folder";
import {Observable} from "rxjs/index";
import {Event} from "../_models/event";

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

  getNotesByCustIdFolderId(custId: string, folderId: string):Observable<Event[]> {
    const url = `${this.foldersUrl}/notes?custId=${custId}&folderId=${folderId}`;
    return this.http.get<Event[]>(url, {headers: AuthenticationService.getAuthHeader()})
  }

  moveNote(noteId:string, folderName:string):Observable<Event>{
    const url = `${this.foldersUrl}/move/${noteId}/${folderName}`;
    return this.http.put<Event>(url, Event, {headers: AuthenticationService.getAuthHeader()});
  }

  deleteFolder(folderId:string){
    console.log('here');
    const url = `${this.foldersUrl}/delete/${folderId}`;
    return this.http.post<string>(url, folderId, {headers: AuthenticationService.getAuthHeader()});
  }


}
