export class classs {
  id: number;
  className: string;
  status: boolean;
}

export class classResponse {
  indicator: string;
  message: string;
  class: Array<classs>;
}

export class classResponse1 {
  indicator: string;
  message: string;
  class: classs;
}

export interface classResponse {
  classResp: classResponse;
}
