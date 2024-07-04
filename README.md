## **Pixabay example**

This small project is designed as a showcase. Although it functions as intended, I chose to simplify the implementation by serializing tags as comma-separated values instead of creating a separate table with a many-to-many relationship for Hit tags.

The project uses the Pixabay API, which requires an API key. To set this up, create a file named **apikey.properties** in the project's root directory and add the following content:

```
pixabay.api.key="your-api-key"
```

Replace "your-api-key" with your actual API key. Gradle will automatically use this key in the project.

## **How cache works**

Sometimes, the API returns results for a given query that do not contain the query in their tags or content. This makes it impossible to retrieve these results during offline searches, as they lack any association with the query. To address this, all results returned by the API in response to a search query are saved in the database along with the corresponding query. This ensures that offline search results will be consistent with the online ones.