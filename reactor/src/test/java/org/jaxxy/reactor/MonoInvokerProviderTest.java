/*
 * Copyright (c) 2018 The Jaxxy Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jaxxy.reactor;

import javax.ws.rs.core.MediaType;

import org.jaxxy.test.JaxrsClientConfig;
import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.hello.HelloResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MonoInvokerProviderTest extends JaxrsTestCase<HelloResource> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private HelloResource resource;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void configureClient(JaxrsClientConfig config) {
        super.configureClient(config);
        config.withProvider(new MonoInvokerProvider());
    }

    @Override
    protected HelloResource createServiceObject() {
        return resource;
    }

    @Test
    public void get() {
        when(resource.sayHello("RX")).thenReturn("Hello, RX!");
        final Mono<String> response = webTarget()
                .path("hello").path("RX")
                .request(MediaType.TEXT_PLAIN_TYPE)
                .rx(MonoInvoker.class)
                .get(String.class);
        assertThat(response.block()).isEqualTo("Hello, RX!");
    }
}