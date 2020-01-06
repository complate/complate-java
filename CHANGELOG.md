# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


## [Unreleased]


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


[Unreleased]: https://github.com/complate/complate-java/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/complate/complate-java/compare/502b0d95d0acf1453ba895ae8930c2140e8c5644...v0.1.0

[mvitz]: https://github.com/mvitz
