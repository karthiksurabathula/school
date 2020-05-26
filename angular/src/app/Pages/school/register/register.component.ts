import { LoginService } from "../../../service/api/school/login/login.service";
import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { ToastService } from "ng-uikit-pro-standard";
import { GlobalService } from "src/app/service/global/global.service";

@Component({
  selector: "app-register",
  templateUrl: "./register.component.html",
  styleUrls: ["./register.component.css"],
})
export class RegisterComponent implements OnInit {
  username = "";

  constructor(
    private loginServie: LoginService,
    private router: Router,
    private toast: ToastService,
    private globalService: GlobalService
  ) {}

  ngOnInit() {
    this.globalService.hideNavBar();
  }

  // register() {
  //   this.loginServie.register(this.username, this.password).subscribe(
  //     (result) => {},
  //     (err) => {}
  //   );
  // }

  checkUsername() {
    if (this.username.length > 0) {
      return false;
    } else {
      return true;
    }
  }
}
