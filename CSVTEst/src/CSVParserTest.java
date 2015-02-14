import org.apache.commons.csv.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class CSVParserTest
{
    public static void main(String [ ] args) throws IOException
    {
        {
            File file = new File("C:\\Users\\NCS Customer\\Desktop\\csv.csv"); //need to make sure if this is the correct path

            FileReader fileReader = new FileReader(file);


            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();
            List<String> headers = Arrays.asList(line.split(","));

            Map<String, List<String>> mappedByHeader = new HashMap<String, List<String>>();

            CSVParser csvFileParser;
            CSVFormat csvFileFormat;

            String[] stockArr = new String[headers.size()];
            stockArr = headers.toArray(stockArr);

            List<CSVRecord> csvRecords = null;


            ArrayList<String> values = new ArrayList<String>();

            // run through each key add its values to the vector

            for (int i = 0; i < headers.size(); i++) {

                String currentHeader = headers.get(i);
                System.out.println(currentHeader);

                csvFileFormat = CSVFormat.DEFAULT.withHeader(stockArr).withSkipHeaderRecord();

                csvFileParser = new CSVParser(new FileReader(file), csvFileFormat);

                csvRecords = (List) csvFileParser.getRecords();

               // values.clear();
                String desiredValue = null;
                //System.out.println(csvRecords.size());


            }
            String value;
            if(!csvRecords.isEmpty()) {
                for (int i = 0; i < csvRecords.size(); i++) {
                    for (String header : headers) {
                        value = csvRecords.get(i).get(header);
                        if (i == 0) {
                            mappedByHeader.put(header, new ArrayList<String>());
                        }
                        mappedByHeader.get(header).add(new String(csvRecords.get(i).get(header)));
                    }
                }

                for (int i = 0; i < mappedByHeader.get("ID").size(); i++) {
                /* Add the ID's and process their given variables. */
                    int currentID = Integer.parseInt(mappedByHeader.get("ID").get(i));
                    double distance, speed, time;
                    int elevatorPosition;

                    distance = Double.parseDouble(mappedByHeader.get("Drive Distance").get(i));
                    speed = Double.parseDouble(mappedByHeader.get("Drive Speed").get(i));
                    time = Double.parseDouble(mappedByHeader.get("Time Out").get(i));
                    elevatorPosition = Integer.parseInt(mappedByHeader.get("Elevator Position").get(i));


                    System.out.println("ID: " + currentID);
                    System.out.println("Speed: " + speed);
                    System.out.println("Distance: " + distance);
                    System.out.println("Time: " + time);
                    System.out.println("Pos: " + elevatorPosition);
                }
            }
        }
    }
}
