import type { User } from "../types/user";

interface UserTableProps {
  users: User[];
  onEdit: (user: User) => void;
  onDelete: (user: User) => void;
}

export function UserTable({ users, onEdit, onDelete }: UserTableProps) {
  if (users.length === 0) {
    return <p className="empty-state">No users found. Add a user to get started.</p>;
  }

  return (
    <table>
      <thead>
        <tr>
          <th>Full name</th>
          <th>Email</th>
          <th>Created</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {users.map((user) => (
          <tr key={user.id}>
            <td>{user.fullName}</td>
            <td>{user.email}</td>
            <td>{new Date(user.createdAt).toLocaleDateString()}</td>
            <td className="actions-cell">
              <button className="btn btn-secondary" onClick={() => onEdit(user)}>
                Edit
              </button>
              <button className="btn btn-danger" onClick={() => onDelete(user)}>
                Delete
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
