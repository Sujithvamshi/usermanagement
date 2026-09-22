import { render, screen } from "@testing-library/react";
import { UserTable } from "../components/UserTable";
import type { User } from "../types/user";

describe("UserTable", () => {
  it("shows empty state when there are no users", () => {
    render(<UserTable users={[]} onEdit={jest.fn()} onDelete={jest.fn()} />);

    expect(screen.getByText(/no users found/i)).toBeInTheDocument();
  });

  it("renders a row per user", () => {
    const users: User[] = [
      {
        id: 1,
        fullName: "Jane Doe",
        email: "jane@example.com",
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
    ];

    render(<UserTable users={users} onEdit={jest.fn()} onDelete={jest.fn()} />);

    expect(screen.getByText("Jane Doe")).toBeInTheDocument();
    expect(screen.getByText("jane@example.com")).toBeInTheDocument();
  });
});
