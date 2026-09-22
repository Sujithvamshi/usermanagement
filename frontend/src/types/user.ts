export interface User {
  id: number;
  fullName: string;
  email: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateUserInput {
  fullName: string;
  email: string;
}

export type UpdateUserInput = CreateUserInput;

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string | null;
}

export interface ApiErrorResponse {
  success: false;
  message: string;
  status: number;
  timestamp: string;
  details: string[];
}
