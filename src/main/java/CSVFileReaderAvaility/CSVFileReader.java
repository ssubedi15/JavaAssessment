package CSVFileReaderAvaility;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CSVFileReader {

    private static HashMap<String, List<Enrollee>> enrollees;

    public static void main(String[] args) throws IOException {
        enrollees  = new HashMap<>();
        // create a reader
        Reader reader = Files.newBufferedReader(Paths.get("src/main/java/CSVFileReaderAvaility/sampleCsvFile.csv"));

        // create csv bean reader
        CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                .withType(Enrollee.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        // iterate through users
        for (Enrollee enrollee : (Iterable<Enrollee>) csvToBean) {
            boolean isNewEnrollee = true;
            String userId = enrollee.getUserId();
            String name = enrollee.getName();
            Integer version = enrollee.getVersion();
            String insuranceCompany = enrollee.getInsuranceCompany();

            //Set Values into Enrollee Object
            Enrollee newEnrollee = new Enrollee();
            newEnrollee.setUserId(userId);
            newEnrollee.setName(name);
            newEnrollee.setVersion(version);
            newEnrollee.setInsuranceCompany(insuranceCompany);

            //Check map if insurance company already exists in map
            if(enrollees.containsKey(insuranceCompany))
            {
                //Loop through Enrollee List to check for existing User Id
                for(int i = 0; i < enrollees.get(insuranceCompany).size(); i++)
                {
                    if(enrollees.get(insuranceCompany).get(i).getUserId().equals(newEnrollee.getUserId()))
                    {
                        //Check if new Enrollee entry has latest Version
                        if(enrollees.get(insuranceCompany).get(i).getVersion() < newEnrollee.getVersion())
                        {
                            enrollees.get(insuranceCompany).set(i, newEnrollee);
                            isNewEnrollee = false;
                            break;
                        }
                    }
                }
                //Add New Enrollee object
                if(isNewEnrollee) {
                    enrollees.get(insuranceCompany).add(newEnrollee);
                }
            }
            else
            {
                //Create new Enrollee list and map to insurance company
                List<Enrollee> enrolleeList = new ArrayList<>();
                enrolleeList.add(newEnrollee);
                enrollees.put(insuranceCompany, enrolleeList);
            }
        }

        int count = 1;
        //Iterate List of Enrollees
        for (Map.Entry<String,List<Enrollee>> entry : enrollees.entrySet())
        {
            //Sort Enrollees by Name
            Collections.sort(enrollees.get(entry.getValue().get(0).getInsuranceCompany()), compareByName);

            // create a write
            String fileName="C:\\tempFile\\insuranceFile"+count+".csv";
            Writer writer = Files.newBufferedWriter(Paths.get(fileName));

            // header record
            String[] headerRecord = {"userid", "name", "version", "insuranceCompany"};

            // create a csv writer
            ICSVWriter csvWriter = new CSVWriterBuilder(writer)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                    .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                    .build();

            // write header record
            csvWriter.writeNext(headerRecord);

            for(int i = 0; i < enrollees.get(entry.getValue().get(0).getInsuranceCompany()).size(); i++)
            {
                //Create new record for csv file
                csvWriter.writeNext(
                        new String[] {
                                entry.getValue().get(i).getUserId(),
                                entry.getValue().get(i).getName(),
                                entry.getValue().get(i).getVersion().toString(),
                                entry.getValue().get(i).getInsuranceCompany()
                        });
            }

            count++;
            // close writers
            csvWriter.close();
            writer.close();
        }
        // close readers
        reader.close();
    }

    //Comparator to sort Enrollees by Name
    static Comparator<Enrollee> compareByName = new Comparator<Enrollee>() {
        @Override
        public int compare(Enrollee o1, Enrollee o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
}
