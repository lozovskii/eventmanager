import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {WishList} from "../_models/wishlist";
import {AlertService} from "../_services/alert.service";
import {WishListService} from "../_services/wishlist.service";
import {UserService} from "../_services/user.service";
import {ItemDto} from "../_models/dto/itemDto";
import { Location } from '@angular/common';

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishListComponent implements OnInit {
  wishlist: WishList;
  hasChanges: boolean = false;

  constructor(private wishListService: WishListService,
              private userService: UserService,
              private activatedRoute: ActivatedRoute,
              private alertService: AlertService,
              private location: Location) {
  }

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      let eventId = params['id'];
      this.getWishListByEventId(eventId);
    });
  }

  getWishListByEventId(eventId: string): void {
    this.wishListService.getByEventId(eventId)
      .subscribe((wishList) => {
          this.wishlist = wishList;

        }, (error) => {

        this.location.back();

        this.alertService.info('Wish list not found',true);
        }
      );
  }

  bookItem(item: ItemDto): void {

    item.booker_customer_login = this.userService.getCurrentLogin();

    this.hasChanges = true;
  }

  // setPriority(item : ItemDto, priority : any) : void{
  //   item.priority = priority.target.value;
  //   this.hasChanges = true;
  // }

  update(): void {
    this.wishListService.update(this.wishlist).subscribe(data => {

      this.alertService.success('Wishlist successfully updated!',
        true);
    });
  }

  isBooker(bookerLogin: string): boolean {
    return bookerLogin ? this.wishListService.isBooker(bookerLogin) : false;
  }

  //TODO: getBookerByLogin. In the itemDto add field login. In DaoImpl + return login
  // getBookerName() : string{
  //   return ;
  // }
}
