package me.jasperedits.command.annotation;

import me.jasperedits.command.settings.CommandAllowance;
import me.jasperedits.command.settings.CommandFormat;
import net.dv8tion.jda.api.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandType {

    CommandFormat format() default CommandFormat.INTERACTIVE;

    String[] names();

    Permission permission() default Permission.VIEW_CHANNEL;

    CommandAllowance allowance() default CommandAllowance.EVERYTHING;

    int minArguments() default 0;

    int maxArguments() default 0;
}