import {
  rolesResponse,
  rolesResponse1,
} from "src/app/model/school/userRolesResponse";
import { Injectable } from "@angular/core";
import { GlobalVariables } from "src/app/globalVariables";
import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";

@Injectable({
  providedIn: "root",
})
export class UserRolesService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  getRoles(bearer: string) {
    return this.http.get<rolesResponse>(this.url + "api/role", {
      headers: new HttpHeaders().set("Authorization", bearer),
    });
  }

  getRolesByStaffId(bearer: string, schoolId: number, staffId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<rolesResponse1>(
      this.url + "api/role/staffId=" + staffId,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }
}
