import {Component, Input, OnInit} from '@angular/core';
import {AlertService} from "../../_services/alert.service";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/item";
import {Location} from '@angular/common';
import {WishList} from "../../_models/wishlist";
import {ItemDto} from "../../_models/dto/itemDto";

@Component({
  selector: 'app-createditems',
  templateUrl: './created-items.component.html',
  styleUrls: ['../wishlist/wishlist.component.css']
})
export class CreatedItemsComponent implements OnInit {
  @Input('included') isIncluded: boolean;

  wishList: WishList;
  items: Item[];
  path: string[] = ['name'];
  order: number = 1; // 1 asc, -1 desc;

  constructor(private wishListService: WishListService,
              private alertService: AlertService,
              private location: Location) {
  }

  ngOnInit() {
    this.wishListService.currentWishList.subscribe((wishList) => this.wishList = wishList);
    this.getCreatedItems();
  }

  addItem(item: Item): void {
    let itemDto: ItemDto = new ItemDto();
    itemDto.item = item;
    this.wishList.items.push(itemDto);
  }

  getCreatedItems(): void {
    this.wishListService.getCreatedItems()
      .subscribe((items) => {
        this.items = items;
      }, (error) => {

        this.location.back();

        this.alertService.info('Items not found', true);
      });
  }

  sortItems(prop: string) {
    this.path = prop.split('.');
    this.order = this.order * (-1); // change order
    return false; // do not reload
  }
}
