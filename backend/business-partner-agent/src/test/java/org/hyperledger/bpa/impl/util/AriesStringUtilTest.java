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
package org.hyperledger.bpa.impl.util;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AriesStringUtilTest {

    @Test
    void testGetSeqNo() {
        assertEquals("571", AriesStringUtil.credDefIdGetSequenceNo("M6Mbe3qx7vB4wpZF4sBRjt:3:CL:571:bank_account"));
    }

    @Test
    void testGetSchemaName() {
        assertEquals("bank_account", AriesStringUtil.schemaGetName("M6Mbe3qx7vB4wpZF4sBRjt:2:bank_account:1.0"));
    }

    @Test
    void testIsSchemaId() {
        assertTrue(AriesStringUtil.isIndySchemaId("3kigpmNVRJyj1NpCkqJqpa:2:a-schema:1.0"));
        assertFalse(AriesStringUtil.isIndySchemaId("something:else"));
        assertFalse(AriesStringUtil.isIndySchemaId("   "));
        assertFalse(AriesStringUtil.isIndySchemaId(null));
    }

    @Test
    void testGetDidAcyPyFormat() {
        assertEquals("44XuhwdnXMqxbQw9tFMQAp", AriesStringUtil.getLastSegment("did:sov:44XuhwdnXMqxbQw9tFMQAp"));
        assertEquals("44XuhwdnXMqxbQw9tFMQAp", AriesStringUtil.getLastSegment("44XuhwdnXMqxbQw9tFMQAp"));
    }

    @Test
    void testGetTag() {
        assertEquals("IATF Certificate", AriesStringUtil
                .credDefIdGetTag("nJvGcV7hBSLRSUvwGk2hT:3:CL:734:IATF Certificate"));
    }

    @Test
    void schemaAttributeFormat() {
        // remove trailing and ending whitespace
        assertEquals("pass", AriesStringUtil.schemaAttributeFormat(" pass "));
        // remove trailing and ending whitespace, replace other whitespace with default
        // char (underscore)
        assertEquals("one_two_three", AriesStringUtil.schemaAttributeFormat(" one two three "));
        // remove trailing and ending whitespace, replace other whitespace with another
        // char (dash)
        assertEquals("one-two-three", AriesStringUtil.schemaAttributeFormat(" one two three ", '-'));
        // use more whitespace, should be a single replacement
        assertEquals("one-two-three", AriesStringUtil.schemaAttributeFormat(" one   two    three ", '-'));
    }

    @Test
    void testIsUUID() {
        assertFalse(AriesStringUtil.isUUID(null));
        assertFalse(AriesStringUtil.isUUID(""));
        assertFalse(AriesStringUtil.isUUID("   "));
        assertFalse(AriesStringUtil.isUUID("Test String"));
        assertTrue(AriesStringUtil.isUUID(UUID.randomUUID().toString()));
    }

    @Test
    void testSchemaGetCreator() {
        // given 3kigpmNVRJyj1NpCkqJqpa:2:a-schema:1.0
        // creator is the first part
        final String validSchemaId = "3kigpmNVRJyj1NpCkqJqpa:2:a-schema:1.0";
        assertEquals("3kigpmNVRJyj1NpCkqJqpa", AriesStringUtil.schemaGetCreator(validSchemaId));

        final String invalidSchemaId = "3kigpmNVRJyj1NpCkqJqpa:2:a-schema"; // no version
        assertThrows(IllegalArgumentException.class, () -> AriesStringUtil.schemaGetCreator(invalidSchemaId));

        assertThrows(NullPointerException.class, () -> AriesStringUtil.schemaGetCreator(null));
    }

    @Test
    void testSchemaGetVersion() {
        // given 3kigpmNVRJyj1NpCkqJqpa:2:a-schema:1.0.01
        // creator is the first part
        final String validSchemaId = "3kigpmNVRJyj1NpCkqJqpa:2:a-schema:1.0.01";
        assertEquals("1.0.01", AriesStringUtil.schemaGetVersion(validSchemaId));

        final String invalidSchemaId = "3kigpmNVRJyj1NpCkqJqpa:2:a-schema:1.0.01:abc"; // extra segment
        assertThrows(IllegalArgumentException.class, () -> AriesStringUtil.schemaGetVersion(invalidSchemaId));

        assertThrows(NullPointerException.class, () -> AriesStringUtil.schemaGetVersion(null));
    }

    @Test
    void testIsCredDef() {
        assertFalse(AriesStringUtil.isCredDef(" "));
        assertFalse(AriesStringUtil.isCredDef(null));
        assertFalse(AriesStringUtil.isCredDef("test:1"));
        assertFalse(AriesStringUtil.isCredDef("test:1:2"));
        assertTrue(AriesStringUtil.isCredDef("EraYCDJUPsChbkw7S1vV96:3:CL:571:bob-bank-04"));
    }

    @Test
    void testParseRevocationNotification() {
        assertEquals("test", AriesStringUtil.revocationEventToRevocationInfo("indy::test::12").getRevRegId());
        assertEquals("12", AriesStringUtil.revocationEventToRevocationInfo("indy::test::12").getCredRevId());
        assertThrows(IllegalArgumentException.class,
                () -> AriesStringUtil.revocationEventToRevocationInfo("12:foo:1::12"));
    }

    @Test
    void testIsDidKey() {
        assertFalse(AriesStringUtil.isDidKey("did:key:äää"));
        assertFalse(AriesStringUtil.isDidKey("zUC7FswgVt61TDM5y88uM"));
        assertFalse(AriesStringUtil.isDidKey(""));
        assertFalse(AriesStringUtil.isDidKey(null));
        assertTrue(AriesStringUtil.isDidKey(
                "did:key:zUC7FswgVt61TDM5y88uM83gpLWZ7upVp7KEiTtggJcspSfbxKd3yHFRXz7njucXLEHU1QvUVa4ZxHWa7fBA68LQ9FL2ay9qcCFjYeHsBAhWT1HRDBnknQ9CCBXonAh6xmvoFap"));
    }

}
