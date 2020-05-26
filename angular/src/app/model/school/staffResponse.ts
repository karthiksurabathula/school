export class staff {
  id: number;
  name: string;
  gender: string;
  bloodGroup: string;
  dob: string;
  address: string;
  qualification: string;
  phoneNo: string;
  email: string;
  status: boolean;
}

export class staffResponse {
  indicator: string;
  message: string;
  staff: Array<staff>;
}

export class staffResponse1 {
  indicator: string;
  message: string;
  staff: staff;
}

export interface staffResponse {
  staffResp: staffResponse;
}
