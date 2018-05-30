import {
  ChangeDetectionStrategy, Component, EventEmitter, Input, OnChanges, OnInit, Output,
  SimpleChanges
} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AlertService} from "../../_services";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/wishList/item";
import {ExtendedTag} from "../../_models/wishList/extendedTag";
import {Tag} from "../../_models/wishList/tag";
import {MAX_IMG_SIZE} from "../../app.config";
import {ALLOWED_IMG_FORMATS} from "../../app.config";

@Component({
  selector: 'app-update-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './update-item.component.html'
})
export class UpdateItemComponent implements OnInit, OnChanges {

  allowedFormats: string[] = ALLOWED_IMG_FORMATS;
  maxImageSize = MAX_IMG_SIZE;
  base64Image: string;
  loading = true;

  itemForm = this.formBuilder.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(40), Validators.pattern("^[ a-zA-Zа-яА-ЯієїґІЄЇҐ]*$")]],
    description: ['', [Validators.maxLength(2048)]],
    link: ['', [Validators.minLength(4), Validators.maxLength(128),
      Validators.pattern("^(ftp|http|https):\\/\\/[^ \"]+$")]],
    dueDate: [''],
    tags: ['']
  });

  additionalForm = this.formBuilder.group({
    image: [''],
    imageUrl: ['', [Validators.minLength(4), Validators.maxLength(128),
      Validators.pattern("(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|gif|png)")]]
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

  ngOnInit(): void { }

  ngOnChanges(changes: SimpleChanges) {
    this.item = changes['editableItem'].currentValue;
    this.itemForm.controls['name'].setValue(this.item.name);
    this.itemForm.controls['description'].setValue(this.item.description);
    this.itemForm.controls['link'].setValue(this.item.link);
    this.itemForm.controls['dueDate'].setValue(this.item.dueDate);
    this.itemForm.controls['tags'].reset();
    this.additionalForm.controls['imageUrl'].setValue(this.item.image);

    let today = new Date().toISOString().split('T')[0];
    document.getElementById("dueDate").setAttribute('min', today);
  }

  updateItem(item: Item) {
    this.item.name = item.name;
    this.item.dueDate = item.dueDate;
    this.item.description = item.description;
    this.item.link = item.link;
    if (this.base64Image)
      this.item.image = this.base64Image;
    else
      this.item.image = this.additionalForm.get('imageUrl').value;

    this.wishListService.updateItem(this.item)
      .subscribe(() => {
          this.alertService.success('Item successfully updated');
        },
        () => {
          this.alertService.error("Something wrong");
        });

    this.updatedItem.emit(this.item);

    if (this.trash.length > 0) {
      this.wishListService.deleteTags(this.trash)
        .subscribe(() => {
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
    if (!this.item.tags)
      this.item.tags = [];

    let tagsString = this.itemForm.get('tags').value;

    if (tagsString) {
      let tagsArray = tagsString.split(',');

      if (tagsArray.length > 0)
        for (let tagString of tagsArray) {
          let tag = new Tag();
          tag.name = tagString;
          let tagDto = new ExtendedTag();
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


  changeListener($event): void {
    this.readThis($event.target);
  }

  static getFileExtension(filename) {
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

  validImg(file: File) {
    let format = UpdateItemComponent.getFileExtension(file.name);
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
