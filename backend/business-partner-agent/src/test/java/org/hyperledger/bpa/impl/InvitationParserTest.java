/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/business-partner-agent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hyperledger.bpa.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.hyperledger.bpa.controller.api.invitation.CheckInvitationResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InvitationParserTest {

    public static MockWebServer mockWebServer;
    private InvitationParser p;

    @BeforeEach
    void init() throws Exception {
        p = new InvitationParser();
        p.setMapper(new ObjectMapper());

        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void TestParseReceiveInvitation() {
        InvitationParser.Invitation invitation = p.parseInvitation(this.invitation);
        Assertions.assertFalse(invitation.isOob());
        Assertions.assertTrue(invitation.isParsed());
        Assertions.assertNotNull(invitation.getInvitation());
    }

    @Test
    void TestParseReceiveOOBInvitation() {
        InvitationParser.Invitation invitation = p.parseInvitation(oob);
        Assertions.assertTrue(invitation.isOob());
        Assertions.assertTrue(invitation.isParsed());
        Assertions.assertNotNull(invitation.getInvitation());
    }

    @Test
    void TestParseReceiveDidCommInvitation() {
        InvitationParser.Invitation invitation = p.parseInvitation(this.didCommInvitation);
        Assertions.assertFalse(invitation.isOob());
        Assertions.assertTrue(invitation.isParsed());
        Assertions.assertNotNull(invitation.getInvitation());
    }

    @Test
    void TestParseReceiveOOBDidCommInvitation() {
        InvitationParser.Invitation invitation = p.parseInvitation(didCommOob);
        Assertions.assertTrue(invitation.isOob());
        Assertions.assertTrue(invitation.isParsed());
        Assertions.assertNotNull(invitation.getInvitation());
    }

    @Test
    void testStreetcredURI() {
        MockResponse response = new MockResponse()
                .setResponseCode(301)
                .setHeader("location", streetCredRedirect);
        mockWebServer.enqueue(response);
        String httpUrl = mockWebServer.url("/46yG3VegpCqc").toString();
        CheckInvitationResponse parsed = p.checkInvitation(httpUrl);
        Assertions.assertEquals("Snapper", parsed.getLabel());
    }

    private final String invitation = "ewogICAgIkB0eXBlIjogImRpZDpzb3Y6QnpDYnNOWWhNcmpIaXFaRFRVQVNIZztzcGVjL2Nvbm5lY3Rpb25zLzEuMC9pbnZpdGF0aW9uIiwKICAgICJAaWQiOiAiNGQ1OGJhZjktZDIwOS00MTE4LThkOTQtNGE0OTBlNGEwNGFhIiwKICAgICJzZXJ2aWNlRW5kcG9pbnQiOiAiaHR0cDovL2hvc3QuZG9ja2VyLmludGVybmFsOjgwMzAiLAogICAgInJlY2lwaWVudEtleXMiOiBbCiAgICAgICAgIjZCTlF1dFJIalNWNmJwQ0E2djVkRVB2NW12dWlRS2hyc256cEN4dUgzdXdqIgogICAgXSwKICAgICJsYWJlbCI6ICJCdXNpbmVzcyBQYXJ0bmVyIEFnZW50IDEiCn0=";
    private final String oob = "eyJAdHlwZSI6ICJkaWQ6c292OkJ6Q2JzTlloTXJqSGlxWkRUVUFTSGc7c3BlYy9vdXQtb2YtYmFuZC8xLjAvaW52aXRhdGlvbiIsICJAaWQiOiAiMmZhYmJhNzYtZTlhNy00Yzk4LTg2ZjMtMTFkNGE1MTYzYjQyIiwgImhhbmRzaGFrZV9wcm90b2NvbHMiOiBbImRpZDpzb3Y6QnpDYnNOWWhNcmpIaXFaRFRVQVNIZztzcGVjL2RpZGV4Y2hhbmdlLzEuMCJdLCAic2VydmljZXMiOiBbImRpZDpzb3Y6RXJhWUNESlVQc0NoYmt3N1MxdlY5NiJdLCAibGFiZWwiOiAiYm9iIn0=";

    private final String didCommInvitation = "eyJAdHlwZSI6ICJodHRwczovL2RpZGNvbW0ub3JnL2Nvbm5lY3Rpb25zLzEuMC9pbnZpdGF0aW9uIiwgIkBpZCI6ICJkNGE5ZmY4YS1jNjlmLTRiMWQtODJlYi04NzQwYWRiMzE0MmEiLCAic2VydmljZUVuZHBvaW50IjogImh0dHBzOi8vaW52aXRlMS1icGEtYWNhcHktZGV2LmFwcHMuc2lsdmVyLmRldm9wcy5nb3YuYmMuY2EiLCAibGFiZWwiOiAiaW52aXRlMSIsICJyZWNpcGllbnRLZXlzIjogWyI5MnV2TTFFOG9RbXFUNGZLZkdtam5UTndiandqYUZXYVRpRmtMZXNvbnhSVCJdfQ==";
    private final String didCommOob = "eyJAdHlwZSI6ICJodHRwczovL2RpZGNvbW0ub3JnL291dC1vZi1iYW5kLzEuMC9pbnZpdGF0aW9uIiwgIkBpZCI6ICI2ZmYzY2UzNy1kYjM1LTRjYTctYTNkOS03MWJmNGYxYzhkYzQiLCAibGFiZWwiOiAiaW52aXRlMSIsICJzZXJ2aWNlcyI6IFsiZGlkOnNvdjpXc3FWaW4xWjRZdnZiODdzU1E3QzJtIl0sICJoYW5kc2hha2VfcHJvdG9jb2xzIjogWyJodHRwczovL2RpZGNvbW0ub3JnL2RpZGV4Y2hhbmdlLzEuMCJdfQ==";

    private final String streetCredRedirect = "id.streetcred://launch/?d_m=eyJsYWJlbCI6IlNuYXBwZXIiLCJpbWFnZVVybCI6bnVsbCwic2VydmljZUVuZHBvaW50IjoiaHR0cHM6Ly90cmluc2ljLW1lZGlhdG9yLWFnZW50LWV1cm9wZS5henVyZXdlYnNpdGVzLm5ldC8iLCJyb3V0aW5nS2V5cyI6WyJDTFBmc3hVaDNMOWR2U2huNjRmYkZKZExrbzZHbmVhQkNEWkJQNjZpWVV3RCJdLCJyZWNpcGllbnRLZXlzIjpbIkc5cDVydVRqcDJiVHhWellIUVpySmZISkNDaENRVUpOVllrUWhTcGlmWTdkIl0sIkBpZCI6IjBiNTc1Zjc4LTNiNTQtNGFhNS1hMzMyLTcwNTljZDg5YzA1NiIsIkB0eXBlIjoiZGlkOnNvdjpCekNic05ZaE1yakhpcVpEVFVBU0hnO3NwZWMvY29ubmVjdGlvbnMvMS4wL2ludml0YXRpb24ifQ%3D%3D&orig=https%3a%2f%2fredir.trinsic.id%2f46yG3VegpCqc";
}
