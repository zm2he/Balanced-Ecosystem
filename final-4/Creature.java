public abstract class Creature extends Life
{   
    //set up booleans for live and reproduce
    //continually updated
    protected boolean live = true;
    protected boolean reproduce = false;

    //set up row and col for each creature
    protected int row, col;

    public Creature(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    //accessors
    public boolean getLive()
    {
        return live;
    }

    public int getRow ()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public boolean getReproduce()
    {
        return reproduce;
    }

    //change row and col, so object's row and col are the same as the one in grid
    public void changeRow (int r)
    {
        row = r;
    }

    public void changeCol (int c)
    {
        col = c;
    }

    //accepts grid, row, and col
    //returns x and y positions of new creature to be made in int[]position
    public int[] reproduce(Life[][]grid, int row, int col)
    {
        //set up array to store x and y
        int[] position = new int[2];
        position[0] = -1;
        position[1] = -1;

        //only do if creature can reproduce
        if(reproduce)
        {
            //hard coded all cases
            //==null means cell is empty
            //check if row and col are within grid
            if(row-1 >= 0 && grid[row-1][col] == null)
            {
                position[0] = row-1;
                position[1] = col;
            }
            else if (row-1 >= 0 && col-1 >= 0 && grid[row-1][col-1] == null)
            {
                position[0] = row-1;
                position[1] = col-1;
            }
            else if (row-1 >= 0 && col+1 < grid[0].length && grid[row-1][col+1] == null)
            {
                position[0] = row-1;
                position[1] = col+1;
            }
            else if (row+1 < grid.length && grid[row+1][col] == null)
            {
                position[0] = row+1;
                position[1] = col;
            }
            else if (row+1 < grid.length && col-1 >= 0 && grid[row+1][col-1] == null)
            {
                position[0] = row+1;
                position[1] = col-1;
            }
            else if (row+1 < grid.length && col+1 < grid[0].length && grid[row+1][col+1] == null )
            {
                position[0] = row+1;
                position[1] = col+1;
            }
            else if (col-1 >= 0 && grid[row][col-1] == null)
            {
                position[0] = row;
                position[1] = col-1;
            }
            else if(col+1 < grid[0].length && grid[row][col] == null)
            {
                position[0] = row;
                position[1] = col+1;
            }
        }
        return position;
    }
}
