# uptimefinder

Preconfigurations required:
1. Alter the values according to your local or remote MySql engine.
2. Use the query in the [sql file](https://github.com/sharangramana/uptimefinder/blob/1.0.0/implement-uptime-finder/schema.sql) to create tables manually in the MySql database.
3. Run the Application and Use the REST API's via postman collection

Post man collection attached here [Postman Collection](https://github.com/sharangramana/uptimefinder/blob/1.0.0/implement-uptime-finder/Uptime%20Finder.postman_collection.json)

### Batch Processing - IN PROGRESS
Using spring batch to process the CSV and upload it in the database. Batch process will run in separate thread
Please track here - [batch processing](https://github.com/sharangramana/uptimefinder/tree/1.0.0/implement-batch-processing)
