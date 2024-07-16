## **Pixabay example**

The project uses the Pixabay API, which requires an API key. To set this up, create a file named **apikey.properties** in the project's root directory and add the following content:

```
pixabay.api.key="your-api-key"
```

Replace "your-api-key" with your actual API key. Gradle will automatically use this key in the project.

## **How cache works**

Sometimes, the API returns results for a given query that do not contain the query in their tags or content. This makes it impossible to retrieve these results during offline searches, as they lack any association with the query. To address this, all results returned by the API in response to a search query are saved in the database along with the corresponding query. This ensures that offline search results will be consistent with the online ones.

## **Paging**

Pagin is done using jetpack paging library. It uses offline data first, and if nothing is found there, fetches it from web.

The Pixabay API provides two sorting options for its results: the default "popularity" (which does not correspond to likes, views, or any attributes within the Hit object) and "latest" (even though the Hit object lacks a date attribute). To maintain the same order as the API when retrieving records from a database, it's advisable to assign custom IDs that capture the sequence in which the results were received from the API. (TODO)