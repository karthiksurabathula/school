export class examNotes {
  id: number;
  note: string;
}

export class examNotesResponse {
  indicator: string;
  message: string;
  examNotes: Array<examNotes>;
}

export class examNotesResponse1 {
  indicator: string;
  message: string;
  examNotes: examNotes;
}

export interface examNotesResponse {
  examNoteResp: examNotesResponse;
}
