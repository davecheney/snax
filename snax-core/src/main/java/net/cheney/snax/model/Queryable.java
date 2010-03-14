package net.cheney.snax.model;

public interface Queryable {

	Iterable<? extends Node> find(String xpath);
}
