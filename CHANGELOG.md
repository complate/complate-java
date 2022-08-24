# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


## [Unreleased]

### 📣 Notes
- `NashornComplateRenderer` has been updated to use the open source
  [`org.openjdk.nashorn`](https://github.com/szegedi/nashorn) branch of Nashorn
  since Nashorn has been removed from newer JDK versions

### 🔨 Dependency Upgrades
- Upgrade to Graal 21.2.0

### ❤️ Contributors

We'd like to thank all the contributors who worked on this release!

- [@mvitz][mvitz]


## [0.2.0] - 2020-07-07

This release contains a new renderer that uses the Graal JavaScript engine.

### 📣 Notes
- Although we recommend using the new `GraalComplateRenderer` if you plan on
  using the `NashornComplateRenderer` you should add an exclusion to
  `org.graalvm.js:js`.
- `NashornComplateRenderer` is deprecated and will be removed in the near
  future. Please migrate to the new `GraalComplateRenderer` ([592d7e3](https://github.com/complate/complate-java/commit/592d7e342dc9e5b4a85e61c2604675ddd40f2e93)).
- `GraalComplateRenderer` is not a drop-in replacement for `NashornComplateRenderer` -
  see the
  [FAQ](./FAQ.md#can-i-replace-nashorncomplaterenderer-with-graalcomplaterenderer).

### ⭐️ New Features
- New `GraalComplateRenderer` ([3d7ea5d](https://github.com/complate/complate-java/commit/3d7ea5d737e93ebaf8d877161e78ec8c66680c4b)).

### ❤️ Contributors

We'd like to thank all the contributors who worked on this release!

- [@larsrh][larsrh]
- [@mvitz][mvitz]


## [0.1.0] - 2020-01-06

This release contains of the initial code extraction from
[complate-spring-mvc](https://github.com/complate/complate-spring-mvc).

It contains a few breaking changes that you will need to change when upgrading:

1. The parameter order of the JavaScript render method changed to
   `stream, view, params`.
2. The `ViewResolver` changed from `com.github.complate.ComplateViewResolver` to
   `org.complate.spring.mvc.ComplateViewResolver`.
3. The `com.github.complate.ScriptingEngine` changed to
   `org.complate.core.ComplateRenderer`.
4. The `com.github.complate.NashornScriptingBridge` changed to
   `org.complate.nashorn.NashornComplateRenderer`.

If you rely on the previous behaviour that your JavaScript file is read on every
request you further need to wrap `org.complate.nashorn.NashornComplateRenderer`
within a `org.complate.core.renderer.ComplateReEvaluatingRenderer`. This may be
slower than before (because the `ScriptingEngine` is then recreated on every
request) but for production usage without wrapping the performance should be
increased.

### 📣 Notes
- Multiple breaking changes as described above.

### ⭐️ New Features
- JavaScript file can be read only once which enhances performance.

### 🔨 Dependency Upgrades
- Upgrade to Servlet 4.0.1

### ❤️ Contributors

We'd like to thank all the contributors who worked on this release!

- [@mvitz][mvitz]


[Unreleased]: https://github.com/complate/complate-java/compare/v0.2.0...HEAD
[0.2.0]: https://github.com/complate/complate-java/compare/v0.1.0...v0.2.0
[0.1.0]: https://github.com/complate/complate-java/compare/502b0d95d0acf1453ba895ae8930c2140e8c5644...v0.1.0

[larsrh]: https://github.com/larsrh
[mvitz]: https://github.com/mvitz
