import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
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
                System.out.println("Files in lastModified descending order : \n");
                Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
                LogReader.printFilesInformation(files);
            }
        }
    }

    private static void printFilesInformation(File[] files) throws IOException {
        Scanner scanner;
        LogsName logsName = new LogsName();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        long start = 0, end;
        for (File file : files) {
            ArrayList<String> lines = new ArrayList<>();
            ArrayList<String> logsDate = new ArrayList<>();
            ArrayList<String> uniqueLibrary = new ArrayList<>();
            Map<String, List<String>> map = new HashMap<>();
            System.out.printf("\n%2$td/%2$tm/%2$tY - %s%n", file.getName(), file.lastModified());
            scanner = new Scanner(file);
            String input;
            while (scanner.hasNextLine()) {
                start = System.nanoTime();
                input = scanner.nextLine();
                lines.add(input);
            }
            end = System.nanoTime();
            for (String s : logsName.listOfLogs) {
                map.put(s, lines
                        .stream()
                        .filter(c -> c.contains(s))
                        .collect(Collectors.toList()));
            }

            logsName.setNumberOfINFO(map.get(logsName.getInfoLog()).size());
            logsName.setNumberOfDEBUG(map.get(logsName.getDebugLog()).size());
            logsName.setNumberOfFATAL(map.get(logsName.getFatalLog()).size());
            logsName.setNumberOfWARN(map.get(logsName.getWarnLog()).size());
            logsName.setNumberOfEROR(map.get(logsName.getErrorLog()).size());

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

            float dif = ((((float) (logsName.getNumberOfEROR() + logsName.getNumberOfFATAL())))
                    / (logsName.getNumberOfDEBUG() + logsName.getNumberOfEROR() + logsName.getNumberOfFATAL() +
                    logsName.getNumberOfINFO() + logsName.getNumberOfWARN())) * 100;

            System.out.println("Time in nano seconds to read file: " + (end - start) + "\n" + timeBetween +
                    "\nNumbers of used unique library: " + hashSet.size() +
                    "\nRatio of the number of logs with severity ERROR or higher to all logs: " + decimalFormat.format(dif));

            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                System.out.println(
                        "Numbers of " + entry.getKey() + " : " + entry.getValue().size());
            }
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