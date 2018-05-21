
import {Component, ElementRef, EventEmitter, NgZone, OnInit, Output} from '@angular/core';
import {FormControl} from '@angular/forms';
import {MapsAPILoader} from '@agm/core';
import {} from '@types/googlemaps';
import {Location} from "../../_models/location";

@Component({
  selector: 'app-google-maps',
  templateUrl: './google-maps.component.html',
  styleUrls: ['./google-maps.component.css']
})
export class GoogleMapsComponent implements OnInit {
  public latitude: number;
  public longitude: number;
  public searchControl: FormControl;
  public zoom: number;
  public inputAddress: string = '';
  public street: string = '';
  public house: string = '';
  public city: string = '';
  public country: string = '';

  @Output() onAddLocation = new EventEmitter<{country: string, city: string, street: string, house: string}>();

  public searchElementRef: ElementRef;

  constructor(public mapsAPILoader: MapsAPILoader,
              public ngZone: NgZone) {
  }

  ngOnInit() {
    this.zoom = 16;
    this.latitude = 50.472026;
    this.longitude = 30.518224;
    this.searchControl = new FormControl();

    setTimeout(() => {
      this.mapsAPILoader.load().then(() => {
        let autocomplete = new google.maps.places.Autocomplete(this.searchElementRef.nativeElement, {
          types: ['address']
        });

        autocomplete.addListener('place_changed', () => {
          this.ngZone.run(() => {
            let place: google.maps.places.PlaceResult = autocomplete.getPlace();

            if (place.geometry === undefined || place.geometry === null) {
              return;
            }

            this.latitude = place.geometry.location.lat();
            this.longitude = place.geometry.location.lng();
            this.zoom = 16;
          });
        });
      });
    }, 700);
  }

  placeMarker($event) {
    this.latitude = $event.coords.lat;
    this.longitude = $event.coords.lng;
    this.geocodeLatLng(new google.maps.LatLng(this.latitude, this.longitude));
  }

  geocodeLatLng(latLng) {
    let geocoder = new google.maps.Geocoder();
    geocoder.geocode({'location': latLng}, (results, status) => {
      if (status == google.maps.GeocoderStatus.OK) {
        if (results[1]) {
          this.inputAddress = results[0].formatted_address;
          this.street = this.inputAddress.split(',')[0].trim();
          console.log(this.street);
          this.house = this.inputAddress.split(',')[1].trim();
          console.log(this.house);
          this.city = this.inputAddress.split(',')[2].trim();
          console.log(this.city);
          this.country = this.inputAddress.split(',')[3].trim();
          console.log(this.country);

          this.onAddLocation.emit({
            country: this.country,
            city: this.city,
            street: this.street,
            house: this.house
          })

          this.street= '';
          this.house = '';
          this.city = '';
          this.country= '';

        } else {
          this.inputAddress = 'Location not found';
        }
      }
      else {
        this.inputAddress = 'Geocoder failed due to: ' + status;
      }
    });
  }

}

