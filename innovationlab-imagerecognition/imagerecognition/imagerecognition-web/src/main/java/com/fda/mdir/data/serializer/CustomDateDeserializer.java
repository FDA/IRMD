/**
 * 
 */
package com.fda.mdir.data.serializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fda.mdir.constants.LABConstants;
import com.fda.mdir.init.AppContextListener;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author Muazzam Ali
 *
 */
public class CustomDateDeserializer extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		
            LABConstants narmsConstants = (LABConstants)AppContextListener.getSpringContext().getBean("labConstants");
            String DATE_FORMAT = (String)narmsConstants.getCONSTANTS().get("DATE_FORMAT");  
            
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            String date = parser.getText();
            try {
                return format.parse(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
	}

}
