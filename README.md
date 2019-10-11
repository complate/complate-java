# complate-java
*- Rendering of JSX based views in Java*

[![license](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0) [![Build Status](https://travis-ci.org/complate/complate-java.svg?branch=master)](https://travis-ci.org/complate/complate-java)

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


## License

complate-java is Open Source software released under the
[Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).
