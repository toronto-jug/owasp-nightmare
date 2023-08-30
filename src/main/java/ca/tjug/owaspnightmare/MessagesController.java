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

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MessagesController {

	private final List<Message> messages = new ArrayList<>();

	@GetMapping("/")
	public ModelAndView index(Principal principal) {
		final ModelAndView modelAndView = new ModelAndView("messages");
		modelAndView.addObject("principal", principal);
		modelAndView.addObject("messages", messages);

		return modelAndView;
	}

	@PostMapping(value = "/message", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<Void> submitMessage(Principal principal, @RequestParam String text) {
		messages.add(new Message(principal.getName(), Instant.now(), text));
		return ResponseEntity.status(302).location(URI.create("/")).build();
	}

	public record Message(String user, Instant timestamp, String text) {}

}
