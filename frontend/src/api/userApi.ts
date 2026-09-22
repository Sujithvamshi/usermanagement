import type {
  ApiErrorResponse,
  ApiResponse,
  CreateUserInput,
  UpdateUserInput,
  User
} from "../types/user";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api";

export class ApiError extends Error {
  status: number;
  details: string[];

  constructor(message: string, status: number, details: string[] = []) {
    super(message);
    this.status = status;
    this.details = details;
  }
}

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: { "Content-Type": "application/json" },
    ...options
  });

  const body = await response.json().catch(() => null);

  if (!response.ok) {
    const errorBody = body as ApiErrorResponse | null;
    throw new ApiError(
      errorBody?.message ?? "Request failed",
      response.status,
      errorBody?.details ?? []
    );
  }

  return (body as ApiResponse<T>).data;
}

export const userApi = {
  list: () => request<User[]>("/users"),
  get: (id: number) => request<User>(`/users/${id}`),
  create: (input: CreateUserInput) =>
    request<User>("/users", { method: "POST", body: JSON.stringify(input) }),
  update: (id: number, input: UpdateUserInput) =>
    request<User>(`/users/${id}`, { method: "PUT", body: JSON.stringify(input) }),
  remove: (id: number) => request<void>(`/users/${id}`, { method: "DELETE" })
};
