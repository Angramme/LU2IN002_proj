


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
    - 

- EatHistory
    - addDataPoint(Food[] eaten)
    - addDataPoint(Food[] eaten, Date date)
    - getAverageOver(PropertyAccessor prop, Date start, Date end)