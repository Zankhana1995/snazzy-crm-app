# Candidate Thoughts

Please consider these questions:

### How would you add pagination to your solution?

> Right now, the /account endpoint returns all matching accounts at once. That works fine for a small dataset, but it wouldn’t scale well if the number of accounts grows significantly. The response size could become large, and performance would eventually suffer.
> 
> To add pagination, I would use Spring’s built-in Pageable support rather than building something custom. Spring Boot already handles page, size, and sort parameters automatically, so the controller method could accept a Pageable argument. For example:
> 
> GET /account?page=0&size=20&sort=name,asc
> 
> The controller would pass both the search filters and the Pageable object to the service layer. The service would then call the repository and return a Page<AccountResponse> instead of a List.
> 
> Since we’re using a custom Criteria query in the repository, I would apply pagination by setting setFirstResult() and setMaxResults() on the query. I would also run a separate count query to calculate the total number of matching records, so the API can return proper pagination metadata (like total pages and total elements).
> 
> The nice part about this approach is that it works well with the existing architecture, so we wouldn’t need to restructure anything significantly. Since it follows Spring’s standard patterns, it also keeps the codebase clean and familiar for other developers.
> 
> If the dataset becomes very large, I would also consider using cursor-based (keyset) pagination instead of offset-based pagination, since offsets can become slow at scale.

### How do you expect performance to be impacted as more account and contact fields are made searchable?

> As more fields become searchable, I would expect the complexity of the queries to increase, and with that, the overall query performance could start to degrade especially as the dataset grows.
> 
> Right now, the search is fairly focused (for example, matching contact phone numbers). If we start adding more searchable fields — such as account name, status, contact first name, last name, etc. the query will require more joins and more WHERE conditions. That increases the workload on the database and can make query execution plans more complex.
> 
> Another important factor is indexing. Some searches, especially those using LIKE %value%, can prevent effective index usage because of the leading wildcard. If we add more searchable fields without adding proper indexes, we could end up with full table scans, which would significantly impact performance as the data grows.
> 
> I would also pay attention to the LEFT JOIN and fetch strategy we’re currently using. As more fields and relationships are included in the search, the result set could grow larger. This can increase memory usage and slow down query execution.
> 
> To maintain performance, I would:
> 
> Add database indexes on frequently searched columns / foreign key columns are indexed (e.g., account_id) / Without indexing, LIKE queries may result in full table scans.
> 
> For high-scale systems, I would consider : Fetching accounts first Then loading contacts separately
> 
> Instead of guessing, I would check how the queries perform with realistic data volumes. Looking at query execution plans helps identify bottlenecks early.
> 
> If search becomes a central feature and performance starts to suffer significantly, then I would consider something like full-text search or a dedicated search engine. But I wouldn’t introduce that complexity unless the system actually requires it.
> 
> 