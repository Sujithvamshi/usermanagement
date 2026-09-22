import type { ReactNode } from "react";
import "../index.css";

interface DashboardLayoutProps {
  children: ReactNode;
}

export function DashboardLayout({ children }: DashboardLayoutProps) {
  return (
    <div className="app-shell">
      <aside className="sidebar">
        <h1>User Management</h1>
        <nav>
          <a href="#" className="active">
            Users
          </a>
        </nav>
      </aside>
      <main className="main-content">{children}</main>
    </div>
  );
}
