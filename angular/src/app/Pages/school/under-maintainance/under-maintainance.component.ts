import { Component, OnInit } from "@angular/core";
import { GlobalService } from "src/app/service/global/global.service";

@Component({
  selector: "app-under-maintainance",
  templateUrl: "./under-maintainance.component.html",
  styleUrls: ["./under-maintainance.component.css"],
})
export class UnderMaintainanceComponent implements OnInit {
  constructor(
    private globalService: GlobalService
  ) {}

  ngOnInit() {
    this.globalService.hideNavBar();
  }
}
