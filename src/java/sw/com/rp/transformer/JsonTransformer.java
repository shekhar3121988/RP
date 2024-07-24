/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.transformer;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import com.fasterxml.jackson.core.JsonGenerator;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 *
 * @author Admin
 */
public class JsonTransformer {

    public synchronized static String transformToJson(Object object) throws Exception {
        String jsonData = null;
        ObjectMapper objectMapper = null;
        objectMapper = new ObjectMapper();
        objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        Writer strWriter = new StringWriter();
        objectMapper.writeValue(strWriter, object);

        objectMapper = null;
        jsonData = strWriter.toString();
        return jsonData;
    }

    public synchronized static Object transformToJavaObject(String jsonData, Class _class) throws Exception {
        Object javaObject = null;
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        if (jsonData != null) {
            javaObject = objectMapper.readValue(jsonData, _class);
        }
        objectMapper = null;
        return javaObject;
    }

    public synchronized static Object transformToJavaObjectArrayList(String jsonData, Class _class) throws Exception {
        Object javaObject = null;
        ObjectMapper objectMapper = new ObjectMapper();
        if (jsonData != null) {
            javaObject = objectMapper.readValue(jsonData, TypeFactory.defaultInstance().constructParametricType(Collection.class, _class));
        }
        objectMapper = null;
        return javaObject;
    }

    public synchronized static Object transformToJavaObjects(Object t, Class _class) throws Exception {
        Object javaObject = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//
        javaObject = mapper.convertValue(t, mapper.getTypeFactory().constructCollectionType(List.class, _class));

        //objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper = null;

        return javaObject;
    }
}
