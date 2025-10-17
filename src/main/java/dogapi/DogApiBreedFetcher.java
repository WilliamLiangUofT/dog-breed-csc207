package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) {
        // TODO Task 1: Complete this method based on its provided documentation
        //      and the documentation for the dog.ceo API. You may find it helpful
        //      to refer to the examples of using OkHttpClient from the last lab,
        //      as well as the code for parsing JSON responses.
        String url = "https://dog.ceo/api/breed/" + breed.toLowerCase() + "list";
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new BreedNotFoundException("API call unsuccessful");
            }

            String strBody = response.body().string();
            JSONObject jsonObject = new JSONObject(strBody);
            String status = jsonObject.getString("status");
            if (!status.equals("success")) {
                throw new BreedNotFoundException("API call unsuccessful");
            }

            JSONArray message = jsonObject.getJSONArray("message");
            ArrayList<String> listOfSubBreeds = new ArrayList<>();
            for (int i = 0; i < message.length(); i++) {
                listOfSubBreeds.add(message.getString(i));
            }
            return listOfSubBreeds;

        } catch (IOException e) {
            throw new BreedNotFoundException("Error fetching sub-breed data");
        }
    }
}