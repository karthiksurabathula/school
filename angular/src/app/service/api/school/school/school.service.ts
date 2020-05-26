import { schoolResponse1 } from "src/app/model/school/schoolResponse";
import { Injectable } from "@angular/core";
import { GlobalVariables } from "src/app/globalVariables";
import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";
import { schoolResponse, school } from "src/app/model/school/schoolResponse";

@Injectable({
  providedIn: "root",
})
export class SchoolService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  getSchools(bearer: string, cityId: number) {
    let params = new HttpParams();
    params = params.append("cityId", cityId.toString());

    return this.http.get<schoolResponse>(this.url + "api/school", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  getSchoolById(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<schoolResponse1>(this.url + "api/schoolInfo", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  getSchoolsByUser(bearer: string) {
    return this.http.get<schoolResponse>(this.url + "api/schoolByUser", {
      headers: new HttpHeaders().set("Authorization", bearer),
    });
  }

  cerateSchool(bearer: string, schoolBody: school, cityId: number) {
    let params = new HttpParams();
    params = params.append("cityId", cityId.toString());

    return this.http.post<schoolResponse1>(
      this.url + "api/school",
      schoolBody,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  updateSchool(bearer: string, schoolBody: school) {
    return this.http.put<schoolResponse1>(this.url + "api/school", schoolBody, {
      headers: new HttpHeaders().set("Authorization", bearer),
    });
  }

  removeSchool(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.delete<schoolResponse1>(this.url + "api/school", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }
}
