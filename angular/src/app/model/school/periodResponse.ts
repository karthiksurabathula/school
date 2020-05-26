export class period {
  id: number;
  startTime: string;
  startTimeDisp: string;
  endTime: string;
  endTimeDisp: string;
  description: string;
}

export class periodResponse {
  indicator: string;
  message: string;
  period: Array<period>;
}

export class periodResponse1 {
  indicator: string;
  message: string;
  period: period;
}

export interface periodResponse {
  periodResp: periodResponse;
}
