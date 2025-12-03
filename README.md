# Search GitHub Repositories
The service queries the GitHub REST API /search/repositories endpoint with filters:

* Programming language (optional)
* Repository created after date (optional)
* Result limit (default: 200, max: 1000
* Scoring algorithm (simple or advanced)

### Scoring Alogorithm
 * Simple -> Stars, forks, recency weight
 * Advanced -> Velocity, star/fork ratio, recency model, size penalty

### Example Usage
```python
POST /api/repositories/popularity
```

Request Body Example:
```json
{
  "language": "java",
  "createdAfter": "2023-01-01",
  "algorithm": "advanced",
  "limit": 150
}

```

Response Example:
```json
[
  {
    "popularityScore": 421,
    "repositoryName": "spring-framework",
    "repositoryUrl": "https://github.com/spring-projects/spring-framework",
    "stars": 50000,
    "forks": 35000,
    "lastUpdatedAt": "2025-01-29T14:11:00Z",
    "createdAt": "2013-01-12T12:10:10Z"
  }
]
```




