import {Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AlertService, UserService} from "../../_services";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/item";
import {Subscription} from "rxjs/Rx";

@Component({
  selector: 'app-update-item',
  templateUrl: './update-item.component.html'
})
export class UpdateItemComponent implements OnInit, OnChanges {

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

  @Input() editableItem: Item;
  @Output('updatedItem') updatedItem = new EventEmitter<Item>();
  item: Item;

  constructor(private wishListService: WishListService,
              private alertService: AlertService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit(): void { }

  ngOnChanges(changes: SimpleChanges) {
    for (let propName in changes) {
      this.item = (changes[propName].currentValue);
      this.itemForm.controls['name'].setValue(this.item.name);
      this.itemForm.controls['description'].setValue(this.item.description);
      this.itemForm.controls['link'].setValue(this.item.link);
      this.itemForm.controls['dueDate'].setValue(this.item.dueDate);
      this.additionalForm.controls['imageUrl'].setValue(this.item.image);
    }
  }

  updateItem(item: Item) {
    item.id = this.item.id;
    item.creator_customer_login = this.item.creator_customer_login;
    item.image = this.additionalForm.get('imageUrl').value;
    this.wishListService.updateItem(item)
      .subscribe(() => {
          this.alertService.success('Item successfully updated');
        },
        () => {
          this.alertService.error("Something wrong");
        });
    this.updatedItem.emit(item);
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
