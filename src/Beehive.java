import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Beehive {

    public static int countCells(String[] honeycomb) {
        assert honeycomb != null;
        return Arrays.stream(honeycomb).mapToInt(s -> s.replaceAll("[^O]", "").length()).sum();
    }

    public static int[] countWNb(String[][] honeycomb) {
        assert honeycomb != null;
        int[] r = new int[honeycomb.length];
        for (int i = 0; i < honeycomb.length; i++) {
            String[] nest = honeycomb[i];
            if (Arrays.stream(nest).mapToInt(s -> s.replaceAll("[^W]", "").length()).sum() != 1) {
                throw new IllegalArgumentException("get fucked");
            }
            int[] waspCoord = getWaspCoord(nest);
            int[][] dirs = {{-1, -1}, {-1, 1}, {0, 2}, {0, -2}, {1, -1}, {1, 1}};
            for (int[] dir : dirs) {
                if (nest[waspCoord[0] + dir[0]].toCharArray()[waspCoord[1] + dir[1]] == 'O') r[i]++;
            }

        }
        return r;
    }

    public static String[] wayOut(String[][] honeyComb) {
        assert honeyComb != null;
        String[] r = new String[honeyComb.length];
        int[] available = countWNb(honeyComb);
        for (int i = 0; i < honeyComb.length; i++) {
            String[] layer = honeyComb[i];
            if (Arrays.stream(layer).mapToInt(s -> s.replaceAll("[^W]", "").length()).sum() != 1) {
                throw new IllegalArgumentException("get fucked2");
            }
            int[] waspCoord = getWaspCoord(layer);
            int[][] dirs = {{-1, -1}, {-1, 1}, {0, 2}, {0, -2}, {1, -1}, {1, 1}};
            for (int[] dir : dirs) {
                int[] cc = Arrays.copyOf(waspCoord, 2);
                while (true) {
                    cc[0] += dir[0];
                    cc[1] += dir[1];
                    try {
                        if (layer[cc[0]].charAt(cc[1]) != 'O') {
                            break;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        r[i] = "FREE";
                        break;
                    }
                }
                if (r[i] != null) if (r[i].equals("FREE")) break;
            }
            if (r[i] == null) r[i] = "TRAPPED";
        }
        return r;
    }

    private static int[] getWaspCoord(String[] nest) {
        int[] waspCoord = null;
        String line = "";
        for (int j = 1; j < nest.length - 1; j++) {
            if (nest[j].contains("W")) {
                waspCoord = new int[]{j, nest[j].indexOf("W")};
                line = nest[j];
                break;
            }
        }
        assert waspCoord != null;
        int indent = line.indexOf("-") == 0 ? 1 : 0;
        if (waspCoord[1] == indent || waspCoord[1] == line.length() - 2 + indent) {
            throw new IllegalArgumentException("get fucked");
        }
        return waspCoord;
    }


    public static String[] readInFile1(String filename) {
        try {
            return Files.readAllLines(Path.of(filename)).toArray(new String[0]);
        } catch (IOException e) {
            System.out.println("problem with reading in file");
            return null;
        }
    }

    public static String[][] readInFile2(String filename) {
        try {
            Path path = Path.of(filename);
            int j = Integer.parseInt(Files.readAllLines(path).toArray()[0].toString());
            String[][] r = new String[j][];

            List<String> a = Files.readAllLines(path);
            int count = 0;
            for (int i = 1; i < a.size(); i++) {
                if (count == r.length) break;
                if (!a.get(i).isEmpty()) {
                    ArrayList<String> comb = new ArrayList<>();
                    for (; i < a.size() && !a.get(i).isEmpty(); i++) comb.add(a.get(i));
                    r[count] = comb.toArray(new String[0]);
                    count++;
                }
                if (i == a.size() - 1 && count == r.length) break;
            }

            return r;
        } catch (IOException e) {
            System.out.println("problem with reading in file");
            return null;
        }
    }

    public static void createSolutionFileLevel2(String inp) throws IOException {
        OutputStream outS = Files.newOutputStream(Path.of("ressoruces/outLevel2"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        int[] a = countWNb(Objects.requireNonNull(readInFile2(inp)));
        for (int i : a) {
            outS.write((i + System.lineSeparator()).getBytes());
        }
        outS.close();

    }

    public static void createSolutionFileLevel3(String inp) throws IOException {
        OutputStream outS = Files.newOutputStream(Path.of("ressoruces/outLevel3"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        String[] a = wayOut(Objects.requireNonNull(readInFile2(inp)));
        for (String i : a) outS.write((i + System.lineSeparator()).getBytes());
        outS.close();
    }

    public static void main(String[] args) {
        try {
            createSolutionFileLevel3("ressoruces/Inp3");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problem");
        }

    }

    public static void printArray(String[][] a) {
        Arrays.stream(a).map(Arrays::toString).forEach(System.out::println);
        System.out.println();
    }


}