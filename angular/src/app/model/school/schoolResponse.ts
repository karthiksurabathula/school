export class school {
  id: number;
  schoolCode: string;
  schoolName: string;
  schoolPhone: string;
  schoolEmail: string;
  location: string;
  address: string;
  status: boolean;
}

export class schoolResponse {
  indicator: string;
  message: string;
  city: Array<school>;
  school: school[];
}

export class schoolResponse1 {
  indicator: string;
  message: string;
  school: school;
}

export interface schoolResponse {
  schoolResp: schoolResponse;
}
