# IAM System Architecture

## Overview
Client gui request vao Gateway. Gateway xac thuc/uy quyen voi Keycloak (OIDC) truoc khi cho phep truy cap vao cac service noi bo.

```
+--------+        +---------+        +-----------------+
| Client | -----> | Gateway | -----> | Internal Services|
+--------+        +---------+        | (hrm, iam, audit,|
     |                |              |  file, noti, ...) |
     |                v              +-----------------+
     |         +-------------+
     |         |  Keycloak   |
     |         | (Auth/OIDC) |
     |         +-------------+
     |                ^
     |                |
     +----------------+
        Token issuance/validation
```

## Flow
1) Client dang nhap/lay token tu Keycloak.
2) Client gui request kem Bearer token toi Gateway.
3) Gateway validate token voi Keycloak (hoac verify JWT).
4) Neu hop le, Gateway forward request vao service noi bo.
5) Service xu ly va tra ket qua ve Gateway -> Client.

## Components
- Keycloak: Quan ly nguoi dung, role, client, token (OIDC).
- Gateway: Cua ngo vao he thong, kiem tra authen/authorize.
- Internal Services: Hrm/Iam/Audit/... chi nhan request da xac thuc.

## Keycloak Architecture
```
               +----------------------+
               |      Keycloak        |
               |  (Auth Server/OIDC)  |
               +----------+-----------+
                          |
                          | JDBC
                          v
               +----------------------+
               |   Keycloak DB (PG)   |
               |   database: iam      |
               +----------------------+

  Admin Console / Account Console / OIDC Endpoints
```

Keycloak luu tru user/role/client/session trong Postgres (DB `iam`). Cac client (Gateway, service, hoac SPA) goi OIDC endpoint cua Keycloak de login/refresh/validate token.
