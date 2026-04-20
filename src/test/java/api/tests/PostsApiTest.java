package api.tests;

import api.data.PostTestData;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

/**
 * End-to-end tests for the /posts resource of JSONPlaceholder.
 *
 * Test order is declarative via `priority`; each test is independent —
 * no shared mutable state is carried between them.
 */
public class PostsApiTest extends BaseApiTest {

    // ──────────────────────────────────────────────────────────────────────────
    // 1. GET /posts — retrieve all posts
    // ──────────────────────────────────────────────────────────────────────────

    @Test(priority = 1,
          testName = "GET all posts returns 200 with a non-empty array",
          description = "Verify the /posts endpoint returns HTTP 200 and a JSON array " +
                        "containing at least one post object with required fields.")
    public void getAllPosts_shouldReturn200WithPostArray() {

        Response response = given()
                .spec(requestSpec)
            .when()
                .get("/posts")
            .then()
                .statusCode(200)
                .extract().response();

        // Deserialise body as a list of maps for flexible assertions
        List<Map<String, Object>> posts = response.jsonPath().getList("$");

        assertFalse(posts.isEmpty(), "Response array must not be empty");

        // JSONPlaceholder always returns 100 posts — assert a reasonable lower bound
        assertThat("Should return at least 100 posts", posts.size(), greaterThanOrEqualTo(100));

        // Spot-check that every item has the expected keys
        for (Map<String, Object> post : posts) {
            assertThat("Each post must have 'id'",     post, hasKey("id"));
            assertThat("Each post must have 'userId'", post, hasKey("userId"));
            assertThat("Each post must have 'title'",  post, hasKey("title"));
            assertThat("Each post must have 'body'",   post, hasKey("body"));
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // 2. GET /posts/{id} — retrieve a single post by ID
    // ──────────────────────────────────────────────────────────────────────────

    @Test(priority = 2,
          testName = "GET single post by ID returns 200 and correct post",
          description = "Verify that fetching a known post by ID returns HTTP 200 " +
                        "and that the returned id matches the requested id.")
    public void getPostById_shouldReturn200WithMatchingId() {

        int targetId = PostTestData.EXISTING_POST_ID;

        Response response = given()
                .spec(requestSpec)
                .pathParam("id", targetId)
            .when()
                .get("/posts/{id}")
            .then()
                .statusCode(200)
                .extract().response();

        int returnedId     = response.jsonPath().getInt("id");
        int returnedUserId = response.jsonPath().getInt("userId");
        String title       = response.jsonPath().getString("title");
        String body        = response.jsonPath().getString("body");

        assertEquals(returnedId, targetId,
                "Returned post id must match the requested id");
        assertEquals(returnedUserId, PostTestData.EXISTING_POST_USER_ID,
                "userId must match the known owner of post " + targetId);
        assertNotNull(title, "title field must be present and non-null");
        assertFalse(title.isEmpty(), "title must not be empty");
        assertNotNull(body, "body field must be present and non-null");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // 3. POST /posts — create a new post
    // ──────────────────────────────────────────────────────────────────────────

    @Test(priority = 3,
          testName = "POST new post returns 201 with created post data",
          description = "Verify that creating a post returns HTTP 201 and that the " +
                        "response echoes back all submitted fields.")
    public void createPost_shouldReturn201WithCreatedData() {

        Map<String, Object> payload = PostTestData.newPostPayload();

        Response response = given()
                .spec(requestSpec)
                .body(payload)
            .when()
                .post("/posts")
            .then()
                .statusCode(201)
                .extract().response();

        // JSONPlaceholder assigns id 101 for all new posts (fake server behaviour)
        int    createdId     = response.jsonPath().getInt("id");
        int    createdUserId = response.jsonPath().getInt("userId");
        String createdTitle  = response.jsonPath().getString("title");
        String createdBody   = response.jsonPath().getString("body");

        assertThat("Created post must receive a numeric id", createdId, greaterThan(0));
        assertEquals(createdUserId, PostTestData.NEW_POST_USER_ID,
                "userId in response must match submitted userId");
        assertEquals(createdTitle, PostTestData.NEW_POST_TITLE,
                "title in response must match submitted title");
        assertEquals(createdBody, PostTestData.NEW_POST_BODY,
                "body in response must match submitted body");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // 4. PUT /posts/{id} — full update of an existing post
    // ──────────────────────────────────────────────────────────────────────────

    @Test(priority = 4,
          testName = "PUT update post returns 200 with updated fields",
          description = "Verify that a full PUT update returns HTTP 200 and that " +
                        "all updated fields in the response match the request payload.")
    public void updatePost_shouldReturn200WithUpdatedFields() {

        Map<String, Object> payload = PostTestData.updatePostPayload();
        int targetId = PostTestData.UPDATE_POST_ID;

        Response response = given()
                .spec(requestSpec)
                .pathParam("id", targetId)
                .body(payload)
            .when()
                .put("/posts/{id}")
            .then()
                .statusCode(200)
                .extract().response();

        int    returnedId     = response.jsonPath().getInt("id");
        int    returnedUserId = response.jsonPath().getInt("userId");
        String returnedTitle  = response.jsonPath().getString("title");
        String returnedBody   = response.jsonPath().getString("body");

        assertEquals(returnedId, targetId,
                "id in response must match the targeted post id");
        assertEquals(returnedUserId, PostTestData.UPDATE_POST_USER_ID,
                "userId must match the submitted value");
        assertEquals(returnedTitle, PostTestData.UPDATE_POST_TITLE,
                "title must reflect the PUT update");
        assertEquals(returnedBody, PostTestData.UPDATE_POST_BODY,
                "body must reflect the PUT update");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // 5. DELETE /posts/{id} — delete a post
    // ──────────────────────────────────────────────────────────────────────────

    @Test(priority = 5,
          testName = "DELETE post returns 200 with empty body",
          description = "Verify that deleting a known post returns HTTP 200 " +
                        "and an empty JSON object, confirming the resource was removed.")
    public void deletePost_shouldReturn200() {

        int targetId = PostTestData.DELETE_POST_ID;

        Response response = given()
                .spec(requestSpec)
                .pathParam("id", targetId)
            .when()
                .delete("/posts/{id}")
            .then()
                .statusCode(200)
                .extract().response();

        // JSONPlaceholder returns `{}` on successful delete
        String body = response.getBody().asString().trim();
        assertThat("Response body on delete should be an empty JSON object",
                body, is("{}"));
    }
}
