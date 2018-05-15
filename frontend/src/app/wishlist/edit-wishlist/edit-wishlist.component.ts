import {Component, Input, OnInit} from '@angular/core';
import {WishListService} from "../../_services/wishlist.service";
import {WishList} from "../../_models/wishlist";

@Component({
  selector: 'app-edit-items',
  templateUrl: './edit-wishlist.component.html',
  styleUrls: ['../wishlist/wishlist.component.css']
})
export class EditWishListComponent implements OnInit {

  wishList: WishList;

  constructor(private wishListService: WishListService) { }

  ngOnInit() {
    this.wishListService.wishList$.subscribe((wishList) => this.wishList = wishList);
  }
}
