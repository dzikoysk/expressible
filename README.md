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
repositories {
    maven { url 'https://repo.panda-lang.org/releases' }
}

dependencies {
    implementation("org.panda-lang:expressible:1.0.16") // Core library
    implementation("org.panda-lang:expressible-kt:1.0.16") // Kotlin extensions
    implementation("org.panda-lang:expressible-kt-coroutines:1.0.16") // Kotlin coroutines extensions
    testImplementation("org.panda-lang:expressible-junit:1.0.16") // JUnit extensions
}
```

### Examples
Suggested snippets show only a small use-cases for the available api. 
You're not forced to use this library this way, so you may need to find your style in expressing your thoughts.
Adopting functional approach requires time and to simplify this process it's easier to slowly introduce new elements based on simple concepts.

#### Option

#### Result

Rather than using `Exception` based error handling, return meaningful errors and interact with api responses gracefully.
Following functional programming patterns make sure your methods don't contain side effects and unexpected exit points. 

```kotlin
class UserFacade {

    // You can start adoption in a regular, non-functional codebases
    fun createUser(username: String): Result<User, String> {
        if (userRepository.findUserByName(username).isPresent()) {
            return error("User $username already exists")
        }

        if (!usernamePattern.matches(username)) {
            return error("Invalid username")
        }

        // [...]
        return ok(userRepository.createUser(username))
    }

}

class UserEndpoint {
    
    fun createUser(request: HttpRequest, response: HttpResponse) =
        request.createUsername(request.param("username"))
            .mapErr { ErrorReposne(BAD_REQUEST, it) }
            .let { response.respondWithJsonDto(it.any) }

}

internal class UserFacadeTest : UserSpec {

    @Test
    fun `should create user with a valid username` () {
        // given: a valid username
        val username = 'onlypanda'
        // when: user is created with the following name
        val user = userFacade.createUser(username)
        // then: user has been created
        assertTrue user.isOk()
        assertEquals username, user.get().getUsername()
    }
    
} 
```

#### Lazy

#### Completable

#### Mono, Pair, Triple, Quad

#### Panda Stream

### Used by

* [Panda Organization](https://github.com/panda-lang) ([Panda](https://github.com/panda-lang/panda), [Hub](https://github.com/panda-lang/hub), [Light](https://github.com/panda-lang))
* [Reposilite](https://github.com/dzikoysk/reposilite)
* Utilities like [CDN](https://github.com/dzikoysk/cdn), [Dependency-Injector](https://github.com/dzikoysk/dependency-injector)
* [FunnyGuilds Organization](https://github.com/FunnyGuilds) ([FunnyGuilds](https://github.com/FunnyGuilds/FunnyGuilds), [FunnyCommands](https://github.com/FunnyGuilds/FunnyCommands))
* Private projects and API consumers of the given libraries
