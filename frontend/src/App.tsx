import { useEffect, useState } from "react";
import { ApiError, userApi } from "./api/userApi";
import { DashboardLayout } from "./components/DashboardLayout";
import { UserForm } from "./components/UserForm";
import { UserTable } from "./components/UserTable";
import type { CreateUserInput, User } from "./types/user";

export function App() {
  const [users, setUsers] = useState<User[]>([]);
  const [editingUser, setEditingUser] = useState<User | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  async function loadUsers() {
    try {
      setLoading(true);
      const data = await userApi.list();
      setUsers(data);
      setError(null);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Failed to load users");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadUsers();
  }, []);

  async function handleCreateOrUpdate(input: CreateUserInput) {
    try {
      if (editingUser) {
        await userApi.update(editingUser.id, input);
        setEditingUser(null);
      } else {
        await userApi.create(input);
      }
      setError(null);
      await loadUsers();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Failed to save user");
    }
  }

  async function handleDelete(user: User) {
    try {
      await userApi.remove(user.id);
      setError(null);
      await loadUsers();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Failed to delete user");
    }
  }

  return (
    <DashboardLayout>
      <div className="page-header">
        <h2>Users</h2>
      </div>

      {error && (
        <div className="error-banner" role="alert">
          {error}
        </div>
      )}

      <div className="card">
        <UserForm
          initialValue={editingUser}
          onSubmit={handleCreateOrUpdate}
          onCancel={editingUser ? () => setEditingUser(null) : undefined}
        />
      </div>

      <div className="card">
        {loading ? (
          <p>Loading users...</p>
        ) : (
          <UserTable users={users} onEdit={setEditingUser} onDelete={handleDelete} />
        )}
      </div>
    </DashboardLayout>
  );
}
