# OTP-Sign-In-via-Email
This project is a Spring Boot application that provides user authentication using an OTP (One-Time Password) sent via email. It includes features for user registration, login, OTP generation, validation, and a simple dashboard for user information.

# **Technologies Used**
- Spring Boot
- SMTP Mail Sender
- HTML/CSS/JavaScript

# API Endpoints
### LoginController
#### GET ```/login```
- Returns the login page.

#### GET ```/otp?email={email}```
- Returns the OTP entry page for the given email.

#### GET ```/dashboard?email={email}```
- Returns the dashboard page with user information.

### OtpController
#### POST ```/api/otp/generate```
- Generates and sends an OTP to the specified email.

**Request Body:**
```
{
  "email": "user@example.com"
}
```
**Response:**
- 201 Created if OTP is successfully generated.
- 404 Not Found if the user is not found.

<img width="1046" alt="image" src="https://github.com/user-attachments/assets/ebb560fb-4d33-4ac7-b3d6-1da1cbc9c668">

#### POST ```/api/otp/resend```
- Resends the OTP to the specified email if the resend limit is not reached.

**Request Body:**
```
{
  "email": "user@example.com"
}
```
**Response:**
- 200 OK if OTP is successfully resent.
- 400 Bad Request if resend limit is reached.

<img width="1036" alt="image" src="https://github.com/user-attachments/assets/67738a34-c3d3-4276-9a29-2aa874071af4">


#### POST ```/api/otp/validate```
- Validates the provided OTP for the specified email.

**Request Body:**
```
{
  "email": "user@example.com",
  "otpCode": "123456"
}
```
**Response:**
- 200 OK if OTP is valid.
- 404 Not Found if OTP is invalid or expired.
- 500 Internal Server Error if an error occurs during validation.

<img width="1041" alt="image" src="https://github.com/user-attachments/assets/10247872-cdb5-40d5-8eb9-e7ac7cb12a2d">


### UserController
#### POST /api/user/create
- Creates a new user.

**Request Body:**
```
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "bio": "Software Developer",
  "linkedInUrl": "https://www.linkedin.com/in/johndoe",
  "githubUrl": "https://github.com/johndoe",
  "twitterUrl": "https://twitter.com/johndoe"
}
```

**Response:**
- 201 Created if user is successfully created.
- 400 Bad Request if the email is already in use or if any required field is missing.

<img width="1046" alt="image" src="https://github.com/user-attachments/assets/72faa99a-0481-4bc8-a3b1-aa296e151c83">

#### POST /api/user/login
- Authenticates a user and redirects to the OTP entry page.

**Request Body:**
```
{
  "email": "john@example.com",
  "password": "password123"
}
```
**Response:**
- 200 OK if authentication is successful.
- 401 Unauthorized if the password is incorrect.
- 404 Not Found if the user does not exist.

<img width="1038" alt="image" src="https://github.com/user-attachments/assets/2ab539e2-8082-4138-8cc9-bf5ffa5ea71e">

## Screenshots

#### Create User Page
<img width="1512" alt="image" src="https://github.com/user-attachments/assets/97d01fa0-b511-42f4-988f-2c2cdac37be3">

#### Login Page
<img width="1512" alt="image" src="https://github.com/user-attachments/assets/1a9533f5-1684-43e8-8ec6-3bb24dc3dd6b">

#### Forgot Password Page
<img width="1512" alt="image" src="https://github.com/user-attachments/assets/4ee31f09-beac-4fd6-82e9-816424f8297c">

#### Reset Password Page
<img width="1512" alt="image" src="https://github.com/user-attachments/assets/eb2a5604-f9e1-4ab8-b659-8634e55eec60">

#### OTP Verification Page
<img width="1512" alt="image" src="https://github.com/user-attachments/assets/0f6b062b-081e-4683-99bd-45033be38a46">

#### Dashboard Page
<img width="1512" alt="image" src="https://github.com/user-attachments/assets/fdb1e2aa-e031-42fd-a842-0bdb90b04931">



<br><br>

Credits <br>
This project was created by **Yash Mehta 🚀**
