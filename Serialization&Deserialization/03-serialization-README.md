# Module 03 — Serialization & Deserialization

> **The core question:** A JavaScript app and a Rust server have completely different data types. How does data sent by one make sense to the other?

Every time a client sends a request or a server sends a response, data crosses a boundary — between languages, runtimes, and machines. Serialization and deserialization is the mechanism that makes this possible. It is not a library or a framework feature. It is a fundamental concept that happens in every client-server interaction you will ever build.

---

## Table of Contents

- [The Problem It Solves](#the-problem-it-solves)
- [What Serialization & Deserialization Mean](#what-serialization--deserialization-mean)
- [The Mental Model](#the-mental-model)
- [Serialization Standards](#serialization-standards)
  - [Text-Based Formats](#text-based-formats)
  - [Binary Formats](#binary-formats)
- [JSON — The Standard We Use](#json--the-standard-we-use)
  - [JSON Syntax Rules](#json-syntax-rules)
  - [Supported Data Types](#supported-data-types)
- [The Full Flow — Client to Server and Back](#the-full-flow--client-to-server-and-back)
- [What You Don't Need to Worry About](#what-you-dont-need-to-worry-about)
- [Demos in This Module](#demos-in-this-module)
- [Getting Started](#getting-started)

---

## The Problem It Solves

Consider this common scenario:

```
Client                            Server
──────                            ──────
JavaScript (dynamic, no types)    Rust (compiled, strict types)
React app running in a browser    Web server running in the cloud
```

The client wants to send this data:

```javascript
// JavaScript object — only JavaScript understands this natively
const user = {
  name: "Jane Doe",
  age: 28,
  active: true
}
```

But the Rust server expects this:

```rust
// Rust struct — only Rust understands this natively
struct User {
    name: String,
    age: u32,
    active: bool,
}
```

These two data representations are incompatible. They cannot be sent directly over a network. The machine on the other end has no idea how to interpret raw JavaScript objects or Rust structs — they are language-specific constructs that cease to exist the moment they leave their runtime.

**The solution:** agree on a common format that both sides know how to read and write — regardless of what language or platform they run on.

---

## What Serialization & Deserialization Mean

**Serialization** is the process of converting data from a language-specific format into a common, transmittable format.

**Deserialization** is the reverse — converting data from that common format back into a language-specific format.

```
JavaScript Object  ──serialize──>  JSON (common format)  ──network──>  Rust Struct
Rust Struct        ──serialize──>  JSON (common format)  ──network──>  JavaScript Object
```

In plain terms:

| Term | What it means |
|---|---|
| **Serialization** | "Pack the data into the agreed format before sending" |
| **Deserialization** | "Unpack the received data from the agreed format into my own types" |

Both the client and the server must agree on the same format. This agreement is the serialization standard.

---

## The Mental Model

You do not need to understand every layer of the network to use serialization correctly. Here is the mental model that matters as a backend engineer:

```
CLIENT SIDE
──────────────────────────────────────────────
JavaScript Object
        │
        ▼  serialize (convert to JSON)
        │
   JSON string  ← This is your responsibility
        │
        ▼  (network handles everything below this line)
   Transport Layer → IP Packets → Physical bits → ...


SERVER SIDE
──────────────────────────────────────────────
   ... → Physical bits → IP Packets → Transport Layer
        │
        ▼  (network hands you this)
   JSON string  ← This is your responsibility
        │
        ▼  deserialize (convert to Rust struct / Java class / Python dict)
        │
Language-specific data type
```

The network handles the conversion of your JSON into bits for transmission and back into JSON on arrival. **Your job as a backend engineer begins and ends at the JSON layer.** Everything in between is the network stack's concern.

---

## Serialization Standards

There is no single serialization format. Different use cases call for different standards. They fall into two categories:

### Text-Based Formats

Human-readable. Easy to debug. Larger in size.

| Format | Common Use Case |
|---|---|
| **JSON** | REST API communication between clients and servers — the focus of this module |
| **XML** | Legacy enterprise systems, SOAP APIs, some configuration formats |
| **YAML** | Configuration files (Docker Compose, Kubernetes, CI/CD pipelines) |

### Binary Formats

Not human-readable. Compact and fast. Requires a schema.

| Format | Common Use Case |
|---|---|
| **Protocol Buffers (protobuf)** | gRPC communication, microservices, high-performance systems |
| **MessagePack** | Compact alternative to JSON for internal service communication |
| **Avro** | Apache Kafka, big data pipelines |

**Why we focus on JSON:**
The same principle applies to serialization standards as it does to every other backend concept — learn the one used in the majority of codebases first. JSON is the default for REST API communication and is present in roughly 80% of client-server interactions on the web today. Everything else can be learned in context once the foundation is solid.

---

## JSON — The Standard We Use

JSON stands for **JavaScript Object Notation**. Despite the name, it is not tied to JavaScript — it is a language-agnostic text format used everywhere: REST APIs, configuration files, log files, database storage, and more.

Its greatest strength is **human readability**. You can open any JSON payload and immediately understand the data it carries.

### JSON Syntax Rules

These are the rules both the client and server must follow:

```json
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "available": true,
  "tags": ["engineering", "best-practices"],
  "publisher": {
    "name": "Prentice Hall",
    "country": "USA"
  }
}
```

| Rule | Detail |
|---|---|
| Starts and ends with `{ }` | Every JSON object is wrapped in curly braces |
| Keys must be strings | Keys are always in **double quotes** — `"id"`, not `id` |
| Key-value pairs separated by `:` | `"key": value` |
| Multiple pairs separated by `,` | Trailing commas are **not allowed** |
| Values follow their own type rules | See the table below |

### Supported Data Types

| Type | Example | Notes |
|---|---|---|
| String | `"Jane Doe"` | Always in double quotes |
| Number | `42` or `3.14` | No quotes — integers and decimals both valid |
| Boolean | `true` or `false` | Lowercase, no quotes |
| Null | `null` | Represents absence of a value |
| Array | `["a", "b", "c"]` | Ordered list — values can be any JSON type |
| Object | `{ "key": "value" }` | Nested JSON — same rules apply recursively |

**What JSON does not support:**

- Functions / methods
- `undefined` (JavaScript-specific — use `null` instead)
- Comments (unlike YAML or most config formats)
- Single-quoted strings (double quotes only)

---

## The Full Flow — Client to Server and Back

Here is what actually happens during a typical REST API call, from a serialization perspective:

### Step 1 — Client Serializes and Sends

```
React App (JavaScript)
──────────────────────
User fills in a form → JavaScript object is created in memory:
  { title: "Clean Code", author: "Robert Martin" }

JSON.stringify() converts it to a JSON string:
  '{"title":"Clean Code","author":"Robert Martin"}'

HTTP request is sent:
  POST /api/books
  Content-Type: application/json

  {"title":"Clean Code","author":"Robert Martin"}
                ↑
         This is serialized JSON — a plain string
         travelling over the network
```

### Step 2 — Server Deserializes and Processes

```
Spring Boot Server (Java)
─────────────────────────
JSON string arrives at the server:
  '{"title":"Clean Code","author":"Robert Martin"}'

Jackson (Spring's JSON library) deserializes it into a Java object:
  Book { title = "Clean Code", author = "Robert Martin" }

Business logic runs:
  - Validate the data
  - Save to database
  - Prepare a response
```

### Step 3 — Server Serializes and Responds

```
Java object is serialized back to JSON:
  { "id": 42, "title": "Clean Code", "author": "Robert Martin", "createdAt": "..." }

HTTP response is sent:
  201 Created
  Content-Type: application/json

  {"id":42,"title":"Clean Code","author":"Robert Martin"}
```

### Step 4 — Client Deserializes and Renders

```
React App receives the JSON string.

JSON.parse() or fetch's .json() deserializes it back into a JavaScript object:
  { id: 42, title: "Clean Code", author: "Robert Martin" }

React renders the new book in the UI.
```

**The complete picture:**

```
JavaScript Object
      │  serialize (JSON.stringify)
      ▼
JSON String ──── network ────> JSON String
                                      │  deserialize (Jackson / Gson)
                                      ▼
                               Java Object (Book.java)
                                      │  business logic + DB
                                      ▼
                               Java Object (response)
                                      │  serialize (Jackson / Gson)
                                      ▼
JSON String <──── network ──── JSON String
      │  deserialize (fetch.json / JSON.parse)
      ▼
JavaScript Object → React renders UI
```

---

## What You Don't Need to Worry About

This is worth stating clearly, because the OSI model and network layers can create confusion about where your responsibility starts and ends.

When your JSON string leaves the application layer, the network stack converts it through several intermediate formats:

```
JSON string
    → Transport layer bytes
    → IP packets
    → Physical bits (electrical signals / optical pulses)
    → ... transmitted ...
    → Physical bits
    → IP packets
    → Transport layer bytes
    → JSON string  ← handed back to your application
```

As a backend engineer, **you never interact with any layer below the application layer.** The network stack handles all of this transparently. You write JSON. You receive JSON. That is the full extent of your serialization concern in day-to-day backend work.

---

## Demos in This Module

| # | Demo | Concept |
|---|---|---|
| 01 | [JSON Basics](./01-json-basics/) | JSON structure, data types, and syntax rules |
| 02 | [Serializing a Request](./02-serialize-request/) | Client sends a JSON body — server deserializes and echoes it back |
| 03 | [Deserializing a Response](./03-deserialize-response/) | Server serializes a Java/Python object into JSON — client reads it |
| 04 | [Nested JSON](./04-nested-json/) | Handling nested objects and arrays in request/response bodies |
| 05 | [Invalid JSON](./05-invalid-json/) | What happens when JSON is malformed — 400 errors and debugging |

---

## Getting Started

### Prerequisites

- Node.js v18+ (or your preferred runtime)
- curl or Postman for firing requests

### Run a demo

```bash
git clone [repository-url]
cd [repository-name]/03-serialization

cd 01-json-basics
node server.js

# Send a JSON body and watch the server deserialize it
curl -X POST http://localhost:3000/echo \
  -H "Content-Type: application/json" \
  -d '{"name": "Jane", "age": 28, "active": true}'

# Try sending invalid JSON — observe the 400 error
curl -X POST http://localhost:3000/echo \
  -H "Content-Type: application/json" \
  -d '{name: Jane}'
```

Each demo includes a **"Break It On Purpose"** section — remove the `Content-Type` header, send malformed JSON, or omit required fields to see exactly how the server responds and why.

---

