

clean:
	rm */*.class

lib/UFood.class: lib/UFood.java
	javac lib/UFood.java

lib/Food.class: lib/Food.java lib/UFood.class
	javac lib/Food.java

lib/Foodpedia.class: lib/Foodpedia.java lib/Food.class
	javac lib/Foodpedia.java

Calories.class: Calories.java lib/Foodpedia.class 
	javac Calories.java

IveEaten.class: IveEaten.java lib/Foodpedia.class 
	javac IveEaten.java

EatStats.class: EatStats.java lib/Foodpedia.class 
	javac EatStats.java

