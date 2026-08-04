# Database Design

## Entity Relationship Diagram

```text
+----------------------+
|        User          |
+----------------------+
| id                   |
| fullName             |
| username             |
| email                |
| password             |
| role                 |
| enabled              |
| createdAt            |
| updatedAt            |
+----------+-----------+
           |
           | 1
           |
           | *
+----------v-----------+
|         Url          |
+----------------------+
| id                   |
| originalUrl          |
| shortCode            |
| customAlias          |
| clickCount           |
| active               |
| expiresAt            |
| createdAt            |
| updatedAt            |
| user_id (FK)         |
+----------------------+
```

---

## Relationship

- One **User** can own multiple **URLs**.
- Each **URL** belongs to exactly one **User**.
- `user_id` is a foreign key referencing the `User` table.

---

## URL Entity

| Field | Description |
|------|-------------|
| id | Primary Key |
| originalUrl | Original destination URL |
| shortCode | Generated short identifier |
| customAlias | Optional custom alias |
| clickCount | Number of redirects |
| active | Active/Inactive status |
| expiresAt | Expiration timestamp |
| createdAt | Creation timestamp |
| updatedAt | Last update timestamp |

---

## User Entity

| Field | Description |
|------|-------------|
| id | Primary Key |
| fullName | User's full name |
| username | Unique username |
| email | Unique email |
| password | Encrypted password |
| role | ROLE_USER / ROLE_ADMIN |
| enabled | Account status |
| createdAt | Creation timestamp |
| updatedAt | Last update timestamp |