class Prey extends Creature
{
    //make int[] to store population data, used in graph
    protected static int []population = new int[0];

    //variables for movement
    private int moveCount = 25;
    private int direction = 0;

    //amount of hunger that is satisfied by eating 1 food
    public static int foodEnergy = 4;

    //will decrease every turn, die at zero, increase by eating
    private static int hunger = 15;
    private int hungerCounter = hunger;

    protected static int foodRequired = 5;//reproduce if hunger > x

    protected static int repCoolDown = 5;//takes x turns before it can reproduce again

    //set up reproduction variables
    protected int repCoolDownCounter = 5;//only reproduce every x turns

    //constructor
    public Prey(int row, int col)
    {
        super(row, col);
        live = true;
    }

    //accessor methods
    public int getFoodEnergy()
    {
        return foodEnergy;
    }

    public int getHunger()
    {
        return hunger;
    }

    public int getFoodRequired()
    {
        return foodRequired;
    }

    public int getRepCoolDown()
    {
        return repCoolDown;
    }

    //able to change energy levels of food
    public static void setFoodEnergy(int num)
    {
        foodEnergy = num;
    }

    public static void setHunger(int num)
    {
        hunger = num;
    }

    public static void setFoodRequired(int num)
    {
        foodRequired = num;
    }

    public static void setRepCoolDown(int num)
    {
        repCoolDown = num;
    }

    //check if it can reproduce, update boolean accordingly
    public void updateReproduce ()
    {
        if (hungerCounter>foodRequired && repCoolDownCounter <0)//can reproduce
        {
            reproduce = true;
            repCoolDownCounter = repCoolDown;//set repCoolDownCounter
        }
        else//can't reproduce
        {
            reproduce = false;
            repCoolDownCounter--;//decrease counter by 1
        }
    }

    //check if it can still live
    public void updateLive()
    {
        if(hungerCounter<=0)//dies when hunger falls below 0
            live = false;
    }

    //accepts grid and changes row and col in creature object accordingly to move
    //movement for prey
    public void preyMove (Life[][]grid)
    {
        int moveX = 0;
        int moveY = 0;
        double random = Math.random();

        //decides general direction for the prey to move for next 10 turns
        //greater probability that prey will move in that direction for 10 turns
        //allows prey to move beyond small enclosed range
        if(moveCount == 0 || moveCount == 25)//enter only for 1st time or when moveCount is 0
        {
            //1 = right, 2 = up, 3 = left, 4 = down;
            if(random>0.75)
                direction = 1;
            else if(random>0.5)
                direction = 2;
            else if(random>0.25)
                direction = 3;
            else
                direction = 4;

            //reset moveCount
            moveCount = 24;
        }

        //only do if it is within moveCount
        if(moveCount>0)
        {
            random = Math.random();
            //40% chance of moving right, 20% of moving in other 3 directions
            if(direction == 1)//right
            {
                if(random>0.6)
                    moveX = 1;
                else if(random>0.4)
                    moveX = -1;
                else if(random>0.2)
                    moveY = 1;
                else
                    moveY = -1;
            }
            else if(direction == 2)//up
            {
                if(random>0.6)
                    moveY = -1;
                else if(random>0.4)
                    moveX = -1;
                else if(random>0.2)
                    moveX = 1;
                else
                    moveY = 1;
            }
            else if(direction == 3)//left
            {
                if(random>0.6)
                    moveX = -1;
                else if(random>0.4)
                    moveY = -1;
                else if(random>0.2)
                    moveX = 1;
                else
                    moveY = 1;
            }
            else if(direction == 4)//down
            {
                if(random>0.6)
                    moveY = 1;
                else if(random>0.4)
                    moveY = -1;
                else if(random>0.2)
                    moveX = 1;
                else
                    moveX = -1;
            }
            moveCount--;
        }

        //check it is within size of grid
        //update row and col variables
        if(row+moveX < grid.length && row+moveX >= 0)
            row = row+moveX;
        if(col+moveY <grid[0].length && col+moveY >= 0)
            col = col + moveY;

        //always able to move, so always reduce hunger
        hungerCounter--;
    }

    //accepts Life[][] and returns x and y values of food to be eaten
    public int[] eat(Life[][] grid)
    {
        int eaten = 0;//counter for eat, to add to hunger

        //set up int[] to store x and y
        int[] position = new int[2];
        position[0] = -1;
        position[1] = -1;

        //hard coded all cases and corresponding x and y values
        if (grid[row][col] instanceof Food)
        {
            eaten ++;
            position[0] = row;
            position[1] = col;
        }
        else if (row-1 >= 0 && grid[row-1][col] instanceof Food)
        {
            eaten ++;
            position[0] = row-1;
            position[1] = col;
        }
        else if (row-1 >= 0 && col-1 >= 0 && grid[row-1][col-1] instanceof Food)
        {
            eaten++;
            position[0] = row-1;
            position[1] = col-1;
        }
        else if (row-1 >= 0 && col+1 < grid[0].length && grid[row-1][col+1] instanceof Food)
        {
            eaten++;
            position[0] = row-1;
            position[1] = col+1;
        }
        else if (row+1 < grid.length && grid[row+1][col] instanceof Food)
        {
            eaten++;
            position[0] = row+1;
            position[1] = col;
        }
        else if (row+1 < grid.length && col-1 >= 0 && grid[row+1][col-1] instanceof Food)
        {
            eaten++;
            position[0] = row+1;
            position[1] = col-1;
        }
        else if (row+1 < grid.length && col+1 < grid[0].length && grid[row+1][col+1] instanceof Food )
        {
            eaten++;
            position[0] = row+1;
            position[1] = col+1;
        }
        else if (col-1 >= 0 && grid[row][col-1] instanceof Food )
        {
            eaten++;
            position[0] = row;
            position[1] = col-1;
        }
        else if(col+1 < grid[0].length && grid[row][col] instanceof Food)
        {
            eaten++;
            position[0] = row;
            position[1] = col+1;
        }

        //eaten something
        if(position[0] != -1 && position[1]!=-1)
        {
            hungerCounter = hungerCounter + eaten*foodEnergy;//update hunger
        }

        return position;
    }

    //accepts num (population value at that generation) and adds it to int[] population
    public static void updatePopulation(int num)
    {
        add(num);
    }

    //add int to population
    private static void add(int a)
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