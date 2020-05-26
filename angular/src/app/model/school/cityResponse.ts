export class city {
  id: number;
  city: string;
  state: string;
  status: boolean;
}

export class cityResponse {
  indicator: string;
  message: string;
  city: Array<city>;
}

export class cityResponse1 {
  indicator: string;
  message: string;
  city: city;
}

export interface cityResponse {
  cityResp: cityResponse;
}
