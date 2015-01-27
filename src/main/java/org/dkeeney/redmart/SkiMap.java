package org.dkeeney.redmart;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    public SkiSlope solve() {
        Queue<SkiSlope> slopes = init();
        SkiSlope slope = null;
        SkiSlope neighbor = null;
        List<SkiSlope> neighbors = new ArrayList<>();
        SkiSlope answer = null;

        while (slopes.peek() != null) {
            slope = slopes.poll();

            neighbor = slope.getRightNeighbor();
            addSlopeIfNotNullAndNotSolved(neighbor, neighbors);
            neighbor = slope.getLeftNeighbor();
            addSlopeIfNotNullAndNotSolved(neighbor, neighbors);
            neighbor = slope.getUpNeighbor();
            addSlopeIfNotNullAndNotSolved(neighbor, neighbors);
            neighbor = slope.getDownNeighbor();
            addSlopeIfNotNullAndNotSolved(neighbor, neighbors);

            for (SkiSlope testMe : neighbors) {
                processNeighbor(testMe);
                if (testMe.isSolved()) {
                    slopes.add(testMe);
                }
            }

            neighbors.clear();

            if (!slope.isSolved()
                    && ((slope.getLeftNeighbor() != null && !slope
                            .getLeftNeighbor().isSolved())
                            || (slope.getRightNeighbor() != null && !slope
                                    .getRightNeighbor().isSolved())
                            || (slope.getUpNeighbor() != null && !slope
                                    .getUpNeighbor().isSolved()) || (slope
                            .getDownNeighbor() != null && !slope
                            .getDownNeighbor().isSolved()))) {
                slopes.add(slope);
            } else {
                if (answer == null
                        || slope.getGreatestLength() > answer
                                .getGreatestLength()
                        || (slope.getGreatestLength() == answer
                                .getGreatestLength() && slope.getGreatestDrop() > answer
                                .getGreatestDrop())) {
                    answer = slope;
                }
            }
        }

        answer.printPath();

        return answer;
    }

    private void addSlopeIfNotNullAndNotSolved(SkiSlope slope,
            List<SkiSlope> list) {
        if (slope != null && !slope.isSolved()) {
            list.add(slope);
        }
    }

    protected void processNeighbor(SkiSlope slope) {
        int numSolvedNeighbors = 0;
        List<SkiSlope> availableNeighbors = new LinkedList<>();

        if (slope.canLeft()) {
            availableNeighbors.add(slope.getLeftNeighbor());
            if (slope.getLeftNeighbor().isSolved()) {
                numSolvedNeighbors++;
            }
        }
        if (slope.canRight()) {
            availableNeighbors.add(slope.getRightNeighbor());
            if (slope.getRightNeighbor().isSolved()) {
                numSolvedNeighbors++;
            }
        }
        if (slope.canUp()) {
            availableNeighbors.add(slope.getUpNeighbor());
            if (slope.getUpNeighbor().isSolved()) {
                numSolvedNeighbors++;
            }
        }
        if (slope.canDown()) {
            availableNeighbors.add(slope.getDownNeighbor());
            if (slope.getDownNeighbor().isSolved()) {
                numSolvedNeighbors++;
            }
        }

        if (numSolvedNeighbors == availableNeighbors.size()) {
            SkiSlope bestNeighbor = null;
            int greatestDistance = 0;
            int greatestDescent = 0;
            for (SkiSlope neighbor : availableNeighbors) {
                if (bestNeighbor == null
                        || neighbor.getGreatestLength() > bestNeighbor
                                .getGreatestLength()
                        || (neighbor.getGreatestLength() == bestNeighbor
                                .getGreatestLength() && calculateGreatestDescent(
                                slope, neighbor) > greatestDescent)) {
                    bestNeighbor = neighbor;
                    greatestDistance = neighbor.getGreatestLength() + 1;
                    greatestDescent = calculateGreatestDescent(slope, neighbor);
                }
            }
            slope.setNextSlope(bestNeighbor);
            slope.setGreatestDrop(greatestDescent);
            slope.setGreatestLength(greatestDistance);
        }

    }

    protected int calculateGreatestDescent(SkiSlope higher, SkiSlope lower) {
        return higher.getHeight() - lower.getHeight() + lower.getGreatestDrop();
    }

    protected Queue<SkiSlope> init() {
        Queue<SkiSlope> queue = new LinkedList<>();
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
                    queue.add(slope);
                }
            }
        }

        return queue;
    }
}
