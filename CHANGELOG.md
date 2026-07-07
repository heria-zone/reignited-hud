# Changelog

All notable changes to Reignited HUD will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/)
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

### Added
- Minecraft 1.21.1 support — Fabric, Forge, NeoForge
- NeoForge loader support (first NeoForge release)

### Fixed
- Off-hand item not counting all inventory items correctly

---

## [1.1.0] - 2024

### Added
- Health Bar — colour-coded: Red → Yellow → Dark Yellow → Gray; adapts to hunger and wither effects
- Hunger Bar — changes appearance based on active status effects
- Experience Bar — current level and progress toward next level
- Breathing Bar — displays breath level when underwater
- Mount Information — name, health, and jump force when riding a mount
- Equipment Slot Display — all four armour slots visible at a glance
- Item in Hand Display — current held item with stack count
- Player Skin Display — renders player skin on HUD
- Player Username Display
- Clock — Minecraft time, real-time based
- Status Effects Display — effect icons without opening inventory
- Configuration via Mod Menu (Fabric) and Configured (Forge)
- Minecraft 1.16.5, 1.18.2, 1.19.2, 1.19.4, 1.20.1, 1.20.4 support
- Fabric and Forge loader support

### Known Issues
- Off-hand item count does not account for all inventory items

---

## [1.0.0] - 2024

Initial release.

### Added
- Core HUD framework based on the original [Ignite HUD](https://www.curseforge.com/minecraft/mc-mods/ignitehud) by Deadzoke
- Basic health, hunger, and experience display

[Unreleased]: https://github.com/heria-zone/reignited-hud/compare/v1.1.0...HEAD
[1.1.0]: https://github.com/heria-zone/reignited-hud/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/heria-zone/reignited-hud/releases/tag/v1.0.0
