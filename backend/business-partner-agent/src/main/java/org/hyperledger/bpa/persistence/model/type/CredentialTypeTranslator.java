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
package org.hyperledger.bpa.persistence.model.type;

import org.hyperledger.bpa.api.CredentialType;

public interface CredentialTypeTranslator {

    CredentialType getType();

    default boolean typeIsIndy() {
        return CredentialType.INDY.equals(getType());
    }

    default boolean typeIsJsonLd() {
        return CredentialType.JSON_LD.equals(getType());
    }

    default boolean typeIsOrgProfile() {
        return CredentialType.ORGANIZATIONAL_PROFILE_CREDENTIAL.equals(getType());
    }
}
