import { Injectable } from '@angular/core';
import { GlobalVariables } from 'src/app/globalVariables';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { LoginResponse } from 'src/app/model/school/loginResponse';

@Injectable({
  providedIn: 'root'
})
export class AppStatusService {

  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  public appStatus(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<LoginResponse>(this.url + "api/appStatus", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params
    });
  }

  public appStatusAdmin(bearer: string) {
    return this.http.get<LoginResponse>(this.url + "api/appStatusAdmin", {
      headers: new HttpHeaders().set("Authorization", bearer)
    });
  }

}
