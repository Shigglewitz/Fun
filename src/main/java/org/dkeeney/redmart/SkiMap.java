package org.dkeeney.redmart;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class SkiMap {
    private int width;
    private int height;

    private SkiSlope[][] map;

    private static final String DELIMITER = " ";

    public SkiMap(String fileName) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getClass()
                    .getResourceAsStream(fileName)));

            String input = br.readLine();
            String[] dimensions = input.split(DELIMITER);
            width = Integer.parseInt(dimensions[0]);
            height = Integer.parseInt(dimensions[1]);

            map = new SkiSlope[width][height];

            for (int i = 0; i < height; i++) {
                input = br.readLine();
                String[] values = input.split(DELIMITER);
                for (int j = 0; j < values.length; j++) {
                    map[j][i] = new SkiSlope(j, i, Integer.parseInt(values[j]),
                            this);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight(int x, int y) {
        return getSlope(x, y).getHeight();
    }

    public SkiSlope getSlope(int x, int y) {
        return map[x][y];
    }

    protected void init() {
        SkiSlope slope = null;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                slope = map[i][j];
                // border cases
                if (slope.getX() == 0) {
                    slope.cannotLeft();
                } else if (slope.getX() >= width - 1) {
                    slope.cannotRight();
                }
                if (slope.getY() == 0) {
                    slope.cannotUp();
                } else if (slope.getY() >= height - 1) {
                    slope.cannotDown();
                }

                // having processed border cases before and logic short
                // circuiting avoids IndexOutOfBoundsException
                if (slope.canLeft()
                        && slope.getHeight() <= map[i - 1][j].getHeight()) {
                    slope.cannotLeft();
                }
                if (slope.canRight()
                        && slope.getHeight() <= map[i + 1][j].getHeight()) {
                    slope.cannotRight();
                }
                if (slope.canUp()
                        && slope.getHeight() <= map[i][j - 1].getHeight()) {
                    slope.cannotUp();
                }
                if (slope.canDown()
                        && slope.getHeight() <= map[i][j + 1].getHeight()) {
                    slope.cannotDown();
                }

                if (!slope.canLeft() && !slope.canRight() && !slope.canUp()
                        && !slope.canDown()) {
                    slope.setGreatestDrop(0);
                    slope.setGreatestLength(1);
                }
            }
        }
    }
}
