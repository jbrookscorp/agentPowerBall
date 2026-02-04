Realtime Data API (Go + Alpha Vantage)

A small, modular REST API that proxies select Alpha Vantage endpoints with clean feature boundaries and unit tests.

Endpoints
- GET /api/v1/health
- GET /api/v1/quote?symbol=SYMBOL
- GET /api/v1/intraday?symbol=SYMBOL&interval=1min|5min|15min|30min|60min[&outputsize=compact|full]
- GET /api/v1/search?keywords=TEXT

Prerequisites
- Go 1.22+
- Alpha Vantage API key: https://www.alphavantage.co/support/#api-key

Configuration (Environment Variables)
- ALPHAVANTAGE_API_KEY (required) – Your Alpha Vantage API key.
- PORT (optional, default 8080) – HTTP server port.
- ALPHAVANTAGE_BASE_URL (optional, default https://www.alphavantage.co) – Override for testing/mocking.

Run the Server
1) Set your API key (Linux/macOS):
   export ALPHAVANTAGE_API_KEY=your_key_here

   On Windows (PowerShell):
   $env:ALPHAVANTAGE_API_KEY = "your_key_here"

2) Start the server:
   go run ./cmd/server

   You should see a log like: server starting on :8080

3) Test with curl (quick sanity checks):
   Health:
     curl -s http://localhost:8080/api/v1/health | jq

   Global Quote:
     curl -s "http://localhost:8080/api/v1/quote?symbol=IBM" | jq

   Intraday (15min interval):
     curl -s "http://localhost:8080/api/v1/intraday?symbol=IBM&interval=15min&outputsize=compact" | jq

   Search symbols:
     curl -s "http://localhost:8080/api/v1/search?keywords=ibm" | jq

Using Insomnia
Option A: Import the included Insomnia collection (recommended)
- File > Import > From File
- Select: docs/insomnia_collection.json
- Optional: Create an Environment in Insomnia with a variable base_url set to http://localhost:8080
- Requests in the collection:
  - Health:    {{ base_url }}/api/v1/health
  - Quote:     {{ base_url }}/api/v1/quote?symbol=IBM
  - Intraday:  {{ base_url }}/api/v1/intraday?symbol=IBM&interval=15min&outputsize=compact
  - Search:    {{ base_url }}/api/v1/search?keywords=ibm

Option B: Create requests manually
- Health
  - Method: GET
  - URL: http://localhost:8080/api/v1/health

- Quote
  - Method: GET
  - URL: http://localhost:8080/api/v1/quote
  - Query Params: symbol=IBM

- Intraday
  - Method: GET
  - URL: http://localhost:8080/api/v1/intraday
  - Query Params: symbol=IBM, interval=15min, outputsize=compact

- Search
  - Method: GET
  - URL: http://localhost:8080/api/v1/search
  - Query Params: keywords=ibm

Run Tests
- go test ./...

Notes & Troubleshooting
- Alpha Vantage has free-tier rate limits. If you see responses with fields like "Note" or "Error Message", it usually indicates throttling or an invalid parameter/key. Retrying after a minute often helps for rate limits.
- Ensure ALPHAVANTAGE_API_KEY is set in the environment of the process running the server.
- Default port is 8080; set PORT to run on a different port (e.g., export PORT=9090).
- If you want to point the app to a mock server in tests, set ALPHAVANTAGE_BASE_URL to your mock URL.

Project Structure
- cmd/server: entrypoint
- internal/config: env config loader
- internal/httpserver: router wiring
- internal/alphavantage: HTTP client to Alpha Vantage (with error handling)
- internal/features/{health,quote,intraday,search}: feature modules (handlers + services)

License
- For PoC/demo purposes.
Realtime Data API (Go + Alpha Vantage)

A small, modular REST API that proxies select Alpha Vantage endpoints with clean feature boundaries and unit tests.

Endpoints
- GET /api/v1/health
- GET /api/v1/quote?symbol=SYMBOL
- GET /api/v1/intraday?symbol=SYMBOL&interval=1min|5min|15min|30min|60min[&outputsize=compact|full]
- GET /api/v1/search?keywords=TEXT

Prerequisites
- Go 1.22+
- Alpha Vantage API key: https://www.alphavantage.co/support/#api-key

Configuration (Environment Variables)
- ALPHAVANTAGE_API_KEY (required) – Your Alpha Vantage API key.
- PORT (optional, default 8080) – HTTP server port.
- ALPHAVANTAGE_BASE_URL (optional, default https://www.alphavantage.co) – Override for testing/mocking.

Linux: Setting your Alpha Vantage API key
- Temporary (only for the current shell/session):
  - export ALPHAVANTAGE_API_KEY=your_key_here
  - Run the app in the same terminal after exporting.
- Persist for Bash (Ubuntu/Debian/most distros using Bash):
  - echo 'export ALPHAVANTAGE_API_KEY=your_key_here' >> ~/.bashrc
  - source ~/.bashrc  # or open a new terminal
- Persist for Zsh:
  - echo 'export ALPHAVANTAGE_API_KEY=your_key_here' >> ~/.zshrc
  - source ~/.zshrc
- Fish shell:
  - set -Ux ALPHAVANTAGE_API_KEY your_key_here
- Verify it’s set:
  - printenv ALPHAVANTAGE_API_KEY  # should output your_key_here
- Optional: systemd service (run as a background service)
  - Create a unit file at ~/.config/systemd/user/realtimedata.service with:
    [Unit]
    Description=Realtime Data API

    [Service]
    Environment=ALPHAVANTAGE_API_KEY=your_key_here
    WorkingDirectory=%h/Development/Sandbox/PoC/Go/realtimeData
    ExecStart=/usr/bin/env bash -lc 'go run ./cmd/server'
    Restart=on-failure

    [Install]
    WantedBy=default.target
  - Then run:
    systemctl --user daemon-reload
    systemctl --user enable --now realtimedata.service

Security notes
- Treat your API key like a password; don’t commit it to git.
- Prefer using shell rc files, OS keyrings, or environment managers (e.g., direnv) over hardcoding in code.

Run the Server
1) Set your API key (Linux/macOS):
   export ALPHAVANTAGE_API_KEY=your_key_here

   On Windows (PowerShell):
   $env:ALPHAVANTAGE_API_KEY = "your_key_here"

2) Start the server:
   go run ./cmd/server

   You should see a log like: server starting on :8080

3) Test with curl (quick sanity checks):
   Health:
     curl -s http://localhost:8080/api/v1/health | jq

   Global Quote:
     curl -s "http://localhost:8080/api/v1/quote?symbol=IBM" | jq

   Intraday (15min interval):
     curl -s "http://localhost:8080/api/v1/intraday?symbol=IBM&interval=15min&outputsize=compact" | jq

   Search symbols:
     curl -s "http://localhost:8080/api/v1/search?keywords=ibm" | jq

Using Insomnia
Option A: Import the included Insomnia collection (recommended)
- File > Import > From File
- Select: docs/insomnia_collection.json
- Optional: Create an Environment in Insomnia with a variable base_url set to http://localhost:8080
- Requests in the collection:
  - Health:    {{ base_url }}/api/v1/health
  - Quote:     {{ base_url }}/api/v1/quote?symbol=IBM
  - Intraday:  {{ base_url }}/api/v1/intraday?symbol=IBM&interval=15min&outputsize=compact
  - Search:    {{ base_url }}/api/v1/search?keywords=ibm

Option B: Create requests manually
- Health
  - Method: GET
  - URL: http://localhost:8080/api/v1/health

- Quote
  - Method: GET
  - URL: http://localhost:8080/api/v1/quote
  - Query Params: symbol=IBM

- Intraday
  - Method: GET
  - URL: http://localhost:8080/api/v1/intraday
  - Query Params: symbol=IBM, interval=15min, outputsize=compact

- Search
  - Method: GET
  - URL: http://localhost:8080/api/v1/search
  - Query Params: keywords=ibm

Run Tests
- go test ./...

Notes & Troubleshooting
- Alpha Vantage has free-tier rate limits. If you see responses with fields like "Note" or "Error Message", it usually indicates throttling or an invalid parameter/key. Retrying after a minute often helps for rate limits.
- Ensure ALPHAVANTAGE_API_KEY is set in the environment of the process running the server.
- Default port is 8080; set PORT to run on a different port (e.g., export PORT=9090).
- If you want to point the app to a mock server in tests, set ALPHAVANTAGE_BASE_URL to your mock URL.

Project Structure
- cmd/server: entrypoint
- internal/config: env config loader
- internal/httpserver: router wiring
- internal/alphavantage: HTTP client to Alpha Vantage (with error handling)
- internal/features/{health,quote,intraday,search}: feature modules (handlers + services)

License
- For PoC/demo purposes.



---

Publish to GitHub

Follow these steps to put this project on GitHub.

1) Create a new empty GitHub repository
- Go to https://github.com/new
- Repository name: realtimeData (or any name you prefer)
- Keep it empty: do NOT initialize with README, .gitignore, or license (you already have these here)
- Click Create repository

2) Initialize git locally (if not already a repo)
- cd /home/josh/Development/Sandbox/PoC/Go/realtimeData
- git init
- git add .
- git commit -m "Initial commit: Go realtime data API"

3) Point to the GitHub remote and push
- git branch -M main
- git remote add origin git@github.com:YOUR_GH_USERNAME/YOUR_REPO_NAME.git
  - Alternatively (HTTPS):
    - git remote add origin https://github.com/YOUR_GH_USERNAME/YOUR_REPO_NAME.git
- git push -u origin main

4) Verify GitHub Actions CI
- This repo includes .github/workflows/ci.yml which builds and runs `go test ./...` on each push and PR with Go 1.22.
- After pushing, open the GitHub repo page > Actions tab to see the workflow run.

5) Optional: protect main branch
- In GitHub repo settings, enable branch protection rules to require the CI workflow to pass before merging PRs.

6) Optional: add a status badge to the top of this README
- Replace YOUR_GH_USERNAME and YOUR_REPO_NAME below, then paste near the top of README:
  - ![CI](https://github.com/YOUR_GH_USERNAME/YOUR_REPO_NAME/actions/workflows/ci.yml/badge.svg)

Notes
- Do not commit your ALPHAVANTAGE_API_KEY; keep secrets in your local environment or GitHub repository secrets. CI does not require the API key because tests use mocked HTTP servers.
- If you later add integration tests that hit Alpha Vantage, store the key as a GitHub Actions repository secret (e.g., ALPHAVANTAGE_API_KEY) and reference it in workflow env.



FAQ

Q: What credentials did you use?
A: No credentials are embedded in this repository or hard-coded in the application. The only credential the app needs is an Alpha Vantage API key, which you provide via the ALPHAVANTAGE_API_KEY environment variable at runtime. The server reads this environment variable and includes the key when it calls Alpha Vantage on your behalf.

Important details:
- Local development: Export ALPHAVANTAGE_API_KEY in your shell before running the server. See the Linux/Windows instructions above.
- Insomnia/clients: The Insomnia collection does not contain any secrets. You do not need to (and should not) send your API key from the client; the backend attaches it when calling Alpha Vantage.
- CI (GitHub Actions): Tests use mocked HTTP servers, so no real API key is required in CI. Do not store secrets in the repo. If you later add integration tests, keep the key in GitHub Secrets and reference it in the workflow.
- Configuration: You can override the Alpha Vantage base URL (for mocks) with ALPHAVANTAGE_BASE_URL. The server port is configured via PORT (default 8080).
- Security: Treat your API key like a password. Keep it in your local environment or a secure secret manager; never commit it to git.


Clarification: Git repository and credentials

- No GitHub repository is bundled or pre-configured with this project.
  - The repo you have locally is just files on disk. Until you run git init and add a remote, there is no connection to GitHub (or any remote).
- No credentials or tokens are stored in this repository.
  - The application uses your Alpha Vantage API key from the environment at runtime (ALPHAVANTAGE_API_KEY). It is not committed anywhere.
  - GitHub credentials (SSH keys or HTTPS tokens) are handled by your local git/OS keychain and are not kept in this codebase.

How to verify whether a remote is set
- In the project directory, run: git remote -v
- If it prints nothing, there is no remote configured yet.

How to add a GitHub remote (quick recap)
- SSH (recommended):
  - git init # if not already
  - git add . && git commit -m "Initial commit"
  - git branch -M main
  - git remote add origin git@github.com:YOUR_GH_USERNAME/YOUR_REPO_NAME.git
  - git push -u origin main
- HTTPS (alternative):
  - git remote add origin https://github.com/YOUR_GH_USERNAME/YOUR_REPO_NAME.git
  - git push -u origin main

Notes on credentials for pushing to GitHub
- SSH: Ensure you have an SSH key added to your GitHub account. Docs: https://docs.github.com/en/authentication/connecting-to-github-with-ssh
- HTTPS: You will be prompted for credentials; use a GitHub personal access token (classic) as the password when 2FA is enabled. Docs: https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token

This project intentionally does not contain any embedded credentials or pre-linked repository to keep your secrets safe and allow you to connect it to your own GitHub account.


Quick add to your GitHub remote (jbrookscorp/realtimeData)

If you want to use HTTPS with your provided repository URL, run one of the following from the project root:

Option A: Helper script
- bash docs/add-remote.sh https://github.com/jbrookscorp/realtimeData

Option B: Manual commands
- git init
- git add .
- git commit -m "Initial commit: Go realtime data API"
- git branch -M main
- git remote add origin https://github.com/jbrookscorp/realtimeData
- git push -u origin main

Notes:
- For HTTPS, when prompted for a password, use a GitHub Personal Access Token (if you have 2FA enabled).
- To use SSH instead, create an SSH remote like: git@github.com:jbrookscorp/realtimeData.git
