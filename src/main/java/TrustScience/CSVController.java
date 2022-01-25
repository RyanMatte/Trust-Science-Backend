package TrustScience;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.*;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CSVController
{
    @PostMapping(path = "/csvToJson")
    public String csvtoJSON(@RequestParam("file") MultipartFile aInCsvFile) throws IOException {
        try {

            CsvSchema csv = CsvSchema.emptySchema().withHeader();
            CsvMapper csvMapper = new CsvMapper();
            csvMapper.enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE);
            csvMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader().forType(Map.class).with(csv).readValues(convert(aInCsvFile));
            List<Map<?, ?>> list = mappingIterator.readAll();
            String json = new Gson().toJson(list);
            File outFile = new File("csvToJson.json");
            FileWriter writer = new FileWriter(outFile);
            writer.write(json);
            writer.flush();
            writer.close();
            return json;
        }
        catch(IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public static File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}

