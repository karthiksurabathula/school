import { Injectable } from "@angular/core";
import { GlobalVariables } from "src/app/globalVariables";
import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";
import { user, userResponse, userResponse1 } from "src/app/model/school/loginUser";

@Injectable({
  providedIn: "root",
})
export class UserManagementService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  getUsers(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<userResponse>(this.url + "api/users", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  saveUserSettings(bearer: string, schoolId: number, user: user) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.post<userResponse1>(this.url + "api/userSettings", user, {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  public resetPassword(
    bearer: string,
    username: string,
    password: string,
    password1: string,
    schoolId: number
  ) {
    let resetReq = {
      password: password,
      password1: password1,
      username: username,
    };

    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.post<userResponse1>(this.url + "api/resetPassword", resetReq, {
      headers: new HttpHeaders()
        .set("Content-Type", "application/json")
        .set("Authorization", bearer),
      params,
    });
  }
}
