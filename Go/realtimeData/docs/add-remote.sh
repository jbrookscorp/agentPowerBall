#!/usr/bin/env bash
set -euo pipefail

# Usage:
#   bash docs/add-remote.sh https://github.com/jbrookscorp/realtimeData
# or set REMOTE_URL env var:
#   REMOTE_URL=https://github.com/jbrookscorp/realtimeData bash docs/add-remote.sh

REMOTE_URL="${1:-${REMOTE_URL:-}}"
if [[ -z "${REMOTE_URL}" ]]; then
  echo "ERROR: REMOTE_URL not provided. Pass as first arg or set REMOTE_URL env var." >&2
  exit 1
fi

# Ensure git is available
if ! command -v git >/dev/null 2>&1; then
  echo "ERROR: git is not installed or not in PATH." >&2
  exit 1
fi

# Initialize repo if needed
if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  echo "Initializing git repository..."
  git init
fi

# Ensure default branch is main
# This also works before the first commit
git branch -M main || true

# Create initial commit if none exists yet
if ! git rev-parse HEAD >/dev/null 2>&1; then
  echo "Creating initial commit..."
  git add .
  git commit -m "Initial commit: Go realtime data API"
fi

# Add or update remote origin
if git remote get-url origin >/dev/null 2>&1; then
  echo "Updating existing 'origin' remote URL to ${REMOTE_URL}..."
  git remote set-url origin "${REMOTE_URL}"
else
  echo "Adding 'origin' remote: ${REMOTE_URL}"
  git remote add origin "${REMOTE_URL}"
fi

# Push and set upstream
echo "Pushing to remote..."
# Use --set-upstream to establish tracking if not yet set
if git rev-parse --abbrev-ref --symbolic-full-name @{u} >/dev/null 2>&1; then
  git push
else
  git push -u origin main
fi

echo "Done. Remote set to: $(git remote get-url origin)"
