# complate-java
*- Rendering of JSX based views in Java*

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.complate/complate-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.complate/complate-core)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Open Issues](https://img.shields.io/github/issues/complate/complate-java.svg)](https://github.com/complate/complate-java/issues)
[![Build Status](https://github.com/complate/complate-java/actions/workflows/main.yml/badge.svg)](https://github.com/complate/complate-java/actions/workflows/main.yml)
[![Code Coverage](https://codecov.io/gh/complate/complate-java/branch/main/graph/badge.svg)](https://codecov.io/gh/complate/complate-java)

[complate](https://complate.org) adapter that can be used in Java projects for
rendering JSX based views.


## Quick Start

Download the jar through Maven:

```xml
<dependency>
  <groupId>org.complate</groupId>
  <artifactId>complate-nashorn</artifactId>
  <version>1.0.0</version>
</dependency>
```

or

```xml
<dependency>
  <groupId>org.complate</groupId>
  <artifactId>complate-graal</artifactId>
  <version>1.0.0</version>
</dependency>
```

If you want to use the latest unstable version `1.0.1-SNAPSHOT` you need to
configure Sonatype's OSS Nexus as snapshot repository:

```xml
<repository>
  <name>Central Portal Snapshots</name>
  <id>central-portal-snapshots</id>
  <url>https://central.sonatype.com/repository/maven-snapshots/</url>
  <releases>
    <enabled>false</enabled>
  </releases>
  <snapshots>
    <enabled>true</enabled>
  </snapshots>
</repository>
```


## Release History

See [CHANGELOG.md](./CHANGELOG.md)

## Frequently Asked Questions

See [FAQ.md](./FAQ.md)

## Code of Conduct

[Contributor Code of Conduct](./CODE_OF_CONDUCT.md). By participating in this
project you agree to abide by its terms.


## License

complate-java is Open Source software released under the
[Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).
