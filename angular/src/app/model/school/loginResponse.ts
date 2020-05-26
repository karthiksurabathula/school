export class LoginResponse {
  indicator: string;
  token: string;
  role: string;
  schoolId: string;
  classId: string;
  sectionId: string;
  studentId: string;
  redirecturl: string;
  message: string;
}

export interface LoginResponse {
  loginResp: LoginResponse;
}
