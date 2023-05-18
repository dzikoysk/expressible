package expressible

operator fun <A> expressible.Mono<A>.component1(): A = first

operator fun <A, B> expressible.Pair<A, B>.component1(): A = first
operator fun <A, B> expressible.Pair<A, B>.component2(): B = second

operator fun <A, B, C> expressible.Triple<A, B, C>.component1(): A = first
operator fun <A, B, C> expressible.Triple<A, B, C>.component2(): B = second
operator fun <A, B, C> expressible.Triple<A, B, C>.component3(): C = third

operator fun <A, B, C, D> expressible.Quad<A, B, C, D>.component1(): A = first
operator fun <A, B, C, D> expressible.Quad<A, B, C, D>.component2(): B = second
operator fun <A, B, C, D> expressible.Quad<A, B, C, D>.component3(): C = third
operator fun <A, B, C, D> expressible.Quad<A, B, C, D>.component4(): D = fourth
