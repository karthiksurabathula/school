export class initTracker {
  id: number;
  identifier: string;
  status: boolean;
}

export class initTrackerResponse {
  indicator: string;
  message: string;
  initTracker: initTracker;
}

export interface initTrackerResponse {
  initTrackerResp: initTrackerResponse;
}
