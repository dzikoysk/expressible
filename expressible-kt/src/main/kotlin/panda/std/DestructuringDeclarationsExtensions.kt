package panda.std

operator fun <A> Mono<A>.component1(): A = first

operator fun <A, B> Pair<A, B>.component1(): A = first
operator fun <A, B> Pair<A, B>.component2(): B = second

operator fun <A, B, C> Triple<A, B, C>.component1(): A = first
operator fun <A, B, C> Triple<A, B, C>.component2(): B = second
operator fun <A, B, C> Triple<A, B, C>.component3(): C = third

operator fun <A, B, C, D> Quad<A, B, C, D>.component1(): A = first
operator fun <A, B, C, D> Quad<A, B, C, D>.component2(): B = second
operator fun <A, B, C, D> Quad<A, B, C, D>.component3(): C = third
operator fun <A, B, C, D> Quad<A, B, C, D>.component4(): D = fourth
