//root of hierarchy
//allow Life[][]grid to hold food, prey, and predator objects
abstract class Life
{
    protected int[] population = new int[0];
    //accepts Life object and returns boolean if to spawn food
    public static boolean spawnFood(Life cell)
    {
        boolean live = false;
        if(cell == null)
        {
            if(Math.random()<Food.getFoodSpawn())//compare with foodSpawn value
                live = true;
        }
        return live;
    }
}