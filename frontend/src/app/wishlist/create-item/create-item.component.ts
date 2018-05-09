import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {AlertService, EventService, UserService} from "../../_services";
import {Router} from "@angular/router";
import {VISIBILITY} from "../../event-visibility";
import {ItemDto} from "../../_models/dto/itemDto";
import {WishListService} from "../../_services/wishlist.service";
import {WishList} from "../../_models/wishlist";
import {Item} from "../../_models/item";

@Component({
  selector: 'app-event',
  templateUrl: './create-item.component.html',
  styleUrls: ['./create-item.component.css']
})
export class CreateItemComponent implements OnInit {

  isValidFormSubmitted = null;

  itemForm = this.formBuilder.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
    description: ['', [Validators.maxLength(1024)]],
    link: ['', [Validators.minLength(4), Validators.maxLength(128)]],
    dueDate: [''],
    priority: ['']
  });

  wishList: WishList;
  itemDto: ItemDto;
  items: Item[];
  item: Item;

  constructor(private router: Router,
              private wishListService: WishListService,
              private alertService: AlertService,
              private formBuilder: FormBuilder,
              private eventService: EventService) {
  }

  ngOnInit(): void {
    this.itemDto = new ItemDto();
  }

  createItem(itemForm: ItemDto) {

     this.itemDto = itemForm;

    console.log('test: ' + JSON.stringify(itemForm));

    console.log(this.itemDto.priority);

    console.log(this.itemDto.item.name);

    // this.items.push(itemForm);

  }

  save(itemDto: ItemDto) {

    this.isValidFormSubmitted = false;

    if (this.itemForm.invalid) {
      return;
    }
    this.isValidFormSubmitted = true;

    this.itemDto.event_id = '';

    console.log('user: ' + JSON.stringify(itemDto));
    // this.loading = true;

    this.wishListService.create(this.wishList)
      .subscribe(() => {
          this.alertService.success('Wish List created.', true);
          setTimeout(() => this.router.navigate(["/"]), 5000);
        },
        (error) => {
          this.alertService.error(error.error);
        });
  }


  get name() {
    return this.itemForm.get('name');
  }

  get description() {
    return this.itemForm.get('description');
  }

  get link() {
    return this.itemForm.get('link');
  }

  get dueDate() {
    return this.itemForm.get('dueDate');
  }

  get status() {
    return this.itemForm.get('status');
  }

}
