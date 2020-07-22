# Frequently Asked Questions

## Graal JavaScript

### Can I replace `NashornComplateRenderer` with `GraalComplateRenderer`?

The behavior of GraalVM JavaScript and Nashorn are quite different by default
and you wil probably need to adjust a few things. See Graal's [Migration Guide
from Nashorn to GraalVM
JavaScript](https://github.com/graalvm/graaljs/blob/master/docs/user/NashornMigrationGuide.md)
for details.

When creating a `GraalComplateRenderer` using the default settings host access
is allowed for
[ALL](https://www.graalvm.org/truffle/javadoc/org/graalvm/polyglot/HostAccess.html#ALL)
and host class lookup, experimental options, and the option
`js.experimental-foreign-object-prototype` are enabled. If you have been using
`NashornComplateRenderer` before then you probably want to enable
`js.nashorn-compat` as well, for example

```java
    GraalComplateRenderer.of(source)
        .withAdditionalContextCustomizations(ctx ->
            ctx.option("js.nashorn-compat", "true"))
        .build();

```

Even after enabling full compatibility a few differences remain. Things that are
known to have changed:

* `Java.from` in Graal will only translate Java `List`s to JavaScript arrays
  while it used to work for any type of collection with Nashorn.

* If your view wants to read entries from a Java `Map` the `mapName.key` syntax
  doesn't work with Graal, you have to use `mapName.get("key")`
  instead. Alternatively you can work around this by wrapping the `Map` instance
  with a `org.graalvm.polyglot.proxy.ProxyObject`.

### Is `GraalComplateRenderer` thread-safe?

No, the GraalVM JavaScript engine relies on a model where JavaScript runtimes
cannot be shared between different threads. For more details see
https://medium.com/graalvm/multi-threaded-java-javascript-language-interoperability-in-graalvm-2f19c1f9c37b

When using `GraalComplateRenderer` in a multi-threaded environment (e.g. using
[complate-spring-mvc](https://github.com/complate/complate-spring)) you may
encounter exceptions with a message like

```
Multi threaded access requested by thread Thread[...] but is not allowed for language(s) js.
```

You must wrap `GraalComplateRenderer` with `ComplateThreadLocalRenderer` when a
renderer could be used by multiple threads.
