import {Component, OnInit} from '@angular/core';
import {AlertService} from "../../_services/alert.service";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/item";
import { Location } from '@angular/common';

@Component({
  selector: 'app-createditems',
  templateUrl: './createditems.component.html',
  styleUrls: ['../wishlist.component.css']
})
export class CreatedItemsComponent implements OnInit {

  items : Item[];
  path: string[] = ['name'];
  order: number = 1; // 1 asc, -1 desc;

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

  sortItems(prop: string) {
    this.path = prop.split('.');
    this.order = this.order * (-1); // change order
    return false; // do not reload
  }
}
