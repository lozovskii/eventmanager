import {Component, OnInit} from '@angular/core';
import {AlertService} from "../../_services/alert.service";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/item";
import { Location } from '@angular/common';

@Component({
  selector: 'app-bookeditems',
  templateUrl: './createditems.component.html',
  styleUrls: ['../wishlist.component.css']
})
export class CreatedItemsComponent implements OnInit {
  items : Item[];

  constructor(private wishListService: WishListService,
              private alertService : AlertService,
              private location: Location
  ) {
  }

  ngOnInit() {
      this.getCreatedItems();
  }

  getCreatedItems(): void {
    this.wishListService.getCreatedItems()
      .subscribe((items) => {
        this.items = items;
        }, (error) => {

        this.location.back();

        this.alertService.info('Items not found',true);
      });
  }
}
