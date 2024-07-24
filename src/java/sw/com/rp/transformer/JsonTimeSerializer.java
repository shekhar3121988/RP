/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.transformer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 *
 * @author Admin
 */
public class JsonTimeSerializer extends JsonSerializer<Date> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        String formattedDate = dateFormat.format(date);
        gen.writeString(formattedDate);
    }
}
