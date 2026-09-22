import { useState } from "react";
import type { FormEvent } from "react";
import type { CreateUserInput, User } from "../types/user";

interface UserFormProps {
  initialValue?: User | null;
  onSubmit: (input: CreateUserInput) => Promise<void> | void;
  onCancel?: () => void;
}

export function UserForm({ initialValue, onSubmit, onCancel }: UserFormProps) {
  const [fullName, setFullName] = useState(initialValue?.fullName ?? "");
  const [email, setEmail] = useState(initialValue?.email ?? "");
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setSubmitting(true);
    try {
      await onSubmit({ fullName, email });
      if (!initialValue) {
        setFullName("");
        setEmail("");
      }
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} aria-label={initialValue ? "Edit user" : "Create user"}>
      <div className="form-grid">
        <label className="form-field">
          Full name
          <input
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
            required
            aria-label="Full name"
          />
        </label>
        <label className="form-field">
          Email
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            aria-label="Email"
          />
        </label>
      </div>
      <div className="form-actions">
        <button type="submit" className="btn btn-primary" disabled={submitting}>
          {initialValue ? "Save changes" : "Add user"}
        </button>
        {onCancel && (
          <button type="button" className="btn btn-secondary" onClick={onCancel}>
            Cancel
          </button>
        )}
      </div>
    </form>
  );
}
