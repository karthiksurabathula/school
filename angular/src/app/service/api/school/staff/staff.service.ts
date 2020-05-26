import { staff } from "src/app/model/school/staffResponse";
import { Injectable } from "@angular/core";
import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";
import { GlobalVariables } from "src/app/globalVariables";
import { staffResponse, staffResponse1 } from "src/app/model/school/staffResponse";

@Injectable({
  providedIn: "root",
})
export class StaffService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  cerateStaff(
    bearer: string,
    schoolId: number,
    role: string,
    staffBody: staff
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("role", role);

    return this.http.post<staffResponse1>(this.url + "api/staff", staffBody, {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  updateStaff(
    bearer: string,
    schoolId: number,
    role: string,
    staffBody: staff
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("role", role);

    return this.http.put<staffResponse1>(this.url + "api/staff", staffBody, {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  getStaffBySchool(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<staffResponse>(this.url + "api/staff", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  deleteStaff(
    bearer: string,
    schoolId: number,
    staffId: number
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("staffId", staffId.toString());

    return this.http.delete<staffResponse1>(this.url + "api/staff", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }
}
