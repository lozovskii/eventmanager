import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AlertService, EventService, UserService} from "../../_services";
import {Router} from "@angular/router";
import {ItemDto} from "../../_models/dto/itemDto";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/item";

@Component({
  selector: 'app-create-item',
  templateUrl: './create-item.component.html'
})
export class CreateItemComponent implements OnInit {

  itemForm = this.formBuilder.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(40)]],
    description: ['', [Validators.maxLength(2048)]],
    link: ['', [Validators.minLength(4), Validators.maxLength(128)]],
    dueDate: ['']
  });

  additionalForm = this.formBuilder.group({
    image: [''],
    imageUrl: ['']
  });

  mainForm: FormGroup = this.formBuilder.group({
    item: this.itemForm,
    additionalProperties: this.additionalForm
  });

  @Output('createdItem') createdItem = new EventEmitter<Item>();

  itemDto: ItemDto;
  item: Item;

  constructor(private wishListService: WishListService,
              private alertService: AlertService,
              private formBuilder: FormBuilder,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.itemDto = new ItemDto();
  }

  createItem(item: Item) {
    this.item = item;
    this.item.image = this.additionalForm.get('imageUrl').value;
    this.item.creator_customer_login = this.userService.getCurrentLogin();
    this.wishListService.createItem(item)
      .subscribe(() => {
        this.alertService.success('Item created.');
      },
      () => {
        this.alertService.error("Something wrong");
      });
    this.createdItem.emit(item);
  }

  onUpload() { }

  get name() {
    return this.itemForm.get('name');
  }

  get description() {
    return this.itemForm.get('description');
  }

  get link() {
    return this.itemForm.get('link');
  }

}
