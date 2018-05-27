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

  constructor(private user: ProfileService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.getCustomerDetails();
  }

  getCustomerDetails(): void {
    const login = this.route.snapshot.paramMap.get("login");
    this.user.getCustomer(login).subscribe(data => {
      this.customer = data;
    });
  }
}
