package com.nours.betterhoeharvester.commands;

import java.util.*;
import java.util.stream.Collectors;

public class TabHelper {

    public static final List<String> EMPTY_LIST = Collections.emptyList();

    public static List<String> matchTabComplete(String arg, Collection<String> availableCommands) {
        return Optional.ofNullable(arg)
                .filter(a -> !a.isEmpty())  // Ensure arg is not empty
                .map(String::toLowerCase)  // Convert to lowercase for case-insensitive matching
                .map(lowerArg -> availableCommands.stream()
                        .filter(command -> command.toLowerCase().startsWith(lowerArg))
                        .collect(Collectors.toList())
                )
                .orElseGet(() -> new ArrayList<>(availableCommands));  // Return all commands if arg is null or empty
    }
}
