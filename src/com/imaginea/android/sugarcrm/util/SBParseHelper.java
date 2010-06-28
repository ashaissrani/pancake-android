package com.imaginea.android.sugarcrm.util;

import static com.imaginea.android.sugarcrm.RestUtilConstants.EXCEPTION;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SBParseHelper {

    /*
     * Helper for the SBParser and SugarBean to parse the JSON response to retrieve the name value
     * pairs either in the entry_list or the relationship_list
     */
    public static Map<String, String> getNameValuePairs(JSONObject jsonObject)
                                    throws SugarCrmException {
        Map<String, String> fields = new HashMap<String, String>();
        try {
            String nameValueList = jsonObject.get("name_value_list").toString();
            JSONObject nameVal = new JSONObject(nameValueList);
            Iterator iter = nameVal.keys();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                String val = ((JSONObject) (nameVal.get(key))).get("value").toString();
                fields.put(key, val);
            }
            return fields;
        } catch (JSONException e) {
            /*
             * when the select_fields is empty while making the rest call, the default response will
             * give empty JSONArray and hence it throws an exception
             */
            throw new SugarCrmException(EXCEPTION, "No name value pairs available!");
        }

    }
}