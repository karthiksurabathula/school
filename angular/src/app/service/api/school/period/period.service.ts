import {
  period,
  periodResponse,
  periodResponse1,
} from "src/app/model/school/periodResponse";
import { Injectable } from "@angular/core";
import { GlobalVariables } from "src/app/globalVariables";
import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";

@Injectable({
  providedIn: "root",
})
export class PeriodService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  ceratePeriod(bearer: string, periodBody: period, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.post<periodResponse1>(
      this.url + "api/period",
      periodBody,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  getPriods(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<periodResponse>(this.url + "api/period", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  deletePeriod(bearer: string, schoolId: number, periodId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("periodId", periodId.toString());

    return this.http.delete<periodResponse>(this.url + "api/period", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }
}
