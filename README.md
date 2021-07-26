# Expressible [![CI](https://github.com/panda-lang/expressible/actions/workflows/maven.yml/badge.svg)](https://github.com/panda-lang/expressible/actions/workflows/maven.yml) [![codecov](https://codecov.io/gh/panda-lang/expressible/branch/main/graph/badge.svg?token=LI1PAPD6NM)](https://codecov.io/gh/panda-lang/expressible)
Utility library, part of the panda-lang SDK, dedicated for functional codebases that require enhanced response handling.
Express yourself with inspired by Rust & Kotlin wrappers to provide better API using this tiny library.

<hr>

Supported wrappers (in `panda.std.*` package):
* `Result<Value, Error>`
* `Option<Value>`
* `Lazy<Value>`
* `Mono<A>`, `Pair<A, B>`, `Triple<A, B, C>`, `Quad<A, B, C, D>`
* `Completable<Value>` with `Publisher & Subscriber` pattern
* Throwing functions, runnables, suppliers and consumers
* Tri and Quad consumers, functions and predicates 
* `PandaStream<Value>` 

<hr>

```xml
<dependency>
    <groupId>org.panda-lang</groupId>
    <artifactId>expressible</artifactId>
    <version>1.0.3</version>
</dependency>>
```

Available in panda-lang repository:

```xml
<repository>
    <id>panda-repository</id>
    <url>https://repo.panda-lang.org/releases</url>
</repository>
```

#### Used by

* Panda Organization (Panda, Hub, Light)
* Reposilite
* Utilities like CDN, Dependency-Injector
* FunnyGuilds Organization (FunnyGuilds, FunnyCommands)
* Private projects and API consumers of the given libraries
