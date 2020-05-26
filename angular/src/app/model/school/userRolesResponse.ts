export class role {
  role: string;
}

export class rolesResponse {
  indicator: string;
  message: string;
  roles: Array<role>;
}

export class rolesResponse1 {
  indicator: string;
  message: string;
  roles: role;
}

export interface rolesResponse {
  rolesResp: rolesResponse;
}
