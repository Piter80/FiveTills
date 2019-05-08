import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FiveTills {
    private static final int TILLS = 5;
    private static final int HOURSOFWORK = 5;

    private File[] files = null;
    private Double[][] tillsData = new Double[TILLS][HOURSOFWORK * 2];

    public static void main(String[] args) {
        FiveTills fiveTills = new FiveTills();
        //System.out.println(Arrays.deepToString(fiveTills.tillsData));
        fiveTills.getMostLoadedTime(fiveTills.tillsData);

    }

    public FiveTills() {
        files = getFileName(TILLS);
        for (int i = 0; i < files.length; i++) {
            tillsData[i] = getNumbersFromFile(files[i]);
        }
    }

    public void getMostLoadedTime(Double[][] load) {
        int period = 0;
        double loadedTime = -1;
        for (int i = 0; i < load[TILLS-1].length; i++) {
            int tills = TILLS-1;
            double tmpLoadedTime = 0;
            while (tills >= 0) {
                tmpLoadedTime += load[tills--][i];
            }
            if (loadedTime < tmpLoadedTime) {
                loadedTime = tmpLoadedTime;
                period = i+1;
            }
        }

        String begin = "";
        String end = "";
        if (period*30%60 == 0) {
            end = period*30/60+"";
            begin = period*30/60-1+":30";
        }
        else {
            end = period*30/60+":30";
            begin = period*30/60+"";
        }

        System.out.println("Tills will be maximum loaded from " +  begin + " hours from begin of work, to " + end + ", and load equal " + loadedTime);
    }


    public File[] getFileName(int numFiles) {
        File[] files = new File[numFiles]; //C:\IdeaProjects\fivetill\src\main\resources\till1.txt;
        try (BufferedReader fileNameReader = new BufferedReader(new InputStreamReader(System.in))) {
            for (int i = 0; i < numFiles; i++) {
                File readed = null;
                System.out.println("Please enter destination of data for till " + (i + 1));
                String fileName = fileNameReader.readLine();
                readed = new File(fileName);
                if (!readed.exists() || readed.isDirectory())
                    throw new IllegalArgumentException("File not exist or its directory. " + readed.getAbsolutePath());
                files[i] = readed;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    public Double[] getNumbersFromFile(File file) {
        List<Double> numbers = new ArrayList<>();
        //if file writed in OS Windows and started with BOM symbol
        Character bom = 65279;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));) {
            while (reader.ready()) {
                double number = Double.valueOf(reader.readLine().trim().replace(bom.toString(), ""));
                numbers.add(number);
            }
            if (numbers.isEmpty()) throw new IllegalStateException("This file is empty");
        } catch (IOException e) {
            System.out.println("Error when reading numbers from file. Maybe file have wrong format.");
            e.printStackTrace();
        }
        if (numbers.size() != 16)
            throw new IllegalStateException("File with data about customers is broken or have wrong size entries.");
        Double[] num = new Double[16];
        numbers.toArray(num);
        return num;

    }
}
