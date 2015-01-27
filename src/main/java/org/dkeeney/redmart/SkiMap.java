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

    public SkiMap(String fileName) throws IOException {
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
                    map[j][i] = new SkiSlope(j, i, Integer.parseInt(values[j]));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight(int x, int y) {
        return map[x][y].getHeight();
    }
}
