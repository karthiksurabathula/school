export class section {
  id: number;
  sectionName: string;
  status: boolean;
}

export class sectionResponse {
  indicator: string;
  message: string;
  section: Array<section>;
}

export class sectionResponse1 {
  indicator: string;
  message: string;
  section: section;
}

export interface sectionResponse {
  sectionResp: sectionResponse;
}
