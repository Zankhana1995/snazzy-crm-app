# Snazzy CRM

Welcome to Snazzy CRM, the snazziest CRM for all your CRM needs!

# Note
Here problem-backend-java has the original code to change, while backend-java has the implemented solution. 
The challenge is explained in this file.

## Installation

### Requirements
1. Docker installed
2. An active internet connection

There are two containers, one for the java test and one for the php test. Depending on which one you want to do,
run the following:

```bash
docker compose --profile java up  
# or
docker compose --profile php up
# or bring up both
docker compose --profile "*" up
```

## Backend Test

The backend test can be written in java or php. The java application is built using Spring and the php 
application is built using Symfony. There is a test suite for each application that can be run for either 
using this single script:
```bash
./test.sh java # or php
```

Both applications are built to be worked on while running in their docker container. The java application
will even hot reload after a code change to help you move as quickly as possible. Of course, feel free to
work on this application in any way you prefer.

The java application can be found at http://localhost:8080 and the php application at http://localhost:9090. 
Each supports the following api endpoints:

`GET /account`

returns:
```json
[
  {
    "id": 1,
    "name": "Good Burger",
    "status": "NEW",
    "attempts": 0
  },
  {
    "id": 2,
    "name": "Acme Corp",
    "status": "NEW",
    "attempts": 0
  },
  {
    "id": 3,
    "name": "Wonka Candy Company",
    "status": "NEW",
    "attempts": 0
  },
  {
    "id": 4,
    "name": "Great Glass Elevator Corp",
    "status": "NEW",
    "attempts": 0
  }
]
```

`POST /account`

payload:
```json
{
  "name": "Good Burger",
  "status": "NEW",
  "attempts": 0
}
```

returns:
```json
{
  "id": 1,
  "name": "Good Burger",
  "status": "NEW",
  "attempts": 0
}
```

### The Challenges

Your goal is to complete the following four challenges. Each one has an accompanying test that should pass 
when complete. At the end, **we also like to get your thoughts on how we could further iterate on this new 
functionality with a series of question we'd ask to fill out in** `./CANDIDATE_THOUGHTS.md`.

#### Challenge 1

The Account entity has a one-to-many relationship with the Contact entity. Add a relationship to these entities
so we now get the following:

`GET /account`

returns:
```json
[
  {
    "id": 1,
    "name": "Good Burger",
    "status": "NEW",
    "attempts": 0,
    "contacts": [
      {
        "id": 2,
        "firstName": "Salim",
        "lastName": "Traher",
        "phoneNumber": "456-103-4512",
        "primary": true
      },
      {
        "id": 3,
        "firstName": "Ewan",
        "lastName": "Maleck",
        "phoneNumber": "388-868-5602",
        "primary": false
      }
    ]
  },
  {
    "id": 2,
    "name": "Acme Corp",
    "status": "NEW",
    "attempts": 0,
    "contacts": [
      {
        "id": 7,
        "firstName": "Saccount_idoney",
        "lastName": "Marconi",
        "phoneNumber": "137-718-5089",
        "primary": true
      },
      {
        "id": 8,
        "firstName": "Hedda",
        "lastName": "Frie",
        "phoneNumber": "181-482-8234",
        "primary": false
      }
    ]
  },
  {
    "id": 3,
    "name": "Wonka Candy Company",
    "status": "NEW",
    "attempts": 0,
    "contacts": [
      {
        "id": 12,
        "firstName": "Freddi",
        "lastName": "Weippert",
        "phoneNumber": "588-683-6350",
        "primary": true
      },
      {
        "id": 13,
        "firstName": "Burr",
        "lastName": "Margrett",
        "phoneNumber": "993-823-2388",
        "primary": false
      }
    ]
  },
  {
    "id": 4,
    "name": "Great Glass Elevator Corp",
    "status": "NEW",
    "attempts": 0,
    "contacts": []
  }
]
```

#### Challenge 2:

We would like to allow the user to search accounts and return results that match all or part of a contact's 
phone number. Alter the method `search()` in the account repository, so we now get the following:

`GET /account?query=388`

returns:
```json
[
  {
    "id": 1,
    "name": "Good Burger",
    "status": "NEW",
    "attempts": 0,
    "contacts": [
      {
        "id": 2,
        "firstName": "Salim",
        "lastName": "Traher",
        "phoneNumber": "456-103-4512",
        "primary": true
      },
      {
        "id": 3,
        "firstName": "Ewan",
        "lastName": "Maleck",
        "phoneNumber": "388-868-5602",
        "primary": false
      }
    ]
  },
  {
    "id": 3,
    "name": "Wonka Candy Company",
    "status": "NEW",
    "attempts": 0,
    "contacts": [
      {
        "id": 12,
        "firstName": "Freddi",
        "lastName": "Weippert",
        "phoneNumber": "588-683-6350",
        "primary": true
      },
      {
        "id": 13,
        "firstName": "Burr",
        "lastName": "Margrett",
        "phoneNumber": "993-823-2388",
        "primary": false
      }
    ]
  }
]
```

#### Challenge 3:

We would now like to make accounts filterable for ones that are not assigned contacts. Please ensure the
following now works:

`GET /account?unassigned=true`

returns:
```json
[
  {
    "id": 4,
    "name": "Great Glass Elevator Corp",
    "status": "NEW",
    "attempts": 0,
    "contacts": []
  }
]
```

`GET /account?unassigned=true&query=388`

returns:
```json
[]
```

#### Challenge 4:

We want to ensure that when creating a new account that it is only possible to set the `status` field to one of two valid
entries: `NEW` or `SOLD`. Additionally, if an account is set to `SOLD` it must have its field `attempt` set to `1` or 
greater. If either of these conditions are not met the endpoint should return a `422` status code. 

`POST /account`

payload:
```json
{
  "name": "Good Burger",
  "status": "IN_PROGRESS",
  "attempts": 0
}
```

returns 
```
422
```

`POST /account`

payload:
```json
{
  "name": "Good Burger",
  "status": "SOLD",
  "attempts": 0
}
```

returns
```
422
```

### Submitting your code

When you are done, please commit your changes, and perform one of the following:
1. If you have received the challenge via Google Drive feel free to push your changes directly to the drive or compress your changes to `.zip` file and push that directly to the drive.
2. If you have received the challenge via email please compact your codebase in a `.zip` file to include your name or initials,
and reply to that email with that as an attachment.
