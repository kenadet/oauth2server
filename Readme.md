# Ouath2 Server

Oauth2 server component which generates jwt after user signup
and login.



## Setup Instruction:

1. Install postgres database using either docker or real installation
2. Do mvn clean package
3. Do  mvn spring-boot:run
4. Do for example:

```bash
  curl -X POST \
   http://localhost:8083/signup \
   -H 'accept: application/json' \
   -H 'cache-control: no-cache' \
   -H 'content-type: application/json' \
   -d '{
   "username" : "tom1234",
   "password": "ddd123&&",
   "email" : "tom1234e@gmail.com"
   }'
```
to register a user

5. Do for example:

```bash
curl -X POST \
   http://localhost:8083/login \
   -H 'accept: application/json' \
   -H 'cache-control: no-cache' \
   -H 'content-type: application/json' \
   -d '{
   "email" : "tom1234e@gmail.com",
   "password": "ddd123&&"
   }'
```
to get access token for login user

  