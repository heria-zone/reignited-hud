# Security Policy — Reignited HUD

## Reporting a Vulnerability

**Do not open a public GitHub issue for security vulnerabilities.**

If you find something, tell me privately and I'll fix it. That's all this is.

### Primary channel — GitHub Private Vulnerability Reporting

Go to the [Security tab](../../security) of this repository and click **"Report a vulnerability"**. GitHub creates a private draft between you and the maintainer. No public exposure, no email required.

### Secondary channel — Discord

Send a direct message to **MSymbios** on the [Heria Zone Discord](https://discord.gg/ZmCPM22FCK).

---

## What Counts as a Security Issue

Reignited HUD is a client-side HUD mod. The realistic threat surface is narrow:

- **Crash exploits** — malformed config files or crafted inputs that crash the client
- **Rendering exploits** — inputs that cause unexpected rendering behaviour affecting other players in multiplayer
- **Config injection** — crafted config values that execute unexpected code paths

## What Does NOT Belong Here

- Visual bugs or incorrect HUD values → open a [GitHub Issue](../../issues)
- Feature requests → open a [GitHub Issue](../../issues)
- Questions → join [Discord](https://discord.gg/ZmCPM22FCK) `#dev`

---

## What to Include in a Report

- Description of the vulnerability and its impact
- Steps to reproduce
- Mod version, Minecraft version, loader (Fabric / Forge / NeoForge)
- Singleplayer or multiplayer
- Relevant crash logs or screenshots

---

## What to Expect

- **Acknowledgement** — within 48 hours
- **Initial assessment** — within 1 week
- **Credit** — if you want to be credited for the find, say so in your report

This is a solo project. I will always respond, but I am one person.

---

## Supported Versions

| Version | Supported |
|---|---|
| Latest release | ✅ |
| Older releases | ⚠️ Critical fixes only |
