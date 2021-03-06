package me.jasperedits.flora.command;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.experimental.UtilityClass;
import me.jasperedits.flora.command.annotation.ButtonAction;
import me.jasperedits.flora.command.annotation.CommandNames;
import me.jasperedits.flora.command.annotation.CommandType;
import me.jasperedits.flora.command.annotation.CommandType.Format;
import me.jasperedits.flora.command.impl.interactive.*;
import me.jasperedits.flora.command.impl.legacy.CalculateCurrentGuildObjective;
import me.jasperedits.flora.command.impl.legacy.UpdateGuildCommands;

import java.lang.reflect.Method;
import java.util.*;

@UtilityClass
public class CommandRegistry {
    private final Map<String, Command> legacyCommands;
    private final Map<String, Command> interactiveCommands;
    private final Map<Command, Method> buttonActions;

    static {
        legacyCommands = Maps.newHashMap();
        interactiveCommands = Maps.newHashMap();
        buttonActions = Maps.newHashMap();

        List<Command> commands = Arrays.asList(
                new Prefix(),
                new Setup(),
                new Ping(),
                new Config(),
                new Privacy(),
                new Start(),
                new CalculateCurrentGuildObjective(),
                new UpdateGuildCommands()
        );

        commands.forEach(commandObj -> {
            for (Method method : commandObj.getClass().getMethods()) {
                if (method.isAnnotationPresent(ButtonAction.class)) {
                    buttonActions.put(commandObj, method);
                }
            }

            List<String> names = new ArrayList<>();

            if (commandObj.getClass().isAnnotationPresent(CommandNames.class) && commandObj.getClass().getAnnotation(CommandNames.class).value() != null)
                names = Arrays.asList(commandObj.getClass().getAnnotation(CommandNames.class).value());
            else names.add(commandObj.getClass().getSimpleName().toLowerCase());

            for (String name : names) {
                switch (commandObj.getClass().getAnnotation(CommandType.class).format()) {
                    case LEGACY -> legacyCommands.put(name.toLowerCase(), commandObj);
                    case INTERACTIVE -> interactiveCommands.put(name.toLowerCase(), commandObj);
                }
            }
        });
    }

    /**
     * @param format the command {@link Format} to fetch
     * @param name   a command name
     * @return a {@link Command} that matches the provided name and {@link Format}
     */
    public Command byName(Format format, String name) {
        Map<String, Command> commandMap;

        switch (format) {
            case LEGACY -> commandMap = legacyCommands;
            case INTERACTIVE -> commandMap = interactiveCommands;
            default -> throw new IllegalStateException("Unexpected value: " + format);
        }

        return commandMap.getOrDefault(name, null);
    }

    /**
     * @param format the command format to fetch
     * @return a {@link Collection} of {@link Command} that have in common their {@link Format}
     */
    public Collection<Command> getAllCommands(CommandType.Format format) {
        Collection<Command> classes = Sets.newHashSet();
        Map<String, Command> commandMap;

        switch (format) {
            case LEGACY -> commandMap = legacyCommands;
            case INTERACTIVE -> commandMap = interactiveCommands;
            default -> throw new IllegalStateException("Unexpected value: " + format);
        }

        for (Command command : commandMap.values()) {
            if (!classes.contains(command)) {
                classes.add(command);
            }
        }
        return classes;
    }

    /**
     * @param command the {@link Command} you want to grab a {@link ButtonAction} from
     * @return a {@link Method} that handles a {@link ButtonAction}
     */
    public Method getButtonAction(Command command) {
        return buttonActions.get(command);
    }

}