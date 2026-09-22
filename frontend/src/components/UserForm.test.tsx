import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { UserForm } from "../components/UserForm";

describe("UserForm", () => {
  it("submits the entered values", async () => {
    const user = userEvent.setup();
    const onSubmit = jest.fn();

    render(<UserForm onSubmit={onSubmit} />);

    await user.type(screen.getByLabelText(/full name/i), "Jane Doe");
    await user.type(screen.getByLabelText(/email/i), "jane@example.com");
    await user.click(screen.getByRole("button", { name: /add user/i }));

    expect(onSubmit).toHaveBeenCalledWith({
      fullName: "Jane Doe",
      email: "jane@example.com"
    });
  });

  it("shows cancel button and save label when editing", () => {
    render(
      <UserForm
        initialValue={{
          id: 1,
          fullName: "Jane Doe",
          email: "jane@example.com",
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        }}
        onSubmit={jest.fn()}
        onCancel={jest.fn()}
      />
    );

    expect(screen.getByRole("button", { name: /save changes/i })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /cancel/i })).toBeInTheDocument();
  });
});
