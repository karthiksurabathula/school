export class exam {
  id: number;
  name: string;
  description: string;
  scope: string;
}

export class examPartResp {
  exam: exam;
  classId: number;
  schoolId: number;
  sectionId: number;
  className: string;
  sectionName: string;
  examName: string;
  scope: string;
}

export class examResponse {
  indicator: string;
  message: string;
  exams: Array<examPartResp>;
}

export class examResponse1 {
  indicator: string;
  message: string;
  exam: examPartResp;
}

export interface examResponse {
  examResp: examResponse;
}
