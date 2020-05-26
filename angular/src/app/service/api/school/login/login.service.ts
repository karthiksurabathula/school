import { LoginResponse } from "src/app/model/school/loginResponse";
import {
  HttpClient,
  HttpHeaders,
  HttpParams,
} from "@angular/common/http";
import { Injectable } from "@angular/core";
import { GlobalVariables } from "src/app/globalVariables";

@Injectable({
  providedIn: "root",
})
export class LoginService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  public userLogin(username: string, password: string) {
    let user = {
      username: username,
      password: password,
    };

    return this.http.post<LoginResponse>(this.url + "authenticate", user, {
      headers: new HttpHeaders().set("Content-Type", "application/json"),
    });
  }

  public register(username: string, password: string) {
    let user = {
      username: username,
      password: password,
    };

    return this.http.post<LoginResponse>(this.url + "register", user, {
      headers: new HttpHeaders().set("Content-Type", "application/json"),
    });
  }

  public logout(bearer: string) {
    return this.http.post<LoginResponse>(
      this.url + "api/logout",
      {},
      {
        headers: new HttpHeaders().set("Authorization", bearer),
      }
    );
  }

  public reset(password: string, password1: string, restetToken: string) {
    let resetReq = {
      password: password,
      password1: password1,
      restetToken: restetToken,
    };

    return this.http.post<LoginResponse>(this.url + "reset", resetReq, {
      headers: new HttpHeaders().set("Content-Type", "application/json"),
    });
  }

  public resetPassWithToken(
    bearer: string,
    currentPassword: string,
    password: string,
    password1: string,
    restetToken: string
  ) {
    let resetReq = {
      currentPassword: currentPassword,
      password: password,
      password1: password1,
      restetToken: restetToken,
    };

    return this.http.post<LoginResponse>(this.url + "api/reset", resetReq, {
      headers: new HttpHeaders()
        .set("Content-Type", "application/json")
        .set("Authorization", bearer),
    });
  }

  public appStatus(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<LoginResponse>(this.url + "api/appStatus", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params
    });
  }
}
