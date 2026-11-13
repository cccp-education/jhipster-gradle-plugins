#!/usr/bin/env bash
# GBL-013 US-5 (wave 2) — CI contract for jhipster-gradle-plugins.
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

job="${1:-}"
case "$job" in
  fast|test)
    ./gradlew :persistence:ktlintCheck :assistant:ktlintCheck --no-daemon --console=plain
    ;;
  *)
    printf 'unsupported job for jhipster-gradle-plugins: %s\n' "${job:-<none>}" >&2
    exit 2
    ;;
esac
