# Expressible [![CI](https://github.com/panda-lang/expressible/actions/workflows/maven.yml/badge.svg)](https://github.com/panda-lang/expressible/actions/workflows/maven.yml) [![codecov](https://codecov.io/gh/panda-lang/expressible/branch/main/graph/badge.svg?token=LI1PAPD6NM)](https://codecov.io/gh/panda-lang/expressible)
Utility library, part of the panda-lang SDK, dedicated for functional codebases that require enhanced response handling.
Express yourself with inspired by Rust, Kotlin and Vavr wrappers, to provide better API using this tiny library.

<hr>

Supported wrappers (in `panda.std.*` package):
* `Result<Value, Error>`
* `Option<Value>`
* `Lazy<Value>`
* `Completable<Value>` with `Publisher & Subscriber` pattern
* `Mono<A>`, `Pair<A, B>`, `Triple<A, B, C>`, `Quad<A, B, C, D>`
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

* [Panda Organization](https://github.com/panda-lang) ([Panda](https://github.com/panda-lang/panda), [Hub](https://github.com/panda-lang/hub), [Light](https://github.com/panda-lang))
* [Reposilite](https://github.com/dzikoysk/reposilite)
* Utilities like [CDN](https://github.com/dzikoysk/cdn), [Dependency-Injector](https://github.com/dzikoysk/dependency-injector)
* [FunnyGuilds Organization](https://github.com/FunnyGuilds) ([FunnyGuilds](https://github.com/FunnyGuilds/FunnyGuilds), [FunnyCommands](https://github.com/FunnyGuilds/FunnyCommands))
* Private projects and API consumers of the given libraries
