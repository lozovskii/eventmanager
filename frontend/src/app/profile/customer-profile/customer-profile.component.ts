import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {User} from "../../_models";
import {ProfileService} from "../../_services/profile.service";
import {Folder} from "../../_models/folder";
import {FolderService} from "../../_services/folder.service";

@Component({
  selector: 'customer-profile',
  templateUrl: './customer-profile.component.html',
  styleUrls: ['./customer-profile.component.css']
})

export class CustomerProfileComponent implements OnInit {
  folders: Folder[];
  customer: User;
  isFriend: boolean = false;
  login: string;

  constructor(private profileService: ProfileService,
              private route: ActivatedRoute,
              private folderService: FolderService) {
  }

  ngOnInit() {
    this.getCustomerDetails();
  }

  getSharedFolders(): void {
    this.folderService.getSharedFolders(this.login)
      .subscribe( (folders) => {
        this.folders = folders;
      })
  }

  getCustomerDetails(): void {
    this.login = this.route.snapshot.paramMap.get("login");
    this.profileService.getCustomer(this.login).subscribe(data => {
      this.customer = data;
      this.profileService.isFriend(this.customer.id).subscribe(
        () => {
          this.isFriend = true;
          this.getSharedFolders();
          },
        () => this.isFriend = false)
    });
  }
}
