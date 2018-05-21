import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AlertService} from "../../_services";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/wishList/item";
import {ExtendedTag} from "../../_models/wishList/extendedTag";
import {Tag} from "../../_models/wishList/tag";

@Component({
  selector: 'app-update-item',
  templateUrl: './update-item.component.html'
})
export class UpdateItemComponent implements OnInit, OnChanges {

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
    item: this.itemForm,
    additionalProperties: this.additionalForm
  });

  @Input() editableItem: Item;
  @Output('updatedItem') updatedItem = new EventEmitter<Item>();

  item: Item;
  trash: ExtendedTag[];

  constructor(private wishListService: WishListService,
              private alertService: AlertService,
              private formBuilder: FormBuilder) {
    this.trash = [];
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges) {
    for (let propName in changes) {
      this.item = (changes[propName].currentValue);
      this.itemForm.controls['name'].setValue(this.item.name);
      this.itemForm.controls['description'].setValue(this.item.description);
      this.itemForm.controls['link'].setValue(this.item.link);
      this.itemForm.controls['dueDate'].setValue(this.item.dueDate);
      this.itemForm.controls['tags'].reset();
      this.additionalForm.controls['imageUrl'].setValue(this.item.image);
    }
  }

  updateItem(item: Item) {
    item.id = this.item.id;
    item.creator_customer_login = this.item.creator_customer_login;
    item.image = this.additionalForm.get('imageUrl').value;
    item.tags = this.item.tags;
    this.wishListService.updateItem(item)
      .subscribe(() => {
          this.alertService.success('Item successfully updated');
        },
        () => {
          this.alertService.error("Something wrong");
        });

    this.updatedItem.emit(item);

    if (this.trash.length > 0) {
      this.wishListService.deleteTags(this.trash)
        .subscribe(() => {
            this.alertService.success('Tags successfully updated');
          },
          () => {
            this.alertService.error("Something wrong");
          });
    }
  }

  removeTag(tag: ExtendedTag) {
    this.trash.push(tag);
    let index = this.item.tags.indexOf(tag);
    this.item.tags.splice(index, 1);
  }

  addTags() {
    if (this.item.tags == null)
      this.item.tags = [];
    let tagsString = this.itemForm.get('tags').value;
    let tagsArray = tagsString.split(',');

    for (let tagString of tagsArray) {
      let tag = new Tag();
      tag.name = tagString;
      let tagDto = new ExtendedTag();
      tagDto.tag = tag;
      this.item.tags.push(tagDto);
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
