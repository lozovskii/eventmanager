import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {User} from "../../_models";
import {ProfileService} from "../../_services/profile.service";

@Component({
  selector: 'customer-profile',
  templateUrl: './customer-profile.component.html',
  styleUrls: ['./customer-profile.component.css']
})

export class CustomerProfileComponent implements OnInit {

  customer: User;
  isFriend: boolean = false;

  constructor(private profileService: ProfileService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.getCustomerDetails();
  }

  getCustomerDetails(): void {
    const login = this.route.snapshot.paramMap.get("login");
    this.profileService.getCustomer(login).subscribe(data => {
      this.customer = data;
      this.profileService.isFriend(this.customer.id).subscribe(
        () => this.isFriend = true,
        () => this.isFriend = false)
    });
  }
}
