import {Item} from "../item";

export class ItemDto{

  item: Item;

  item_wishlist_id: string;

  event_wishlist_id: string;

  event_id: string;

  booker_customer_login: string;

  private _priority: number;


  get priority(): number {
    return this._priority;
  }

  set priority(value: number) {
    this._priority = value;
  }
}
