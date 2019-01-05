class Predator extends Creature
{
    //in[] to store population values, used for graph
    protected static int []population = new int[0];

    //amount of hunger that is satisfied by eating 1 food
    public static int preyEnergy = 5;

    //will decrease every turn, die at zero, increase by eating
    private static int hunger = 30;
    private int hungerCounter = hunger;

    protected static int foodRequired = 100;//reproduce if hunger > x

    protected static int repCoolDown = 5;//takes x turns before it can reproduce again

    //set up reproduction variables
    protected int repCoolDownCounter = 5;//only reproduce every x turns

    //constructor
    public Predator(int row, int col)
    {
        super(row, col);
        live = true;
    }

    //accessor methods
    public int getPreyEnergy()
    {
        return preyEnergy;
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

    //able to change instance variables 
    public static void setPreyEnergy(int num)
    {
        preyEnergy = num;
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

    //accepts life [][] and updates row and col so predator
    //moves in the direction of the closest prey
    public void predatorMove(Life[][]grid)
    {
        //set up moveX and Y with random point, in case it doesn't work
        int moveX = (Math.random()>0.5)? 1 : -1;
        int moveY = (Math.random()>0.5)? 1 : -1;

        //get x and y value of closest prey, using method
        int[]closest = closestPrey(row,col,grid);

        //use +/- signs from change to calcualte which way to move
        int yChange = row - closest[0];
        int xChange = col - closest[1];

        //move predator in that direction
        if(xChange<0 && yChange<0)
        {
            moveX = 1;
            moveY = 1;
        }
        else if(xChange<0 && yChange>0)
        {
            moveX = 1;
            moveY = -1;
        }
        else if (xChange > 0&& yChange<0)
        {
            moveX = -1;
            moveY = 1;
        }
        else if (yChange>0 && xChange >0)
        {
            moveX = -1;
            moveY = -1;
        }

        //check row and col are within grid and update
        if(row+moveY < grid.length && row+moveY >= 0)
            row = row + moveY;
        if(col+moveX <grid[0].length && col+moveX >= 0)
            col = col + moveX;

        //remove 1 hunger, cost of moving
        hungerCounter--;
    }

    //accept row, col, and Life[][]
    public int[] closestPrey(int predatorRow, int predatorCol, Life[][] grid)
    {
        //set dummy values
        int distance = 0;
        int smallestDistance = 100;
        for(int r = 0; r<grid.length; r++)
            for(int c = 0; c<grid[0].length; c++)
                if(grid[r][c] instanceof Prey)
                {   //change in x and change in y, summed
                    //smallest distance = closest to predator
                    distance = Math.abs(predatorRow - r) + Math.abs(predatorCol - c);
                    if(distance<smallestDistance)//save smallest distance
                        smallestDistance = distance;
                }

        int preyRow = 0, preyCol = 0;
        for(int r = 0; r<grid.length; r++)
            for(int c = 0; c<grid[0].length; c++)
                if(grid[r][c] instanceof Prey)
                //find smallest distance, take row and col at the point
                    if(smallestDistance == Math.abs(predatorRow - r) + Math.abs(predatorCol - c))
                    {
                        preyRow = r;
                        preyCol = c;
                    }

        //return x and y values
        int[] closest = {preyRow, preyCol};
        return closest;
    }

    //accepts Life[][] and returns int[] of where the prey is
    public int[] eat(Life[][] grid)
    {
        //initialize eaten counter
        int eaten = 0;

        //setup int[] for storing x and y
        int[] position = new int[2];
        position[0] = -1;
        position[1] = -1;

        //hard coded all clases of 3x3 grid around prey
        //update eaten and position accordingly
        if (grid[row][col] instanceof Prey)
        {
            eaten ++;
            position[0] = row;
            position[1] = col;
        }
        else if (row-1 >= 0 && grid[row-1][col] instanceof Prey)
        {
            eaten ++;
            position[0] = row-1;
            position[1] = col;
        }
        else if (row-1 >= 0 && col-1 >= 0 && grid[row-1][col-1] instanceof Prey)
        {
            eaten++;
            position[0] = row-1;
            position[1] = col-1;
        }
        else if (row-1 >= 0 && col+1 < grid[0].length && grid[row-1][col+1] instanceof Prey)
        {
            eaten++;
            position[0] = row-1;
            position[1] = col+1;
        }
        else if (row+1 < grid.length && grid[row+1][col] instanceof Prey)
        {
            eaten++;
            position[0] = row+1;
            position[1] = col;
        }
        else if (row+1 < grid.length && col-1 >= 0 && grid[row+1][col-1] instanceof Prey)
        {
            eaten++;
            position[0] = row+1;
            position[1] = col-1;
        }
        else if (row+1 < grid.length && col+1 < grid[0].length && grid[row+1][col+1] instanceof Prey)
        {
            eaten++;
            position[0] = row+1;
            position[1] = col+1;
        }
        else if (col-1 >= 0 && grid[row][col-1] instanceof Prey )
        {
            eaten++;
            position[0] = row;
            position[1] = col-1;
        }
        else if(col+1 < grid[0].length && grid[row][col+1] instanceof Prey)
        {
            eaten++;
            position[0] = row;
            position[1] = col+1;
        }

        //eaten something
        if(position[0] != -1 && position[1]!=-1)
        {
            hungerCounter = hungerCounter + eaten*preyEnergy;//update hunger
        }

        return position;
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
