import {Component, OnInit} from '@angular/core';
import {AlertService} from "../../_services/alert.service";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/item";
import { Location } from '@angular/common';
import {ActivatedRoute} from "@angular/router";
import {WishList} from "../../_models/wishlist";
import {ItemDto} from "../../_models/dto/itemDto";

@Component({
  selector: 'app-additems',
  templateUrl: './add-items.component.html',
  styleUrls: ['../wishlist.component.css']
})
export class AddItemsComponent implements OnInit {

  hasChanges: boolean = false;
  wishList : WishList;
  items : Item[];
  path: string[] = ['name'];
  order: number = 1; // 1 asc, -1 desc;

  constructor(private wishListService: WishListService,
              private alertService : AlertService,
              private activatedRoute: ActivatedRoute,
              private location: Location
  ) {
  }

  ngOnInit() {
    this.wishList = new WishList();

    this.activatedRoute.params.subscribe(params => {
      let wishListId = params['id'];
      this.wishList.id = wishListId;
    });

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

  addItem(item : ItemDto) : void{
    this.hasChanges = true;
    this.wishList.items.push(item);
  }

  addItems() : void {
    this.wishListService.addItems(this.wishList).subscribe(data => {

      this.alertService.success('Wish list successfully updated!');
    }, error2 => {
      this.alertService.error('Something wrong');
    });
  }

  sortItems(prop: string) {
    this.path = prop.split('.');
    this.order = this.order * (-1); // change order
    return false; // do not reload
  }
}
