package net.cheney.snax.builder;

import net.cheney.snax.SNAX;

import org.junit.Test;

public class SimpleBuilderTest {

	@Test public void testBuilder() {
		DocumentBuilder b = SNAX.newDocument();
		b.start("car").close();
	}
	
	@Test public void testBuilder2() {
		DocumentBuilder b = SNAX.newDocument();
		b.start("car").text("Prius").end().close();
	}
	
	@Test public void testBuilderWithComment() {
		DocumentBuilder b = SNAX.newDocument();
		b.comment("This is a hybrid car.")
		   .start("car").child("model", "Prius").close();
	}
	
	@Test public void testTwoElementWithAttr() {
		DocumentBuilder b = SNAX.newDocument();
		String year = "2008", make = "Toyota", model = "Prius";
	    b.start("car")
	       .attr("year", year)
	       .child("make", make)
	       .child("model", model)
	       .end();
	}
	
	@Test public void testNamespaces() {
		DocumentBuilder b = SNAX.newDocument();
		b.start("car").attr("year", 2008)
		   .defaultNamespace("http://www.ociweb.com/cars", "car.xsd")
		   .namespace("m", "http://www.ociweb.com/model", "model.xsd")
		   .child("m", "model", "Prius").close();
	}
}
