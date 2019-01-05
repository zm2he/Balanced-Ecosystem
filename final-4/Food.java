//creates and manages food objects
class Food extends Life
{
    //int[] with population values for graph
    protected static int []population = new int[0];

    //back end variables that affect the spawn rate of food
    private static double foodSpawn = 0.01;//1% of empty spaces filled with food per turn
    private double foodSpawn1 = foodSpawn;

    private static int foodLimit = 20;//food has 50% of dying after 20 turns
    private int foodLimit1 = foodLimit;

    //counts how many turns the food has existed
    private int aliveCount = 0;

    //creates food object
    //count is number of turns the food has been there
    //want to simulate food going bad or disappearing after a set number of turns
    public Food(int count)
    {
        aliveCount = count;
    }

    //getters
    public int getAliveCount()
    {
        return aliveCount;
    }

    public int getFoodLimit()
    {
        return foodLimit;
    }

    //used in Life, so static
    public static double getFoodSpawn()
    {
        return foodSpawn;
    }

    //change alive count, used in Environment constructor
    //allows for more even decreasing of food
    public void setAliveCount(int num)
    {
        aliveCount = num;
    }

    //update instance variables --> affect spawn/decay rate of food
    //user is able to change
    public static void setFoodSpawn(double num)
    {
        foodSpawn = num;
    }

    public static void setFoodLimit(int num)
    {
        foodLimit = num;
    }

    //returns boolean if food object should remain for the next turn
    public boolean live ()
    {
        boolean live = false;
        foodSpawn1 = foodSpawn;

        //if less than food limit, food can remain
        if(aliveCount<foodLimit1)
            live = true;

        //greater than limit, 50% of dying
        else if (aliveCount>=foodLimit1)
        {
            live = (Math.random()<0.5)? true:false;
            if(live = false)
                aliveCount = 0;
        }

        //if it remains, update count
        if(live)
            aliveCount++;

        return live;
    }

    public void updateAliveCount()
    {
        aliveCount++;
    }

    public static void updatePopulation(int num)
    {
        add(num);
    }

    public static void add(int a)
    {
        int[] temp = new int[population.length+1];
        for(int i = 0; i<population.length; i++)
            temp[i] = population[i];
        temp[temp.length-1] = a;
        population = temp;
    }

    public static void reset()
    {
        population = new int[0];
    }
}
