package CSVFileReaderAvaility;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Enrollee {
    @CsvBindByName
    private String userId;
    @CsvBindByName
    private String name;
    @CsvBindByName
    private Integer version;
    @CsvBindByName
    private String insuranceCompany;
}
