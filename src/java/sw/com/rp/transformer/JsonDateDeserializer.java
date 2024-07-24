/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.transformer;

//import com.sw.pa.config.PAConfig;
import java.io.IOException;
import java.util.Date;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 *
 * @author Admin
 */
public class JsonDateDeserializer extends JsonDeserializer<Date> {
//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    // private static final SimpleDateFormat dateFormat = new SimpleDateFormat(PAConfig.getDateFormat());

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
//                try {
//                        return dateFormat.parse(jp.getText());
//                } catch (ParseException e) {
//                        e.printStackTrace();
        return null;
//                }
    }
}
