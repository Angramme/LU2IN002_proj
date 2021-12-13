


## Classes:
- abstract BaseFood
    - String name
    - String getName()
    - String toString
- Food
    - String name
    - ArrayList< Food > components
    - void parse(String s)
    - String serialize()
    - toString()
    - double getCalories()

- Foodpedia
    - Food[] FindFood(String name) // finds more or less matching results
    - Foodpedia(String filepath) // opens and parses filepedia file
    - addFood(Food food)
    - String serialize() // serializes to string
    - void serialize(String filepath) // serializes to file
    - boolean checkFoodDependencies() // checks if all referenced foods exist

- EatHistory
    - addDataPoint(Food[] eaten)
    - addDataPoint(Food[] eaten, Date date)
    - getAverageOver(PropertyAccessor prop, Date start, Date end)

- Date
    - String serialize()
    - int getTimestamp()