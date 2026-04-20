package api.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralised test data for Post-related API tests.
 * Keeping data here prevents magic values from leaking into test logic.
 */
public class PostTestData {

    // ── Existing resource IDs ──────────────────────────────────────────────

    /** A known post ID that must exist on the server. */
    public static final int EXISTING_POST_ID = 1;

    /** Expected userId owner of EXISTING_POST_ID. */
    public static final int EXISTING_POST_USER_ID = 1;

    // ── Create (POST) payload ──────────────────────────────────────────────

    public static final int    NEW_POST_USER_ID = 101;
    public static final String NEW_POST_TITLE   = "RestAssured TestNG Post";
    public static final String NEW_POST_BODY    = "This post was created by an automated API test.";

    /** Returns a fresh map each call to avoid shared-state issues between tests. */
    public static Map<String, Object> newPostPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", NEW_POST_USER_ID);
        payload.put("title",  NEW_POST_TITLE);
        payload.put("body",   NEW_POST_BODY);
        return payload;
    }

    // ── Update (PUT) payload ───────────────────────────────────────────────

    public static final int    UPDATE_POST_ID      = 1;
    public static final int    UPDATE_POST_USER_ID = 1;
    public static final String UPDATE_POST_TITLE   = "Updated Title via PUT";
    public static final String UPDATE_POST_BODY    = "Updated body content via PUT request.";

    public static Map<String, Object> updatePostPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id",     UPDATE_POST_ID);
        payload.put("userId", UPDATE_POST_USER_ID);
        payload.put("title",  UPDATE_POST_TITLE);
        payload.put("body",   UPDATE_POST_BODY);
        return payload;
    }

    // ── Delete ─────────────────────────────────────────────────────────────

    /** Post ID to target in the DELETE test. */
    public static final int DELETE_POST_ID = 1;
}
