import {Component, OnInit} from '@angular/core';
import {WishList} from "../../_models/wishlist";
import {AlertService} from "../../_services/alert.service";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/item";
import {ItemDto} from "../../_models/dto/itemDto";
import {Subscription} from "rxjs";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-bookeditems',
  templateUrl: './booked-items.component.html',
  styleUrls: ['../wishlist/wishlist.component.css']
})
export class BookedItemsComponent implements OnInit {

  filterInput = new FormControl();
  filterText: string;
  filterPlaceholder: string;
  wishList: WishList;
  updatableWishList: WishList;
  hasChanges: boolean = false;
  items: Item[];
  itemDtoView: ItemDto;
  value : number;
  path: string[] = ['item'];
  order: number = 1;

  constructor(private wishListService: WishListService,
              private alertService: AlertService) {
    this.wishList = new WishList();
    this.updatableWishList = new WishList();
    this.updatableWishList.items = [];
    this.itemDtoView = new ItemDto();
  }

  ngOnInit() {
    this.getBookedItems();

    this.filterText = '';
    this.filterPlaceholder = 'You can filter values by name, description, link and creator login';
    this.filterInput
      .valueChanges
      .debounceTime(200)
      .subscribe(term => {
        this.filterText = term;
      });
  }

  getBookedItems(): void {
    this.wishListService.getBookedItems()
      .subscribe((wishList) => {
        this.wishList = wishList;
      }, () => {
      this.alertService.error("Items not found")});
  }

  showItemDetails(item: ItemDto): void {
    this.itemDtoView = item;
  }

  cancelBooking(itemDto: ItemDto): void {
    itemDto.booker_customer_login = null;
    this.updatableWishList.items.push(itemDto);

    let index = this.wishList.items.indexOf(itemDto);
    this.wishList.items.splice(index,1);
    this.hasChanges = true;
  }

  updatedPriority(item: ItemDto): void{
    this.updatableWishList.items.push(item);
    this.hasChanges = true;
  }

  sortItems(prop: string) {
    this.path = prop.split('.');
    this.order = this.order * (-1);
    return false;
  }

  update(): void {
    if (this.updatableWishList.items != null)
    this.wishListService.update(this.updatableWishList).subscribe(() => {
      this.alertService.success('Wish list successfully updated!');
    }, () => {
      this.alertService.error('Something wrong');
    });
  }
}
