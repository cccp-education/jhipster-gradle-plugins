#!/usr/bin/env bash
# GBL-013 US-5 (wave 2) — CI contract for jhipster-gradle-plugins.
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

job="${1:-}"
# GBL-014 — publication channel (jitpack default until the Sonatype reset).
channel="${CHANNEL:-jitpack}"
case "$job" in
  fast|test)
    ./gradlew :persistence:ktlintCheck :assistant:ktlintCheck \
      -Pchannel="$channel" --no-daemon --console=plain
    ;;
  *)
    printf 'unsupported job for jhipster-gradle-plugins: %s\n' "${job:-<none>}" >&2
    exit 2
    ;;
esac
