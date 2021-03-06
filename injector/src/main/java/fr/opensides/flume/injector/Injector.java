package fr.opensides.flume.injector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.ThreadLocalRandom;


/**
 * User: khanh
 * To change this template use File | Settings | File Templates.
 */
public class Injector {
    static final Logger LOGGER = LoggerFactory.getLogger("FILE");
    static final Logger LOGGER_INJECTOR = LoggerFactory.getLogger("INJECTOR");

    public static void main(String... args) throws IOException, InterruptedException {
        Type type = checkParameters(args);


        File file;
        BufferedReader br = null;
        switch (type) {
            case FILE:
                file = new File(args[1]);
                if (!file.exists() || !file.isFile()) {
                    System.out.println("File " + args[1] + " does not exist...\n");
                    printHelp();
                    System.exit(-1);
                }

                readAndLogLegacyFileLog(args[1]);
                break;
            case INJECT:
                readAndLogSpecFileLog("log.sample.txt");
                break;
        }
    }

    public static void readAndLogLegacyFileLog(String fileName) throws IOException, InterruptedException {
        File file = new File(fileName);
        String line;

        ThreadLocalRandom tlr = ThreadLocalRandom.current();

        try (BufferedReader br = new BufferedReader(new FileReader(file));) {

            while ((line = br.readLine()) != null) {
                long randomMillis = tlr.nextLong(1000);
                Thread.sleep(randomMillis);

                if (line.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.*$")) {
                    int index_comma = line.indexOf(",");
                    String date = line.substring(0, index_comma);
                    String content = line.substring(index_comma + 1);
                    LOGGER.info(date + "|" + content);
                }
            }
        }

    }

    public static void readAndLogSpecFileLog(String fileName) throws IOException, InterruptedException {
        String line;

        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        while (true) {
            try (InputStream inputStream = Injector.class.getClassLoader().getResourceAsStream("log.sample.txt");) {

                try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));) {

                    while ((line = br.readLine()) != null) {
                        long randomMillis = tlr.nextLong(1000);
                        Thread.sleep(randomMillis);

                        if (tlr.nextBoolean() && tlr.nextBoolean()) {
                            line = "2014-10-08T14:15:30-07:00;" + randomMillis + ";session-id-" + randomMillis + ";DSL380-29S;NOEE2;;1.0;FATAL;q.f.MonException;error.log|Illegal Argument Exception";
                        }
                        LOGGER_INJECTOR.info(line);
                    }

                    br.close();
                }
            }
        }

    }

    private static Type checkParameters(String[] args) {
        if (args == null || !(args.length == 2 || args.length == 1)) {
            printHelp();
            System.exit(-1);
        }

        Type type = null;
        try {
            type = Type.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            printHelp();
            System.exit(-1);
        }

        switch (type) {
            case INJECT:
                if (args.length != 1) {
                    printHelp();
                    System.exit(-1);
                }
                break;
            case FILE:
                if (args.length != 2) {
                    printHelp();
                    System.exit(-1);
                }
                break;
        }
        return type;
    }

    private static void printHelp() {
        StringBuilder sb = new StringBuilder();

        sb.append("Usage: java -jar flume-sample.jar [FILE file|INJECT]\n");
        sb.append("       where file is the file which contains datas\n\n");
        sb.append("       This file will be tailed and append into syslog\n");
        sb.append("    If INJECT is used, will read the file log.sample.txt");

        System.out.println(sb.toString());
    }
}
