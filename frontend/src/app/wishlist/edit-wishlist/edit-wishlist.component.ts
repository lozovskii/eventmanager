import {Component, Input, OnInit} from '@angular/core';
import {WishListService} from "../../_services/wishlist.service";
import {WishList} from "../../_models/wishList/wishList";

@Component({
  selector: 'app-edit-wishList',
  templateUrl: './edit-wishlist.component.html',
  styleUrls: ['../wishlist/wishlist.component.css']
})
export class EditWishListComponent {
  constructor() {}
}
