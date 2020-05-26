import { Injectable } from "@angular/core";
import { GlobalVariables } from "src/app/globalVariables";
import { HttpParams, HttpHeaders, HttpClient } from "@angular/common/http";
import {
  classResponse,
  classResponse1,
  classs,
} from "src/app/model/school/classResponse";

@Injectable({
  providedIn: "root",
})
export class ClassService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  getClasses(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<classResponse>(this.url + "api/class", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  cerateClass(bearer: string, classBody: classs, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.post<classResponse1>(this.url + "api/class", classBody, {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  updateClass(bearer: string, classBody: classs, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.put<classResponse1>(this.url + "api/class", classBody, {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  removeClass(bearer: string, schoolId: number, classId: number) {
    let params = new HttpParams();
    params = params.append("classId", classId.toString());
    params = params.append("schoolId", schoolId.toString());

    return this.http.delete<classResponse1>(this.url + "api/class", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }
}
