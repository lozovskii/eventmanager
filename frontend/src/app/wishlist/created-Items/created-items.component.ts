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

  hasChanges: boolean = false;
  editableItem: Item;
  itemView: Item;
  wishList: WishList;
  trash: Item[];
  items: Item[];
  path: string[] = ['name'];
  order: number = 1; // 1 asc, -1 desc;

  constructor(private wishListService: WishListService,
              private alertService: AlertService) {
    this.items = [];
    this.trash = [];
    this.wishList = new WishList();
    this.editableItem = new Item();
  }

  ngOnInit() {
    this.wishListService.wishList$.subscribe((wishList) => {
      this.wishList = wishList;
      console.log(this.wishList.items);
    });
    this.getCreatedItems();
    this.editableItem = new Item();
  }

  showItemDetails(item: Item): void {
    this.itemView = item;
  }

  addItem(item: Item): void {
    this.wishListService.wishList$.subscribe((wishList) => {
      this.wishList = wishList;
    });
    let itemDto: ItemDto = new ItemDto();
    itemDto.item = item;
    itemDto.event_id = this.wishList.id;
    itemDto.priority = 3;
    this.wishList.items.push(itemDto);
  }

  editItem(item: Item): void {
    this.editableItem = item;
  }

  updateEditedItem(item: Item): void {
    let index = this.items.indexOf(this.editableItem);
    this.items.fill(item,index,index+1);
  }

  addCreatedItem(item: Item): void {
    this.items.push(item);
  }

  deleteItem(item: Item): void {
      let index = this.items.indexOf(item);
      this.items.splice(index, 1);
      this.trash.push(item);
      this.hasChanges = true;
  }

  getCreatedItems(): void {
    this.wishListService.getCreatedItems()
      .subscribe((items) => {
        this.items = items;
      }, () => {
        history.back();
        this.alertService.info('Items not found', true);
      });
  }

  executeDelete(): void {
    if (this.trash.length > 0) {
      this.wishListService.deleteItems(this.trash).subscribe(() =>
          this.alertService.success('Items successfully deleted!'),
        () => this.alertService.error('Something wrong'));
    }
  }

  sortItems(prop: string) {
    this.path = prop.split('.');
    this.order = this.order * (-1); // change order
    return false; // do not reload
  }
}
