import { section } from "src/app/model/school/sectionResponse";
import { Injectable } from "@angular/core";
import { GlobalVariables } from "src/app/globalVariables";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import {
  sectionResponse,
  sectionResponse1,
} from "src/app/model/school/sectionResponse";

@Injectable({
  providedIn: "root",
})
export class SectionService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  getSections(bearer: string, schoolId: number, calssId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", calssId.toString());

    return this.http.get<sectionResponse>(this.url + "api/section", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  cerateSection(
    bearer: string,
    sectionBody: section,
    schoolId: number,
    calssId: number
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", calssId.toString());

    return this.http.post<sectionResponse1>(
      this.url + "api/section",
      sectionBody,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  updateSection(
    bearer: string,
    sectionBody: section,
    schoolId: number,
    calssId: number
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", calssId.toString());

    return this.http.put<sectionResponse1>(
      this.url + "api/section",
      sectionBody,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  removeSection(bearer: string, schoolId: number, sectionId: number) {
    let params = new HttpParams();
    params = params.append("sectionId", sectionId.toString());
    params = params.append("schoolId", schoolId.toString());

    return this.http.delete<sectionResponse1>(this.url + "api/section", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }
}
