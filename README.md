


## Classes:
- abstract BaseFood
    - String name
    - String getName()
    - String toString

- SimpleFood extends BaseFood
    - double calories
    - double portion
    - String serialize()
    - Double calories()
    - Double portion()
    - Double caloriesPortion()

- Food extends SimpleFood
    - ArrayList< BaseFood > components
    - ArrayList< Double > quantities
    - int unresolved
    - SimpleFood parse(Scanner scan)
    - static double scanWUnit(Scanner scan)
    - boolean isResolved()
    - void resolveComponents(NameToFood getFood)
    - void addCalories(double kcal, double Q, int total)
    - void addComponent(Food food, double quantity)
    - String serialize()
    - toString()
    - double getCalories()

- Foodpedia
    - HashMap< String, SimpleFood > foods
    - static Foodpedia openAndSync(String path)
    - Food[] FindFood(String name) // finds more or less matching results
    - Foodpedia(File file) // opens and parses filepedia file
    - SimpleFood repas(String[] args)
    - void addFood(SimpleFood food)
    - SimpleFood FindExactMatch(String name)
    - void resolveDependencies()
    - boolean isResolved(SimpleFood food)
    - void resolveFood(SimpleFood food)
    - void serialize(File file)

- FoodpediaParseException extends Exception
    - String line
    - Long line_n
    - void printToUser()

- GraphShow
    - static DataPoint implements Comparable< DataPoint >
        - long key
        - double value
        - int compareTo(DataPoint o)
    - static DataPointComparator implements Comparator< GraphShow.DataPoint >
        - int compare(GraphShow.DataPoint o1, GraphShow.DataPoint o2)
    - static GraphShow get()
    - void drawGraph(ArrayList< DataPoint > points, String genre)

- Levenshtein
    - static int calculate(String x, String y)
    - static int costOfSubstitution(char a, char b)
    (not in project)

- UnresolvedFood extends BaseFood
    - String toString()

Main functions :

- Calories
- EatStats
- IveEaten