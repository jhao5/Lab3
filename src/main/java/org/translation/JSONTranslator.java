package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    // TODO Task: pick appropriate instance variables for this class
    private Map<Map<String, Object>, Map<String, String>> translationsByCountry = new HashMap<>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> code = new HashMap<>();
                Map<String, String> translation = new HashMap<>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                for (String key : jsonObject.keySet()) {
                    if ("id".equals(key) || "alpha2".equals(key) || "alpha3".equals(key)) {
                        code.put(key, jsonObject.get(key));
                    }
                    else {
                        translation.put(key, jsonObject.getString(key));
                    }
                }
                translationsByCountry.put(code, translation);
            }

            // TODO Task: use the data in the jsonArray to populate your instance variables
            //            Note: this will likely be one of the most substantial pieces of code you write in this lab.
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        // TODO Task: return an appropriate list of language codes,
        //            but make sure there is no aliasing to a mutable object
        for (Map.Entry<Map<String, Object>, Map<String, String>> entry : translationsByCountry.entrySet()) {
            if (entry.getKey().containsValue(country)) {
                return entry.getValue().keySet().stream().toList();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getCountries() {
        // TODO Task: return an appropriate list of country codes,
        //            but make sure there is no aliasing to a mutable object
        List<String> countries = new ArrayList<>();
        for (Map.Entry<Map<String, Object>, Map<String, String>> entry : translationsByCountry.entrySet()) {
            countries.add(Objects.toString(entry.getKey().get("alpha2")));
        }
        return countries;
    }

    @Override
    public String translate(String country, String language) {
        // TODO Task: complete this method using your instance variables as needed
        for (Map.Entry<Map<String, Object>, Map<String, String>> entry : translationsByCountry.entrySet()) {
            if (entry.getKey().containsValue(country)) {
                return entry.getValue().get(language);
            }
        }
        return null;
    }
}
