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

## Cau hinh IAM ket noi Keycloak
IAM service call truc tiep den Keycloak qua cac bien moi truong:

```yaml
keycloak:
  base-url: ${KEYCLOAK_BASE_URL:http://localhost:8080}
  realm: ${KEYCLOAK_REALM:master}
  client-id: ${KEYCLOAK_CLIENT_ID:iam-service}
  client-secret: ${KEYCLOAK_CLIENT_SECRET:}
  admin-realm: ${KEYCLOAK_ADMIN_REALM:master}
  admin-client-id: ${KEYCLOAK_ADMIN_CLIENT_ID:admin-cli}
  admin-username: ${KEYCLOAK_ADMIN_USERNAME:admin}
  admin-password: ${KEYCLOAK_ADMIN_PASSWORD:admin}
```

Luu y:
- FE khong can gui `clientId/clientSecret` nua cho login/refresh/introspect.
- IAM su dung `keycloak.client-id` va `keycloak.client-secret` de call token/introspect endpoint.
- `client-secret` de trong khi dung public client; bat buoc set khi dung confidential client.

## Authorization Code Flow (OIDC)
Ap dung cho web app (SPA/BFF) hoac server-side app dang nhap qua Keycloak.

```
+-------------+       +----------+       +-----------+       +----------+
| Browser/App | ----> | Gateway  | ----> | Keycloak  | ----> | IAM APIs  |
+-------------+       +----------+       +-----------+       +----------+
       |                   |                   |
       | 1. /authorize     |                   |
       |------------------>|(redirect)         |
       |<--------------------------------------|
       | 2. login + consent                    |
       |-------------------------------------->|
       | 3. redirect ve redirect_uri?code=...  |
       |<--------------------------------------|
       | 4. doi code lay token (/token)        |
       |------------------>|------------------>|
       |                   |<------------------|
       | 5. access token   |                   |
       |----Bearer token-->|----forward------->|
```

### Trinh tu chi tiet
1) Client redirect user toi endpoint authorize cua Keycloak voi:
   - `response_type=code`
   - `client_id`
   - `redirect_uri`
   - `scope=openid profile email`
   - `state`, `nonce`
   - `code_challenge`, `code_challenge_method=S256` (khuyen nghi bat buoc cho public client/SPA)
2) User login thanh cong tren Keycloak (va consent neu co).
3) Keycloak redirect ve `redirect_uri` kem `code` + `state`.
4) Backend (hoac BFF) goi endpoint token de doi `code` lay:
   - `access_token`
   - `id_token`
   - `refresh_token` (neu bat scope/offline_access tuong ung)
5) Client dung `access_token` goi Gateway.
6) Gateway verify JWT/introspection, authorize theo role/permission roi forward vao IAM/internal services.

### OIDC endpoints (realm)
- Authorize: `/realms/{realm}/protocol/openid-connect/auth`
- Token: `/realms/{realm}/protocol/openid-connect/token`
- Logout: `/realms/{realm}/protocol/openid-connect/logout`
- UserInfo: `/realms/{realm}/protocol/openid-connect/userinfo`
- JWKS: `/realms/{realm}/protocol/openid-connect/certs`

### Cau hinh khuyen nghi
- Bat `Authorization Code` cho client tren Keycloak.
- Cau hinh dung `Valid Redirect URIs` va `Web Origins` (khong dung `*` trong production).
- Bat PKCE (`S256`) cho public client.
- Luu token an toan (uu tien HTTP-only cookie voi BFF; han che luu refresh token tren browser).
- Dinh nghia role/claim ro rang de Gateway va service authorize nhat quan.

## Cac Grant Type (OIDC/OAuth2)

### 1) Authorization Code (khuyen nghi)
- Dung cho web app/server-side app.
- User dang nhap qua Keycloak, app nhan `code`, sau do doi `code` lay token.
- Bao mat cao, ho tro session SSO, co the ket hop PKCE.

```
+-------------+      +-----------+      +-----------+
| Browser/App | ---> | Keycloak  | ---> | IAM/GW    |
+-------------+      +-----------+      +-----------+
      |                   |
      | /auth (login)     |
      |------------------>|
      |<-- redirect ?code |
      |                   |
      | /token (exchange) |
      |------------------>|
      |<-- access/id/refresh
      |---- Bearer token ----------->|
```

### 2) Authorization Code + PKCE (khuyen nghi bat buoc cho SPA/mobile)
- Giong Authorization Code, nhung them `code_verifier`/`code_challenge`.
- Giam nguy co bi danh cap authorization code.
- Dung cho public client (khong giu duoc client secret).

```
+------------+      +-----------+
| SPA/Mobile | ---> | Keycloak  |
+------------+      +-----------+
      | /auth + code_challenge
      |------------------------>|
      |<-- redirect ?code
      | /token + code_verifier
      |------------------------>|
      |<-- access/id/refresh
```

### 3) Client Credentials (may-to-may)
- Khong co user context; token dai dien cho service account.
- Dung cho service A goi service B qua Gateway.

```
+-----------+      +-----------+      +-----------+
| Service A | ---> | Keycloak  | ---> | Service B |
+-----------+      +-----------+      +-----------+
      | /token (grant_type=client_credentials)
      |--------------------------------------->|
      |<-- access_token
      |------------- Bearer token ------------>|
```

### 4) Refresh Token
- Dung de xin `access_token` moi khi access token het han.
- Giup giam tan suat user phai login lai.

```
+-----------+      +-----------+
| App/BFF   | ---> | Keycloak  |
+-----------+      +-----------+
      | /token (grant_type=refresh_token)
      |------------------------------->|
      |<-- new access_token (+/- refresh_token moi)
```

### 5) Resource Owner Password Credentials (Password Grant) - khong khuyen nghi
- App gui truc tiep username/password len auth server.
- Mat an toan do app cam duoc credential nguoi dung.
- OAuth 2.1 da loai bo; chi dung cho he thong legacy co kiem soat chat.

```
+-----------+      +-----------+
| Legacy App| ---> | Keycloak  |
+-----------+      +-----------+
      | /token (grant_type=password, username, password)
      |----------------------------------------------->|
      |<-- access/id/refresh
```

### 6) Device Authorization Grant (cho TV/IoT, neu can)
- Thiet bi khong nhap duoc password de dang.
- User dang nhap tren thiet bi khac bang `user_code`, thiet bi poll token.

```
+-----------+      +-----------+      +--------------+
| TV/Device | ---> | Keycloak  | <--- | User Browser |
+-----------+      +-----------+      +--------------+
      | device_authorization: lay device_code + user_code
      |----------------------->|
      |<-----------------------|
      | (hien thi user_code cho user)
      |                        | login + nhap user_code
      |                        |---------------------->|
      | poll /token voi device_code
      |----------------------->|
      |<-- access_token (sau khi user authorize)
```

### Lua chon grant type theo bai toan
- User dang nhap tren web app: `Authorization Code` (co PKCE neu la public client).
- SPA/mobile: `Authorization Code + PKCE`.
- Service-to-service: `Client Credentials`.
- Duy tri phien dang nhap: `Refresh Token`.
- Legacy bat kha khang: `Password Grant` (han che toi da, co lo trinh bo).
- TV/IoT: `Device Authorization Grant`.

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
