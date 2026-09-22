import { render, screen, waitFor } from "@testing-library/react";
import { App } from "./App";
import { userApi } from "./api/userApi";

jest.mock("./api/userApi", () => ({
  userApi: {
    list: jest.fn(),
    create: jest.fn(),
    update: jest.fn(),
    remove: jest.fn()
  },
  ApiError: class ApiError extends Error {}
}));

const mockedUserApi = userApi as jest.Mocked<typeof userApi>;

describe("App", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it("renders users returned from the API", async () => {
    mockedUserApi.list.mockResolvedValue([
      {
        id: 1,
        fullName: "Jane Doe",
        email: "jane@example.com",
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
    ]);

    render(<App />);

    await waitFor(() => expect(screen.getByText("Jane Doe")).toBeInTheDocument());
  });

  it("shows empty state when no users exist", async () => {
    mockedUserApi.list.mockResolvedValue([]);

    render(<App />);

    await waitFor(() => expect(screen.getByText(/no users found/i)).toBeInTheDocument());
  });
});
