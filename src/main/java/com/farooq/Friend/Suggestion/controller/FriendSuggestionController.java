package com.farooq.Friend.Suggestion.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class FriendSuggestionController {
    private final Map<String, Set<String>> friendsMap = new HashMap<>();
//    private final Set<String> availableUsers = new HashSet<>(Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace"));

//    @GetMapping("/users")
//    public Set<String> getAvailableUsers() {
//        return availableUsers;
//    }

    @GetMapping("/suggestions")
    public List<String> getSuggestions(@RequestParam String user) {
        List<String> suggestions = new ArrayList<>();
        Set<String> friends = friendsMap.get(user);
        if (friends != null) {
            for (String friend : friends) {
                Set<String> friendOfFriend = friendsMap.get(friend);
                if (friendOfFriend != null) {
                    for (String foaf : friendOfFriend) {
                        if (!foaf.equals(user) && (friends == null || !friends.contains(foaf)) && !suggestions.contains(foaf)) {
                            suggestions.add(foaf);
                        }
                    }
                }
            }
        }
        return suggestions;
    }

    @PostMapping("/addFriend")
    public String addFriend(@RequestParam String user, @RequestParam String friend) {
        if (!user.equals(friend)) {
            friendsMap.putIfAbsent(user, new HashSet<>());
            friendsMap.putIfAbsent(friend, new HashSet<>());
            friendsMap.get(user).add(friend);
            friendsMap.get(friend).add(user); // Make it mutual
            return "Friend added successfully";
        }
        return "Cannot add yourself as a friend";
    }

    @GetMapping("/users")
    public Set<String> getUsers() {
        return friendsMap.keySet();
    }
}
