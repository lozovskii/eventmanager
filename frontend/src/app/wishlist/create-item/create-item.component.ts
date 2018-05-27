import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AlertService, UserService} from "../../_services";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/wishList/item";
import {ExtendedTag} from "../../_models/wishList/extendedTag";
import {Tag} from "../../_models/wishList/tag";
import {MAX_IMG_SIZE} from "../../app.config";
import {ALLOWED_IMG_FORMATS} from "../../app.config";

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
    imageUrl: ['', [Validators.minLength(4), Validators.maxLength(128), Validators.pattern("https:\\+")]]
  });

  mainForm: FormGroup = this.formBuilder.group({
    itemF: this.itemForm,
    additionalProperties: this.additionalForm
  });

  @Input('included') isIncluded: boolean = false;
  @Output('createdItem') createdItem = new EventEmitter<Item>();

  allowedFormats: string[] =  ALLOWED_IMG_FORMATS;
  maxImageSize = MAX_IMG_SIZE;
  base64Image: string;
  loading = true;

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
    item.creator_customer_login = this.userService.getCurrentLogin();
    item.tags = this.item.tags;
    if (this.base64Image)
      item.image = this.base64Image;
    else
      item.image = this.additionalForm.get('imageUrl').value;
    this.wishListService.createItem(item)
      .subscribe(() => {
          this.alertService.success('Item successfully created.');
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

  get name() {
    return this.itemForm.get('name');
  }

  get description() {
    return this.itemForm.get('description');
  }

  get link() {
    return this.itemForm.get('link');
  }

  get imageUrl(){
    return this.additionalForm.get('imageUrl');
  }



  changeListener($event): void {
    this.readThis($event.target);
  }

  getFileExtension(filename) {
    return filename.split('.').pop();
  }

  validFormat(format: string) {
    let isValid = false;
    for (let allowedFormat of this.allowedFormats) {
      if (allowedFormat == format) {
        isValid = true;
      }
    }
    return isValid;
  }

  validSize(file) {
    return file.size <= this.maxImageSize;
  }

  validImg(file: File){
    let format = this.getFileExtension(file.name);
    console.log(file.size);
    this.loading = !(this.validFormat(format) && this.validSize(file));
  }

  readThis(inputValue: any): void {
    let file: File = inputValue.files[0];
    let myReader: FileReader = new FileReader();
    myReader.onloadend = () => {
      this.base64Image = myReader.result;
      this.validImg(file);
    };
    myReader.readAsDataURL(file);
  }
}
