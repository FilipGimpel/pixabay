## **Pixabay example**

This small project is designed as a showcase. Although it functions as intended, I chose to simplify the implementation by serializing tags as comma-separated values instead of creating a separate table with a many-to-many relationship for Hit tags.

The project uses the Pixabay API, which requires an API key. To set this up, create a file named **apikey.properties** in the project's root directory and add the following content:

```
pixabay.api.key="your-api-key"
```

Replace "your-api-key" with your actual API key. Gradle will automatically use this key in the project.