package com.yiwugou.dbbus.core.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Command {
    private static final String CONFIG_FILE_KEY = "-config";

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String configFile;

    public static Command parse(String[] args) {
        Command command = new Command();
        if (args != null && args.length >= 2) {
            for (int i = 0, len = args.length; i < len; i++) {
                if (CONFIG_FILE_KEY.equalsIgnoreCase(args[i].trim())) {
                    command.setConfigFile(args[i + 1]);
                    i++;
                    break;
                }
            }
        }
        return command;
    }
}
