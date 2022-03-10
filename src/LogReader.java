import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LogReader {
    public static void main(String[] args) throws IOException {
        File dir = new File("C://logs");
        File[] files = dir.listFiles();
        if (files != null) {
            if (files.length <= 0) {
                System.out.println("Dir is empty");
            } else {
                System.out.println("In lastModified descending order : \n");
                Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
                LogReader.printFileOrder(files);
            }
        }
    }

    private static void printFileOrder(File[] files) throws IOException {
        Scanner scanner;
        long start = 0;
        for (File file : files) {
            ArrayList<String> lines = new ArrayList<>();
            ArrayList<String> logsDate = new ArrayList<>();
            ArrayList<String> uniqueLibrary = new ArrayList<>();
            int numberOfWARN, numberOfINFO, numberOfEROR, numberOfFATAL, numberOfDEBUG;
            System.out.printf("\n%2$td/%2$tm/%2$tY - %s%n", file.getName(), file.lastModified());
            scanner = new Scanner(file);
            String input;
            while (scanner.hasNextLine()) {
                start = System.nanoTime();
                input = scanner.nextLine();
                lines.add(input);
            }
            long end = System.nanoTime();
            List<String> listOfInfos = lines
                    .stream()
                    .filter(c -> c.contains("INFO"))
                    .collect(Collectors.toList());
            numberOfINFO = listOfInfos.size();
            List<String> listOfDebugs = lines
                    .stream()
                    .filter(c -> c.contains("DEBUG"))
                    .collect(Collectors.toList());
            numberOfDEBUG = listOfDebugs.size();
            List<String> listOfFatales = lines
                    .stream()
                    .filter(c -> c.contains("FATAL"))
                    .collect(Collectors.toList());
            numberOfFATAL = listOfFatales.size();
            List<String> listOfWarns = lines
                    .stream()
                    .filter(c -> c.contains("WARN"))
                    .collect(Collectors.toList());
            numberOfWARN = listOfWarns.size();
            List<String> listOfErrors = lines
                    .stream()
                    .filter(c -> c.contains("ERROR"))
                    .collect(Collectors.toList());
            numberOfEROR = listOfErrors.size();
            System.out.println("Time in nano seconds to read file: " + (end - start));
            for (String line : lines) {
                if (!Objects.equals(line, "")) {
                    if (line.matches("^[0-9].*")) {
                        logsDate.add(line.substring(0, 11));
                        Matcher matcher = Pattern.compile("\\[(.*?)]").matcher(line);
                        while (matcher.find()) {
                            uniqueLibrary.add(String.valueOf(matcher.group()));
                        }
                    }
                }
            }
            logsDate.sort(Comparator.naturalOrder());
            HashSet<String> hashSet = new HashSet<>(uniqueLibrary);
            String timeBetween = LogReader.daysBetweenLogs(logsDate.get(0), logsDate.get(logsDate.size() - 1));
            float dif = ((((float) (numberOfEROR + numberOfFATAL))) / (numberOfDEBUG + numberOfEROR + numberOfFATAL + numberOfINFO + numberOfWARN)) * 100;
            System.out.println(timeBetween);
            System.out.println(
                    "Numbers of INFO severity logs: " + numberOfINFO + "\n" +
                            "Numbers of DEBUG severity logs: " + numberOfDEBUG + "\n" +
                            "Numbers of FATAL severity logs: " + numberOfFATAL + "\n" +
                            "Numbers of WARN severity logs: " + numberOfWARN + "\n" +
                            "Numbers of ERROR severity logs: " + numberOfEROR + "\n" +
                            "Numbers of used unique library: " + hashSet.size());
            System.out.printf("Ratio of the number of logs with severity ERROR or higher to all logs: %.2f\n", dif);
        }
    }

    private static String daysBetweenLogs(String eldest, String newest) {
        String daysBetween = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateLatest = dateFormat.parse(eldest);
            Date dateNewest = dateFormat.parse(newest);
            long difference = dateNewest.getTime() - dateLatest.getTime();
            daysBetween = "Between newest and eldest log was " + (TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)) + " days";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return daysBetween;
    }
}