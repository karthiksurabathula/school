import { GlobalService } from "src/app/service/global/global.service";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { city } from "src/app/model/school/cityResponse";
import {
  Component,
  OnInit,
  ViewChild
} from "@angular/core";
import {
  MdbTableDirective,
  ModalDirective,
  ToastService
} from "ng-uikit-pro-standard";
import { CookieService } from "ngx-cookie-service";
import { Router } from "@angular/router";

@Component({
  selector: "app-setup-city",
  templateUrl: "./setup-city.component.html",
  styleUrls: ["./setup-city.component.css"],
})
export class SetupCityComponent implements OnInit {
  @ViewChild("basicModal", { static: true }) basicModal: ModalDirective;
  @ViewChild("conformationModal", { static: true })
  conformationModal: ModalDirective;

  searchText: string;

  editCity = false;
  popupTitle: string;

  ceid: number;
  cestate = "";
  cecity = "";
  cecityId: number;
  cestatus: boolean;
  updateFlag: boolean;

  cities: Array<city>;

  constructor(
    private cityservice: SchoolCityService,
    private cookie: CookieService,
    private toast: ToastService,
    private router: Router,
    private globalService: GlobalService
  ) {}

  ngOnInit() {
    this.globalService.showNavBar();
    if (this.cookie.check("token")) {
    } else {
      this.router.navigate(["/login"]);
    }
    // Get list for table on startup
    this.getcities();
  }

  // check switch state
  changeStatus() {
    this.cestatus = !this.cestatus;
  }

  // Popup
  removeConformationPopup(city: any) {
    this.cecityId = city.id;
    this.conformationModal.show();
  }

  editCityPopup(city: city) {
    this.editCity = true;
    this.popupTitle = "Edit City";
    this.cecityId = city.id;
    this.cestate = city.state;
    this.cecity = city.city;
    this.cestatus = city.status;
    this.updateFlag = true;
    this.basicModal.show();
  }

  showCreateCityPopup() {
    this.editCity = false;
    this.popupTitle = "Create City";
    this.cecityId = 0;
    this.cestate = "";
    this.cecity = "";
    this.cestatus = true;
    this.updateFlag = false;
    this.basicModal.show();
  }

  // Input Validation

  checkIfStateIsEmpty() {
    if (this.cestate.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  checkIfCityIsEmpty() {
    if (this.cecity.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  // API Calls

  getcities() {
    this.cityservice.getcities("Bearer " + this.cookie.get("token")).subscribe(
      (result) => {
        if (result.indicator === "success") {
          this.cities = result.city;
        } else {
          this.cities = [];
        }
      },
      (err) => {}
    );
  }

  add() {
    if (!this.checkIfStateIsEmpty() && !this.checkIfCityIsEmpty()) {
      if (this.updateFlag) {
        this.basicModal.hide();
        this.cityservice
          .updatecity("Bearer " + this.cookie.get("token"), {
            state: this.cestate,
            city: this.cecity,
            id: this.cecityId,
            status: this.cestatus,
          })
          .subscribe(
            (result) => {
              if (result.indicator === "success") {
                this.cestate = "";
                this.cecity = "";
                this.cecityId = null;
                this.getcities();
                this.basicModal.hide();
              } else {
                this.basicModal.show();
              }
            },
            (err) => {}
          );
      } else {
        this.basicModal.hide();
        this.cityservice
          .ceratecity("Bearer " + this.cookie.get("token"), {
            state: this.cestate,
            city: this.cecity,
            id: this.cecityId,
            status: this.cestatus,
          })
          .subscribe(
            (result) => {
              if (result.indicator === "success") {
                this.cestate = "";
                this.cecity = "";
                this.cecityId = null;
                this.getcities();
                this.basicModal.hide();
              } else {
                this.basicModal.show();
              }
            },
            (err) => {}
          );
      }
    } else {
      this.toast.error("Please fill all the mandatory fields");
    }
  }

  remove() {
    this.conformationModal.hide();
    this.cityservice
      .removecity("Bearer " + this.cookie.get("token"), this.cecityId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.getcities();
            this.conformationModal.hide();
          } else {
            this.conformationModal.show();
          }
        },
        (err) => {}
      );
  }
}
