export class analytics {
  descriptor: string;
  count: number;
  image: string;
  refer: string;
}

export class analyticsResponse {
  indicator: string;
  message: string;
  analytics: Array<analytics>;
}

export class analyticsResponse1 {
  indicator: string;
  message: string;
  analytics: analytics;
}

export interface analyticsResponse {
  analyticsResp: analytics;
}
