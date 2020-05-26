import { GlobalVariables } from "src/app/globalVariables";
import {
  cityResponse,
  city,
  cityResponse1,
} from "src/app/model/school/cityResponse";
import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";

@Injectable({
  providedIn: "root",
})
export class SchoolCityService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  getcities(bearer: string) {
    return this.http.get<cityResponse>(this.url + "api/schoolCity", {
      headers: new HttpHeaders().set("Authorization", bearer),
    });
  }

  ceratecity(bearer: string, citybody: city) {
    return this.http.post<cityResponse1>(
      this.url + "api/schoolCity",
      citybody,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
      }
    );
  }

  updatecity(bearer: string, citybody: city) {
    return this.http.put<cityResponse1>(this.url + "api/schoolCity", citybody, {
      headers: new HttpHeaders().set("Authorization", bearer),
    });
  }

  removecity(bearer: string, cityId: number) {
    let params = new HttpParams();
    params = params.append("cityId", cityId.toString());

    return this.http.delete<cityResponse1>(this.url + "api/schoolCity", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }
}
