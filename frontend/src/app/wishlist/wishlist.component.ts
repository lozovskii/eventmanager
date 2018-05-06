import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {WishList} from "../_models/wishlist";
import {AlertService} from "../_services/alert.service";
import {WishListService} from "../_services/wishlist.service";

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishListComponent implements OnInit {
  wishlist: WishList;

  constructor(private wishListService: WishListService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private alertService : AlertService) {
  }

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      let eventId = params['id'];
      this.getWishListByEventId(eventId);
    });
  }

  getWishListByEventId(eventId : string): void {
    this.wishListService.getByEventId(eventId)
      .subscribe((wishList) => {
        this.wishlist = wishList;

        if(this.wishlist == null){
          this.alertService.info('Wish list not found',true);
        }
      });
  }
}
