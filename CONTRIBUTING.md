# Contributing to Reignited HUD

Thank you for considering a contribution. This covers everything you need before you start.

---

## Your Credit, Your Copyright

**The copyright in your contribution stays with you.** Reignited HUD is MIT licensed — you grant the project permission to use your work, but you retain ownership.

Meaningful contributions are credited in [`CONTRIBUTORS.md`](CONTRIBUTORS.md) with your name, handle, and a description of what you built. Smaller contributions (bug fixes, tweaks) are credited through git commit authorship, which GitHub tracks automatically.

---

## Setting Up

```bash
git clone https://github.com/heria-zone/reignited-hud
```

**Requirements:**
- JDK 21
- Gradle (wrapper included)

**Build:**
```bash
./gradlew :fabric:build
./gradlew :forge:build
./gradlew :neoforge:build
```

---

## What You Can Contribute

**New HUD elements** — a new piece of information to display on screen. Open an issue first to discuss placement, default visibility, and config options before implementing.

**Config options** — new toggles, scale options, position overrides. Keep the config structure consistent with existing options.

**Loader ports** — if you are porting to a new Minecraft version or loader, open an issue first. Version ports require testing across all supported mod loaders.

**Bug fixes** — go straight to a PR. No prior discussion needed.

**Visual improvements** — changes to how existing elements render. Include before/after screenshots in your PR.

---

## Code Standards

Keep HUD elements modular — each element should be independently toggleable. Configuration keys follow `snake_case`. All new config options must have a sensible default that matches vanilla Minecraft behaviour.

---

## Commit Messages

```
ADD:   New HUD element or feature
FIX:   Bug fix
REF:   Refactor without behaviour change
REM:   Removal
DOCS:  Documentation only
CFG:   Build or config change
NULL:  Trivial formatting
```

---

## Opening a Pull Request

- One logical change per PR
- All active loaders must build without warnings
- Update `CHANGELOG.md` under `[Unreleased]`
- Fill in the PR template checklist
- For visual changes, include screenshots

---

## Reporting a Security Issue

See [`SECURITY.md`](SECURITY.md). Do not open a public GitHub issue for security vulnerabilities.

---

## Questions

[Heria Zone Discord](https://discord.gg/ZmCPM22FCK) — `#dev` channel.
