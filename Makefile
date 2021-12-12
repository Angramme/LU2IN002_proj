all: lib* Calories.class IveEaten.class EatStats.class

clean:
	rm */*.class

lib/BaseFood.class: lib/BaseFood.java
	javac lib/BaseFood.java

lib/EatHistory.class: lib/EatHistory.java lib/EatHistoryChunk.class
	javac lib/EatHistory.java

lib/EatHistoryChunk.class: lib/EatHistoryChunk.java
	javac lib/EatHistoryChunk.java

lib/Food.class: lib/Food.java lib/SimpleFood.class
	javac lib/Food.java

lib/Foodpedia.class: lib/Foodpedia.java lib/Food.class
	javac lib/Foodpedia.java

lib/SimpleFood.class: lib/SimpleFood.java lib/BaseFood.class
	javac lib/SimpleFood.java

lib/UnresolvedFood.class: lib/UnresolvedFood.java lib/BaseFood.class
	javac lib/UnresolvedFood.java

Calories.class: Calories.java lib/Foodpedia.class 
	javac Calories.java

IveEaten.class: IveEaten.java lib/Foodpedia.class lib/EatHistory.class
	javac IveEaten.java

EatStats.class: EatStats.java lib/Foodpedia.class lib/GraphShow.class
	javac EatStats.java

lib/GraphShow.class: lib/GraphShow.java
	javac -encoding UTF-8 lib/GraphShow.java
	# javac -encoding ISO-8859-1 lib/GraphShow.java