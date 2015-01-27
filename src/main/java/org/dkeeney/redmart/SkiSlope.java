package org.dkeeney.redmart;

public class SkiSlope {
    private int x;
    private int y;
    private int height;
    private int greatestDrop = -1;
    private int greatestLength = -1;
    private SkiSlope nextSlope = null;
    private SkiMap map = null;
    private boolean canLeft = true;
    private boolean canRight = true;
    private boolean canUp = true;
    private boolean canDown = true;

    public SkiSlope(int x, int y, int height, SkiMap map) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.map = map;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getGreatestDrop() {
        return greatestDrop;
    }

    public void setGreatestDrop(int greatestDrop) {
        this.greatestDrop = greatestDrop;
    }

    public int getGreatestLength() {
        return greatestLength;
    }

    public void setGreatestLength(int greatestLength) {
        this.greatestLength = greatestLength;
    }

    public SkiSlope getNextSlope() {
        return nextSlope;
    }

    public void setNextSlope(SkiSlope nextSlope) {
        this.nextSlope = nextSlope;
    }

    public boolean isSolved() {
        return greatestLength > 0;
    }

    public boolean canLeft() {
        return canLeft;
    }

    public void cannotLeft() {
        canLeft = false;
    }

    public boolean canRight() {
        return canRight;
    }

    public void cannotRight() {
        canRight = false;
    }

    public boolean canUp() {
        return canUp;
    }

    public void cannotUp() {
        canUp = false;
    }

    public boolean canDown() {
        return canDown;
    }

    public void cannotDown() {
        canDown = false;
    }

    public SkiSlope getLeftNeighbor() {
        if (x == 0) {
            return null;
        }
        return map.getSlope(x - 1, y);
    }

    public SkiSlope getRightNeighbor() {
        if (x >= map.getWidth() - 1) {
            return null;
        }
        return map.getSlope(x + 1, y);
    }

    public SkiSlope getUpNeighbor() {
        if (y == 0) {
            return null;
        }
        return map.getSlope(x, y - 1);
    }

    public SkiSlope getDownNeighbor() {
        if (y >= map.getHeight() - 1) {
            return null;
        }
        return map.getSlope(x, y + 1);
    }

    @Override
    public String toString() {
        String ret = "(" + x + ", " + y + ") " + height;

        return ret;
    }

    public void printPath() {
        if (nextSlope == null) {
            return;
        }

        SkiSlope slope = this;

        do {
            System.out.print(slope.toString() + " --> ");
            slope = slope.getNextSlope();
        } while (slope != null);
    }
}
