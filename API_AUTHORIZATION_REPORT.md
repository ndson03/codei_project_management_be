# Bao Cao Phan Quyen Va Danh Sach API (Chi Tiet)

## 1) Tong quan bao mat va phan quyen

### 1.1 Kieu xac thuc
- Co che: JWT Bearer token
- Dang nhap qua endpoint public: `POST /api/auth/login`
- Sau khi login thanh cong, FE gui header:

```http
Authorization: Bearer <accessToken>
```

### 1.2 Cau hinh bao mat
- Security stateless (khong dung session server).
- `csrf` dang tat.
- Tat ca endpoint ngoai `/api/auth/**` deu yeu cau da xac thuc.
- Rule URL-level + business check trong Service.

### 1.3 Danh sach role
- `ADMIN`
- `DEPT_PIC`
- `PROJECT_PM`
- `DEVELOPER`

### 1.4 JWT payload hien tai
Token duoc tao voi:
- `sub`: username
- `role`: authority dau tien (vd: `ROLE_ADMIN`, `ROLE_DEPT_PIC`, `ROLE_PROJECT_PM`)
- `iat`, `exp`

Han token:
- `app.jwt.expiration-ms`
- Mac dinh: `86400000` ms (~24 gio)

---

## 2) Ma loi va format loi

### 2.1 Format response loi tu GlobalExceptionHandler

```json
{
  "message": "Noi dung loi"
}
```

### 2.2 Mapping status chinh
- `400 Bad Request`: `IllegalArgumentException`
- `403 Forbidden`: `AccessDeniedException`
- `404 Not Found`: `EntityNotFoundException`
- `401 Unauthorized`: den tu Spring Security (chua xac thuc/token sai-het han)

---

## 3) Bang tong hop tat ca API hien co

| STT | Method | Path | Auth | Quyen URL-level |
|---|---|---|---|---|
| 1 | POST | `/api/auth/login` | Public | Permit all |
| 2 | POST | `/api/admin/departments` | Bearer | `ADMIN` |
| 3 | PUT | `/api/admin/departments/{deptId}/pic` | Bearer | `ADMIN` |
| 4 | POST | `/api/departments/{deptId}/projects` | Bearer | Dynamic check (DEPT_PIC + ownership) |
| 5 | PUT | `/api/projects/{projectId}/pm` | Bearer | `DEPT_PIC` |
| 6 | PUT | `/api/projects/{projectId}` | Bearer | Dynamic check (PROJECT_PM + ownership) |
| 7 | GET | `/api/departments` | Bearer | Authenticated |
| 8 | GET | `/api/projects` | Bearer | Authenticated |
| 9 | GET | `/api/users` | Bearer | `ADMIN` |
| 10 | GET | `/api/users/me` | Bearer | Authenticated |

Luu y:
- Ngoai URL-level, Service van check them role va quan he so huu/de duoc gan.

---

## 4) Chi tiet tung API

## 4.1 Login

### `POST /api/auth/login`
- Muc dich: Dang nhap, lay access token.
- Auth: Khong can token.

Request body:

```json
{
  "username": "string",
  "password": "string"
}
```

Response `200 OK`:

```json
{
  "accessToken": "<jwt>",
  "tokenType": "Bearer"
}
```

Loi thuong gap:
- `401` neu username/password sai.

---

## 4.2 Tao department (Admin)

### `POST /api/admin/departments`
- Muc dich: Tao phong ban.
- Auth: Bearer token.
- URL-level role: `ADMIN`.
- Service check:
  - User hien tai phai la `ADMIN`.
  - `partId` khong duoc null.
  - `partId` khong duoc trung.

Request body:

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
  "jiraLaPat": "..."
}
```

Response `201 Created`:

```json
{
  "partId": 1001,
  "partName": "Department A",
  "departmentPicUserId": null
}
```

Loi:
- `400`: `partId` null hoac da ton tai.
- `403`: khong phai `ADMIN`.

---

## 4.3 Gan DEPT PIC cho department (Admin)

### `PUT /api/admin/departments/{deptId}/pic`
- Muc dich: Gan user lam PIC cua phong ban.
- Auth: Bearer token.
- URL-level role: `ADMIN`.
- Service check:
  - User thuc hien phai la `ADMIN`.
  - `userId` phai ton tai.
  - User duoc gan phai co role `DEPT_PIC`.
  - Department phai ton tai.

Path param:
- `deptId` (Long)

Request body:

```json
{
  "userId": 12
}
```

Response `200 OK`:

```json
{
  "partId": 1001,
  "partName": "Department A",
  "departmentPicUserId": 12
}
```

Loi:
- `400`: user khong co role `DEPT_PIC`.
- `403`: khong phai `ADMIN`.
- `404`: khong tim thay user hoac department.

---

## 4.4 Tao project trong department (DEPT PIC)

### `POST /api/departments/{deptId}/projects`
- Muc dich: Tao project thuoc department.
- Auth: Bearer token.
- URL-level dynamic check (`ApiAuthorizationService`):
  - Co authority `ROLE_DEPT_PIC`.
  - Username phai la PIC cua `deptId`.
- Service check them:
  - Role user hien tai la `DEPT_PIC`.
  - User hien tai la PIC cua department.
  - Department ton tai.

Path param:
- `deptId` (Long)

Request body:

```json
{
  "projectName": "Project X",
  "branch": "main",
  "notes": "Mo ta",
  "taskManagements": ["jira"],
  "repositories": ["repo-a", "repo-b"],
  "pics": ["alice"],
  "devWhiteList": ["dev01", "dev02"]
}
```

Response `201 Created`:

```json
{
  "id": 101,
  "departmentId": 1001,
  "projectName": "Project X",
  "branch": "main",
  "notes": "Mo ta",
  "taskManagements": ["jira"],
  "repositories": ["repo-a", "repo-b"],
  "pics": ["alice"],
  "devWhiteList": ["dev01", "dev02"],
  "pmUserIds": []
}
```

Loi:
- `403`: khong du role hoac khong phai PIC cua department.
- `404`: department khong ton tai.

---

## 4.5 Gan PM cho project (DEPT PIC)

### `PUT /api/projects/{projectId}/pm`
- Muc dich: Gan user lam PM cua project.
- Auth: Bearer token.
- URL-level role: `DEPT_PIC`.
- Service check:
  - User thuc hien phai la `DEPT_PIC`.
  - User thuc hien phai la PIC cua department chua project do.
  - Project ton tai.
  - User duoc gan ton tai va role phai la `PROJECT_PM`.

Path param:
- `projectId` (Long)

Request body:

```json
{
  "userId": 25
}
```

Response `200 OK`:

```json
{
  "id": 101,
  "departmentId": 1001,
  "projectName": "Project X",
  "branch": "main",
  "notes": "Mo ta",
  "taskManagements": ["jira"],
  "repositories": ["repo-a", "repo-b"],
  "pics": ["alice"],
  "devWhiteList": ["dev01", "dev02"],
  "pmUserIds": [25]
}
```

Loi:
- `400`: user duoc gan khong phai `PROJECT_PM`.
- `403`: khong du quyen hoac khong phai PIC cua department.
- `404`: project hoac user khong ton tai.

---

## 4.6 PM cap nhat thong tin project

### `PUT /api/projects/{projectId}`
- Muc dich: PM cap nhat `branch`, `repositories`.
- Auth: Bearer token.
- URL-level dynamic check (`ApiAuthorizationService`):
  - Co authority `ROLE_PROJECT_PM`.
  - Username phai nam trong PM list cua project.
- Service check them:
  - Role user hien tai phai la `PROJECT_PM`.
  - User hien tai phai la PM da duoc assign cua project.
  - Project ton tai.

Path param:
- `projectId` (Long)

Request body:

```json
{
  "branch": "release-1.0",
  "repositories": ["repo-a", "repo-c"]
}
```

Response `200 OK`:

```json
{
  "id": 101,
  "departmentId": 1001,
  "projectName": "Project X",
  "branch": "release-1.0",
  "notes": "Mo ta",
  "taskManagements": ["jira"],
  "repositories": ["repo-a", "repo-c"],
  "pics": ["alice"],
  "devWhiteList": ["dev01", "dev02"],
  "pmUserIds": [25]
}
```

Loi:
- `403`: khong phai PM hoac PM khong duoc assign vao project.
- `404`: project khong ton tai.

---

## 4.7 Lay thong tin user dang dang nhap

### `GET /api/users/me`
- Muc dich: Lay profile user hien tai cho FE.
- Auth: Bearer token.
- Quyen: Chi can da xac thuc.
- Service check:
  - Phai co authentication hop le trong security context.
  - Username trong token phai tim thay duoc user trong DB.

Response `200 OK`:

```json
{
  "id": 25,
  "username": "pm01",
  "fullname": "Project Manager",
  "email": "pm01@example.com",
  "role": "PROJECT_PM"
}
```

Loi:
- `401`: chua dang nhap/token khong hop le.
- `404`: user trong token khong con ton tai trong DB.

---

## 4.8 Lay danh sach department

### `GET /api/departments`
- Muc dich: Lay toan bo danh sach department.
- Auth: Bearer token.
- Quyen: Chi can da xac thuc.

Response `200 OK`:

```json
[
  {
    "partId": 1001,
    "partName": "Department A",
    "departmentPicUserId": 12
  }
]
```

Loi:
- `401`: chua dang nhap/token khong hop le.

---

## 4.9 Lay danh sach project

### `GET /api/projects`
- Muc dich: Lay toan bo danh sach project.
- Auth: Bearer token.
- Quyen: Chi can da xac thuc.

Response `200 OK`:

```json
[
  {
    "id": 101,
    "departmentId": 1001,
    "projectName": "Project X",
    "branch": "main",
    "notes": "Mo ta",
    "taskManagements": ["jira"],
    "repositories": ["repo-a"],
    "pics": ["alice"],
    "devWhiteList": ["dev01"],
    "pmUserIds": [25]
  }
]
```

Loi:
- `401`: chua dang nhap/token khong hop le.

---

## 4.10 Lay danh sach user (Admin)

### `GET /api/users`
- Muc dich: Lay toan bo danh sach user de phuc vu quan tri/gan quyen.
- Auth: Bearer token.
- URL-level role: `ADMIN`.
- Service check:
  - User hien tai phai co role `ADMIN`.

Response `200 OK`:

```json
[
  {
    "id": 1,
    "username": "admin",
    "fullname": "System Admin",
    "email": "admin@example.com",
    "role": "ADMIN"
  }
]
```

Loi:
- `401`: chua dang nhap/token khong hop le.
- `403`: khong phai `ADMIN`.

---

## 5) Quy tac FE can ap dung

- Sau login, luu `accessToken` va luon gui `Authorization` cho API protected.
- Neu `401`: xoa token, dieu huong ve man login.
- Neu `403`: hien thi thong bao khong du quyen.
- Co the dung `/api/users/me` de khoi tao state user-role thay vi decode token thu cong.

---

## 6) Cac API chua co (hien tai)

He thong hien chua co cac endpoint sau:
- `POST /api/users` (tao user)
- `PUT /api/users/{id}` (cap nhat user)
- `POST /api/auth/refresh` (refresh token)
- `POST /api/auth/logout` (logout phia server)

Neu can, co the bo sung tiep de hoan thien luong quan tri nguoi dung.


