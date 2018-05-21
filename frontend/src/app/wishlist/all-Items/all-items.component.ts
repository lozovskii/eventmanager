import {Component, Input, OnInit} from '@angular/core';
import {AlertService} from "../../_services/alert.service";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/wishList/item";
import {UserService} from "../../_services/user.service";
import {WishList} from "../../_models/wishList/wishList";
import {WishListItem} from "../../_models/wishList/wishListItem";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-all-items',
  templateUrl: './all-items.component.html',
  styleUrls: ['../wishlist/wishlist.component.css']
})
export class AllItemsComponent implements OnInit {
  @Input('included') isIncluded: boolean = false;

  filterInput = new FormControl();
  filterText: string;
  filterPlaceholder: string;
  itemView: Item;
  editableItem: Item;
  wishList: WishList;
  item: Item;
  items: Item[];
  path: string[] = ['name'];
  order: number = 1;

  constructor(private wishListService: WishListService,
              private userService: UserService,
              private alertService: AlertService) {
    this.items = [];
    this.item = new Item();
    this.itemView = new Item();
    this.editableItem = new Item();
  }

  ngOnInit() {
    if (this.isIncluded)
      this.wishListService.wishList$.subscribe((wishList) => {
        this.wishList = wishList;
      });
    this.getAllItems();

    this.filterText = '';
    this.filterPlaceholder = 'You can filter values by name, description, link and creator login';
    this.filterInput
      .valueChanges
      .debounceTime(200)
      .subscribe(term => {
        this.filterText = term;
      });
  }

  showItemDetails(item: Item): void {
    this.itemView = item;
  }

  addItem(item: Item): void {
    let wishListItem: WishListItem = new WishListItem();
    wishListItem.item = item;
    wishListItem.event_id = this.wishList.id;
    wishListItem.priority = 3;
    this.wishList.items.push(wishListItem);
  }

  copyItem(item: Item): void {
    delete item.id;
    item.creator_customer_login = this.userService.getCurrentLogin();
    this.wishListService.createItem(item)
      .subscribe(() => {
          this.alertService.success('Item added to you Collection.');
        },
        () => {
          this.alertService.error("Something wrong");
        });
  }

  getAllItems(): void {
    this.wishListService.getAllItems()
      .subscribe((items) => {
        this.items = items;
      }, () => {
        this.alertService.info('Items not found');
      });
  }

  sortItems(prop: string) {
    this.path = prop.split('.');
    this.order = this.order * (-1); // change order
    return false; // do not reload
  }
}
