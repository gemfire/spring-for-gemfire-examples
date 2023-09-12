/*
Copyright 2019 - 2021 VMware, Inc.
SPDX-License-Identifier: Apache-2.0
*/

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.session.data.gemfire.AbstractGemFireOperationsSessionRepository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import sessionstate.SessionStateApplication;

import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SessionStateApplication.class)
@AutoConfigureMockMvc
public class SessionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    CacheManager cacheManager;

    static String NOTE1 = "Nothing More Than Memories";
    static String NOTE2 = "Macarthur Park";

    @Test
    @DirtiesContext
    public void addSessionNote_should_addNoteToSessionInCache() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/addSessionNote")
                .content(NOTE1))
                .andExpect(status().isOk())
                .andReturn();

        String encodedSessionUUID = mvcResult.getResponse().getCookie("SESSION").getValue();

        List<String> notesList = getNotesForSessionInCache(encodedSessionUUID);

        assertEquals(NOTE1, (notesList.get(0)));
    }

    @Test
    @DirtiesContext
    public void addSessionNote_should_addMultipleNotesToSessionInCache() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/addSessionNote")
                .content(NOTE1))
                .andExpect(status().isOk())
                .andReturn();

        String encodedSessionUUID = mvcResult.getResponse().getCookie("SESSION").getValue();

        mockMvc.perform(post("/addSessionNote")
                .content(NOTE2)
                .cookie(new Cookie("SESSION", encodedSessionUUID)))
                .andExpect(status().isOk());

        List<String> notesList = getNotesForSessionInCache(encodedSessionUUID);

        assertEquals(2, notesList.size());
        assertEquals(NOTE1, (notesList.get(0)));
        assertEquals(NOTE2, (notesList.get(1)));
    }

    @Test
    @DirtiesContext
    public void getSessionNotes_should_returnAllNotesForSessionInCache() throws Exception {
        MvcResult mvcPostResult = mockMvc.perform(post("/addSessionNote")
                .content(NOTE1))
                .andExpect(status().isOk())
                .andReturn();

        String encodedSessionUUID = mvcPostResult.getResponse().getCookie("SESSION").getValue();

        mockMvc.perform(get("/getSessionNotes")
                .cookie(new Cookie("SESSION", encodedSessionUUID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0]").value(NOTE1));
    }

    @Test
    @DirtiesContext
    public void invalidateSession_should_removeSessionFromCache() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/addSessionNote")
                .content("Just to create session!"))
                .andExpect(status().isOk())
                .andReturn();

        String encodedSessionUUID = mvcResult.getResponse().getCookie("SESSION").getValue();

        mockMvc.perform(post("/invalidateSession")
                .cookie(new Cookie("SESSION", encodedSessionUUID)))
                .andExpect(status().isOk());

        ValueWrapper sessionContents = getCachedSessionUUIDs(encodedSessionUUID);

        assertNull(sessionContents);
    }

    private List<String> getNotesForSessionInCache(String encodedSessionUUID) {

        String sessionUUID = decodeBase64(encodedSessionUUID);
        List<String> notesFromCache = getNotesForSession(sessionUUID);

        return notesFromCache;
    }

    private ValueWrapper getCachedSessionUUIDs(String encodedSessionUUID) {
        String sessionUUID = decodeBase64(encodedSessionUUID);

        return cacheManager
                .getCache("ClusteredSpringSessions")
                .get(sessionUUID);
    }

    private String decodeBase64(String base64EncodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedString);
        return new String(decodedBytes);
    }

    private List<String> getNotesForSession(String sessionUUID) {
        return (List<String>) ((AbstractGemFireOperationsSessionRepository.GemFireSession)
                cacheManager
                        .getCache("ClusteredSpringSessions")
                        .get(sessionUUID)
                        .get())
                .getAttribute("NOTES");
    }
}
