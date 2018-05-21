import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AlertService, UserService} from "../../_services";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/wishList/item";
import {ExtendedTag} from "../../_models/wishList/extendedTag";
import {Tag} from "../../_models/wishList/tag";

@Component({
  selector: 'app-create-item',
  templateUrl: './create-item.component.html'
})
export class CreateItemComponent implements OnInit {

  itemForm = this.formBuilder.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(40)]],
    description: ['', [Validators.maxLength(2048)]],
    link: ['', [Validators.minLength(4), Validators.maxLength(128)]],
    dueDate: [''],
    tags: ['']
  });

  additionalForm = this.formBuilder.group({
    image: [''],
    imageUrl: ['']
  });

  mainForm: FormGroup = this.formBuilder.group({
    itemF: this.itemForm,
    additionalProperties: this.additionalForm
  });

  @Input('included') isIncluded: boolean = false;
  @Output('createdItem') createdItem = new EventEmitter<Item>();

  item: Item;

  constructor(private wishListService: WishListService,
              private alertService: AlertService,
              private formBuilder: FormBuilder,
              private userService: UserService) {
    this.item = new Item();
    this.item.tags = [];
  }

  ngOnInit(): void {
  }

  createItem(item: Item) {
    item.image = this.additionalForm.get('imageUrl').value;
    item.creator_customer_login = this.userService.getCurrentLogin();
    item.tags = this.item.tags;
    this.wishListService.createItem(item)
      .subscribe(() => {
          this.alertService.success('Item created.');
        },
        () => {
          this.alertService.error("Something wrong");
        });
    this.createdItem.emit(item);
  }

  removeTag(tag: ExtendedTag) {
    let index = this.item.tags.indexOf(tag);
    this.item.tags.splice(index, 1);
  }

  addTags() {
    if (this.item.tags == null)
      this.item.tags = [];
    let tagsString = this.itemForm.get('tags').value;
    if (tagsString != null) {
      let tagsArray = tagsString.split(',');
      for (let tagString of tagsArray) {
        let tagDto = new ExtendedTag();
        let tag = new Tag();
        tag.name = tagString;
        tagDto.tag = tag;
        this.item.tags.push(tagDto);
      }
    }
  }

  onUpload() {
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

}
