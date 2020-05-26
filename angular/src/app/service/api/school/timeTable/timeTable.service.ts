import { timeTable } from "src/app/model/school/timtableResponse";
import { Injectable } from "@angular/core";
import { GlobalVariables } from "src/app/globalVariables";
import { HttpParams, HttpClient, HttpHeaders } from "@angular/common/http";
import {
  timeTableResponse,
  timeTableRequest,
} from "src/app/model/school/timtableResponse";

@Injectable({
  providedIn: "root",
})
export class TimeTableService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  getTimeTable(
    bearer: string,
    schoolId: number,
    classId: number,
    sectionId: number,
    day: string
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());
    params = params.append("sectionId", sectionId.toString());
    params = params.append("day", day);

    return this.http.get<timeTableResponse>(this.url + "api/timeTable", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }


  getTimeTableStaff(
    bearer: string,
    schoolId: number,
    day: string
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("day", day);

    return this.http.get<timeTableResponse>(this.url + "api/timeTableStaff", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  saveTimeTable(
    bearer: string,
    schoolId: number,
    classId: number,
    sectionId: number,
    day: string,
    timeTableRequestBody: timeTableRequest
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());
    params = params.append("sectionId", sectionId.toString());
    params = params.append("day", day);

    return this.http.post<timeTableResponse>(
      this.url + "api/timeTable",
      timeTableRequestBody,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }
}
