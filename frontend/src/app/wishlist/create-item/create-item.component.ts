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

  itemForm = this.formBuilder.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
    description: ['', [Validators.maxLength(1024)]],
    link: ['', [Validators.minLength(4), Validators.maxLength(128)]],
    dueDate: ['']
  });

  additionalForm = this.formBuilder.group({
    priority: [''],
    image: [''],
    imageUrl: ['']
  });

  mainForm: FormGroup = this.formBuilder.group({
    item: this.itemForm,
    additionalProperties: this.additionalForm
  });


  itemDto: ItemDto;
  item: Item;

  constructor(private router: Router,
              private wishListService: WishListService,
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

    console.log(JSON.stringify(this.item));

    this.wishListService.createItem(item)
      .subscribe(() => {
        this.alertService.success('Item created.');
      },
      (error) => {
        this.alertService.error("Something wrong");
      });

  }

  onUpload() {
    // this.http is the injected HttpClient
    // this.https.post('my-backend.com/file-upload', this.selectedFile)
    //   .subscribe(...);
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
