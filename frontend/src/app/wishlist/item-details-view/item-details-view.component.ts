import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {Item} from "../../_models/item";
import {ItemDto} from "../../_models/dto/itemDto";
import {WishList} from "../../_models/wishlist";
import {WishListService} from "../../_services/wishlist.service";

@Component({
  selector: 'app-item-details-view',
  templateUrl: './item-details-view.component.html'
})
export class ItemDetailsViewComponent implements OnInit, OnChanges {

  @Input('isBooker') boolean = false;
  @Input('itemView') itemView: Item;
  @Input('itemDtoView') itemDtoView: ItemDto;
  @Output('updatedItem') updatedItem = new EventEmitter<ItemDto>();
  @Output('cancelledItem') cancelledItem = new EventEmitter<ItemDto>();
  @Output('bookedItem') bookedItem = new EventEmitter<ItemDto>();
  item: Item;
  itemDto: ItemDto;
  isBooker: boolean = false;

  constructor(private wishListService: WishListService) {
    this.item = new Item();
    this.itemDto = new ItemDto();
  }

  ngOnInit(): void {
    this.item = new Item();
    this.itemDto = new ItemDto();
  }

  ngOnChanges(changes: SimpleChanges) {
    this.item = new Item();
    this.itemDto = new ItemDto();
    for (let prop in changes) {
      if (prop == 'itemDtoView') {
        this.itemDto = changes['itemDtoView'].currentValue;
        if (this.itemDto.booker_customer_login != null)
          this.isBooker = this.checkBooker(this.itemDto.booker_customer_login);
      }
      else
        if (prop == 'itemView')
          this.item = changes['itemView'].currentValue;
    }
      }

  cancelBooking(itemDto: ItemDto): void {
    this.cancelledItem.emit(itemDto);
    this.isBooker = false;
  }

  updatePriority(itemDto: ItemDto): void{
    this.updatedItem.emit(itemDto);
  }

  bookItem(itemDto: ItemDto): void {
    this.bookedItem.emit(itemDto);
    this.isBooker = true;
  }

  checkBooker(bookerLogin: string): boolean {
    return this.wishListService.isBooker(bookerLogin);
  }

}
