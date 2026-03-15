# Backend API And Entity Report (Detailed)

Last updated: 2026-03-15

## 1) Security And Authorization Overview

### 1.1 Authentication
- Mechanism: JWT Bearer token
- Public endpoint: `POST /api/auth/login`
- FE sends header for protected APIs:

```http
Authorization: Bearer <accessToken>
```

### 1.2 Effective access
- `ADMIN`: fixed configured admin username
- `PIC`: derived from `Department.departmentPic`
- `PM`: derived from `Project.pms`
- `NONE`: authenticated user without current assignment

### 1.3 URL-level security (`SecurityConfig`)
- `/api/auth/**` -> `permitAll`
- most other `/api/**` endpoints -> authenticated
- fine-grained department/project authorization now happens in service layer

### 1.4 Business-level checks (service layer)
Besides URL-level checks, services validate business ownership:
- Admin access for admin actions
- Department PIC ownership for department/project operations
- PM assignment for project update operations

### 1.5 JWT payload
Token includes:
- `sub`: username
- `role`: first authority, e.g. `ROLE_ADMIN`
- `iat`, `exp`

Token TTL comes from `app.jwt.expiration-ms`.

---

## 2) Error Format And Status Codes

### 2.1 Error response payload

```json
{
  "message": "Error message"
}
```

(`ErrorResponse` record)

### 2.2 Error mapping (`GlobalExceptionHandler`)
- `400 Bad Request` -> `IllegalArgumentException`
- `403 Forbidden` -> `AccessDeniedException`
- `404 Not Found` -> `EntityNotFoundException`
- `401 Unauthorized` -> Spring Security (missing/invalid/expired token)

---

## 3) Full Endpoint List

| # | Method | Path | Auth | URL-level rule | Controller |
|---|---|---|---|---|---|
| 1 | POST | `/api/auth/login` | Public | Permit all | `AuthController` |
| 2 | POST | `/api/auth/logout` | Public | Permit all | `AuthController` |
| 3 | GET | `/api/users` | Bearer | `ADMIN` | `UserController` |
| 4 | GET | `/api/users/me` | Bearer | Authenticated | `UserController` |
| 5 | POST | `/api/admin/departments` | Bearer | Authenticated + service-layer admin check | `AdminController` |
| 6 | PUT | `/api/admin/departments/{deptId}` | Bearer | Authenticated + service-layer admin check | `AdminController` |
| 7 | DELETE | `/api/admin/departments/{deptId}` | Bearer | Authenticated + service-layer admin check | `AdminController` |
| 8 | GET | `/api/departments` | Bearer | Authenticated | `DepartmentController` |
| 9 | POST | `/api/departments/{deptId}/projects` | Bearer | Authenticated + service-layer PIC check | `DepartmentController` |
| 10 | GET | `/api/projects` | Bearer | Authenticated | `ProjectController` |
| 11 | PUT | `/api/projects/{projectId}` | Bearer | Authenticated + service-layer PM/PIC check | `ProjectController` |
| 12 | PUT | `/api/projects/{projectId}/data` | Bearer | Authenticated + service-layer PM/PIC check | `ProjectController` |
| 13 | DELETE | `/api/projects/{projectId}` | Bearer | Authenticated + service-layer PIC check | `ProjectController` |

---

## 4) API Details

### 4.1 `POST /api/auth/login`
- Purpose: Login and issue access token
- Request DTO: `LoginRequest`

```json
{
  "username": "string",
  "password": "string"
}
```

- Response DTO: `LoginResponse` (`200 OK`)

```json
{
  "accessToken": "<jwt>",
  "tokenType": "Bearer"
}
```

### 4.1.1 `POST /api/auth/logout`
- Purpose: Logout acknowledgement endpoint for FE
- Auth: public (same `/api/auth/**` policy)
- Notes:
  - current BE uses stateless JWT
  - this endpoint does not invalidate token on server
  - FE should clear token/client auth state

- Response (`200 OK`):

```json
{
  "message": "Logged out successfully"
}
```

---

### 4.2 `GET /api/users`
- Purpose: Get all users (for admin management screens)
- Auth: `ADMIN`
- Service check: current user role must be `ADMIN`
- Response DTO: `List<UserResponse>` (`200 OK`)

`UserResponse` fields:
- `id`, `username`, `fullname`, `email`

---

### 4.3 `GET /api/users/me`
- Purpose: Get current user profile
- Auth: authenticated
- Service behavior:
  - gets username from security context
  - loads user by username
- Response DTO: `UserMeResponse` (`200 OK`)

`UserMeResponse` fields:
- `id`, `username`, `fullname`, `email`, `accessMode`, `departmentPicPartIds`, `pmProjectIds`

---

### 4.4 `POST /api/admin/departments`
- Purpose: Create department
- Auth: `ADMIN`
- Request DTO: `CreateDepartmentRequest`

```json
{
  "partId": 1001,
  "partName": "Department A",
  "gitPat": "...",
  "ecodePat": "...",
  "gerritUserName": "...",
  "gerritHttpPassword": "...",
  "jiraSecPat": "...",
  "jiraMxPat": "...",
  "jiraLaPat": "...",
  "departmentPicUserId": 12
}
```

- Service checks:
  - only configured admin account
  - `partId` is required
  - department `partId` must be unique
- Response DTO: `DepartmentResponse` (`201 Created`)

---

### 4.5 `PUT /api/admin/departments/{deptId}`
- Purpose: Update department data (partial update)
- Auth: configured admin account
- Request DTO: `UpdateDepartmentRequest`

```json
{
  "partName": "Department A Updated",
  "gitPat": "...",
  "ecodePat": "...",
  "gerritUserName": "...",
  "gerritHttpPassword": "...",
  "jiraSecPat": "...",
  "jiraMxPat": "...",
  "jiraLaPat": "...",
  "departmentPicUserId": 12
}
```

- Service behavior:
  - only non-null fields are updated
  - `departmentPicUserId` updates the assigned PIC in the same request
- Response DTO: `DepartmentResponse` (`200 OK`)

---

### 4.6 `DELETE /api/admin/departments/{deptId}`
- Purpose: Delete a department
- Auth: configured admin account
- Service behavior:
  - verifies department exists
  - deletes all projects under this department (`findAllByDepartmentPartId` + `deleteAll`)
  - deletes department
- Response: `204 No Content`

---

### 4.7 `GET /api/departments`
- Purpose: Get all departments
- Auth: authenticated
- Response DTO: `List<DepartmentResponse>` (`200 OK`)

`DepartmentResponse` fields:
- `partId`, `partName`, secret/config fields, `departmentPicUserId`, `departmentPicUsername`

---

### 4.8 `POST /api/departments/{deptId}/projects`
- Purpose: Create project in department
- Auth: authenticated + service-layer ownership check
- Service check:
  - caller must be PIC of `{deptId}`
- Request DTO: `ProjectDataRequest`

```json
{
  "projectName": "Project X",
  "branch": "main",
  "notes": "Initial setup",
  "taskManagements": ["jira"],
  "repositories": ["repo-a", "repo-b"],
  "pics": ["alice"],
  "devWhiteList": ["dev01", "dev02"],
  "pmUserIds": [25, 26]
}
```

- Response DTO: `ProjectResponse` (`201 Created`)

---

### 4.9 `GET /api/projects`
- Purpose: Get all projects
- Auth: authenticated
- Response DTO: `List<ProjectResponse>` (`200 OK`)

---

### 4.10 `PUT /api/projects/{projectId}`
- Purpose: Update PM-editable project info
- Auth: authenticated + service-layer PM/PIC check
- Request DTO: `ProjectUpdateInfoRequest`

```json
{
  "branch": "release-1.0",
  "repositories": ["repo-a", "repo-c"]
}
```

- Response DTO: `ProjectResponse` (`200 OK`)

---

### 4.11 `PUT /api/projects/{projectId}/data`
- Purpose: PIC or assigned PM updates project business data
- Auth: authenticated + service-layer PM/PIC check
- Request DTO: `ProjectDataRequest`
- Service checks:
  - caller must be PIC of that project's department or assigned PM
  - `projectName`, `branch`, `notes` are updated only when non-null
  - list fields are updated using provided values
  - `pmUserIds` is only applied when caller is the owning PIC
- Response DTO: `ProjectResponse` (`200 OK`)

---

### 4.12 `DELETE /api/projects/{projectId}`
- Purpose: Delete project
- Auth: authenticated + service-layer PIC check
- Service checks:
  - caller must be PIC of that project's department
  - project must exist
- Response: `204 No Content`

---

## 5) DTO Reference

### 5.1 Request DTOs
- `LoginRequest`: `username`, `password`
- `CreateDepartmentRequest`: `partId`, `partName`, `gitPat`, `ecodePat`, `gerritUserName`, `gerritHttpPassword`, `jiraSecPat`, `jiraMxPat`, `jiraLaPat`, `departmentPicUserId`
- `UpdateDepartmentRequest`: same mutable fields as create except no `partId`
- `ProjectDataRequest`: `projectName`, `branch`, `notes`, `taskManagements`, `repositories`, `pics`, `devWhiteList`, `pmUserIds`
- `ProjectUpdateInfoRequest`: `branch`, `repositories`

### 5.2 Response DTOs
- `LoginResponse`: `accessToken`, `tokenType`
- `UserResponse`: `id`, `username`, `fullname`, `email`
- `UserMeResponse`: `id`, `username`, `fullname`, `email`, `accessMode`, `departmentPicPartIds`, `pmProjectIds`
- `DepartmentResponse`: department identity, department config fields, `departmentPicUserId`, `departmentPicUsername`
- `ProjectResponse`: `id`, `departmentId`, `projectName`, `branch`, `notes`, `taskManagements`, `repositories`, `pics`, `devWhiteList`, `pmUserIds`
- `ErrorResponse`: `message`

---

## 6) Full Entity Documentation

### 6.1 `User` (`app_user`)
Fields:
- `id` (`Long`, PK, auto increment)
- `username` (`String`, unique, not null, max 100)
- `password` (`String`, not null)
- `fullname` (`String`, not null, max 150)
- `email` (`String`, unique, not null, max 150)
- `role` (`Role`, enum as string, not null)

Relations:
- Referenced by `Department.departmentPic` (1-1)
- Referenced by `Project.pms` (many-to-many)

### 6.2 `Department` (`department_information`)
Fields:
- `partId` (`Long`, PK)
- `partName` (`String`, not null, max 150)
- `gitPat` (`String`)
- `ecodePat` (`String`)
- `gerritUserName` (`String`)
- `gerritHttpPassword` (`String`)
- `jiraSecPat` (`String`)
- `jiraMxPat` (`String`)
- `jiraLaPat` (`String`)
- `departmentPic` (`User`, nullable, unique)

Relations:
- `@OneToOne` to `User` via `pic_user_id`
- Parent side for `Project.department` (`@ManyToOne` from project)

### 6.3 `Project` (`project_information`)
Fields:
- `id` (`Long`, PK, auto increment)
- `department` (`Department`, not null)
- `projectName` (`String`, not null, max 200)
- `branch` (`String`, max 200)
- `notes` (`String`, max 3000)
- `taskManagements` (`List<String>`, element collection table `project_task_managements`)
- `repositories` (`List<String>`, element collection table `project_repositories`)
- `pics` (`List<String>`, element collection table `project_pics`)
- `devWhiteList` (`List<String>`, element collection table `project_dev_white_list`)
- `pms` (`Set<User>`, join table `project_pm`)

Relations:
- `@ManyToOne` to `Department` (`part_id`)
- `@ManyToMany` to `User` through `project_pm`

### 6.4 `Role` (enum)
Values:
- `ADMIN`
- `DEPT_PIC`
- `PROJECT_PM`
- `DEVELOPER`

### 6.5 `StatisticResult` (`statistic_result`)
Fields:
- `id` (`Long`, PK)
- `departmentId`, `departmentName`, `projectName`, `issueKey`
- `prNumber`
- `createdTime`, `mergedTime`
- `week`, `aiSupport`
- `numberOfCommit`, `numberOfSegments`, `pattern`, `numberOfFile`
- `aiLoc`, `firstAiLoc`, `developerLoc`, `aiContribution`
- `service`, `language`, `taskType`, `devType`, `cycleTimeHour`

Notes:
- Entity exists in model/repository layer.
- No public controller endpoint currently exposes this entity directly.

---

## 7) FE Integration Notes

- Always include `Authorization: Bearer <token>` except login endpoint.
- Use `/api/users/me` right after login to initialize role-based UI state.
- Handle:
  - `401`: token missing/invalid/expired -> clear auth and redirect login
  - `403`: authenticated but not authorized -> show permission error
  - `404`: missing resource
  - `400`: invalid payload/business rule violation

---

## 8) Current Gaps (Not Implemented Yet)

- User management APIs are still missing:
  - `POST /api/users`
  - `PUT /api/users/{id}`
  - `DELETE /api/users/{id}`
- Auth refresh endpoint is not present:
  - `POST /api/auth/refresh`






