package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting country codes to their names.
 */
public class CountryCodeConverter {
    public static final int SPACE = 3;
    private final Map<String, String> codeToCountryMap;
    private final Map<String, String> countryToCodeMap;

    /**
     * Default constructor which will load the country codes from "country-codes.txt"
     * in the resources folder.
     */
    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the country code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */

    public CountryCodeConverter(String filename) {

        // Initialize the HashMaps
        codeToCountryMap = new HashMap<>();
        countryToCodeMap = new HashMap<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split("\t");
                if (parts.length >= SPACE) {
                    String countryName = parts[0].trim();
                    String alpha3Code = parts[2].trim();

                    // Populate the maps
                    codeToCountryMap.put(alpha3Code, countryName);
                    countryToCodeMap.put(countryName, alpha3Code);

                }
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException("Error loading country data from file: " + ex.getMessage(), ex);
        }

    }

    /**
     * Returns the name of the country for the given country code.
     * @param code the 3-letter code of the country
     * @return the name of the country corresponding to the code, or null if not found
     */
    public String fromCountryCode(String code) {
        if (codeToCountryMap.containsKey(code.toUpperCase())) {
            return codeToCountryMap.get(code.toUpperCase());
        }
        return null;
    }

    /**
     * Returns the code of the country for the given country name.
     * @param country the name of the country
     * @return the 3-letter code of the country corresponding to the name, or null if not found
     */
    public String fromCountry(String country) {
        if (countryToCodeMap.containsKey(country)) {
            return countryToCodeMap.get(country);
        }
        return null;
    }

    /**
     * Returns how many countries are included in this code converter.
     * @return how many countries are included in this code converter.
     */
    public int getNumCountries() {
        return codeToCountryMap.size();
    }
}
