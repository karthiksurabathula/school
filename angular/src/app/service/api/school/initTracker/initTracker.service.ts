import { Injectable } from "@angular/core";
import { GlobalVariables } from "src/app/globalVariables";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { initTrackerResponse } from "src/app/model/school/initTrackerResponse";

@Injectable({
  providedIn: "root",
})
export class InitTrackerService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  checkDataCreationStatus(bearer: string) {
    return this.http.get<initTrackerResponse>(this.url + "api/dummyData", {
      headers: new HttpHeaders().set("Authorization", bearer),
    });
  }

  createSampleData(
    bearer: string,
    cityCount: number,
    schoolCount: number,
    classCount: number,
    sectionCount: number,
    subjectCount: number,
    staffCount: number,
    studentCout: number,
    periodCount: number
  ) {
    let params = new HttpParams();
    params = params.append("cityCount", cityCount.toString());
    params = params.append("schoolCount", schoolCount.toString());
    params = params.append("classCount", classCount.toString());
    params = params.append("sectionCount", sectionCount.toString());
    params = params.append("subjectCount", subjectCount.toString());
    params = params.append("staffCount", staffCount.toString());
    params = params.append("studentCout", studentCout.toString());
    params = params.append("periodCount", periodCount.toString());

    return this.http.post<initTrackerResponse>(
      this.url + "api/dummyData",
      {},
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  createSampleDailyTasksData(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.put<initTrackerResponse>(
      this.url + "api/dummyData",
      {},
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }
}
