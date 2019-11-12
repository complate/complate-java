# complate-java
*- Rendering of JSX based views in Java*

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.complate/complate-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.complate/complate-core) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0) [![Open Issues](https://img.shields.io/github/issues/complate/complate-java.svg)](https://github.com/complate/complate-java/issues) [![Build Status](https://travis-ci.org/complate/complate-java.svg?branch=master)](https://travis-ci.org/complate/complate-java) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=org.complate%3Acomplate-core&metric=alert_status)](https://sonarcloud.io/dashboard?id=org.complate%3Acomplate-core) [![Code Coverage](https://codecov.io/gh/complate/complate-java/branch/master/graph/badge.svg)](https://codecov.io/gh/complate/complate-java)

[complate](https://complate.org) adapter that can be used in Java projects for
rendering JSX based views.


## Quick Start

Because there is no stable version in maven central right now you need to
configure Sonatype's OSS Nexus as snapshot repository.

```xml
<repository>
  <id>ossrh</id>
  <name>Sonatype OSS Snapshot Repository</name>
  <url>https://oss.sonatype.org/content/repositories/snapshots</url>
</repository>
```

Afterwards the JAR is available through Maven:

```xml
<dependency>
  <groupId>org.complate</groupId>
  <artifactId>complate-core</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```


## Code of Conduct

[Contributor Code of Conduct](./CODE_OF_CONDUCT.md). By participating in this
project you agree to abide by its terms.


## License

complate-java is Open Source software released under the
[Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).
