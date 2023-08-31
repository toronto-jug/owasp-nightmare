/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.tjug.owaspnightmare;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
public class MessagesController {

    private final List<Message> messages = new ArrayList<>();

    @GetMapping("/")
    public ModelAndView index(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oauth2User,
            @RequestParam(defaultValue = "false") boolean richText) {
        final ModelAndView modelAndView = new ModelAndView("messages");
        modelAndView.addObject("userName", oauth2User.getName());
        modelAndView.addObject("clientName", authorizedClient.getClientRegistration().getClientName());
        modelAndView.addObject("userAttributes", oauth2User.getAttributes());
        modelAndView.addObject("messages", messages);
        modelAndView.addObject("risky", richText);

        return modelAndView;
    }

    @PostMapping(value = "/message", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> submitMessage(@RequestParam String text, @AuthenticationPrincipal OAuth2User oauth2User) {
        messages.add(new Message(oauth2User.getAttribute("name"), oauth2User.getAttribute("picture"), Instant.now(), text));
        return ResponseEntity.status(302).location(URI.create("/")).build();
    }

    @RequestMapping(method = {GET, POST}, path = "/error-page")
    public String displayErrorPage() {
        return "error";
    }

    public record Message(String name, String profileUrl, Instant timestamp, String text) {
    }

}
