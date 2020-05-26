import { Injectable } from "@angular/core";
import { GlobalVariables } from 'src/app/globalVariables';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { analyticsResponse } from 'src/app/model/school/analyticsResponse';

@Injectable({
  providedIn: "root",
})
export class AnalyticsService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  getAnalytics(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<analyticsResponse>(this.url + "api/analytics", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }
}
