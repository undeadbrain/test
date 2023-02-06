package Basket;

import java.util.Arrays;
import java.util.List;

public class SysOut {
    // Style. String 8. There are two guides about Java's coding style: https://www.oracle.com/technetwork/java/codeconventions-150003.pdf, https://google.github.io/styleguide/javaguide.html;
    // 1. Constant names use UPPER_SNAKE_CASE
    // 2. Do not start variable name from _.
    private static final List<String> _colors = Arrays.asList("BLACK", "RED", "GREEN", "YELLOW", "BLUE", "MAGENTA", "CYAN", "WHITE");

    public enum Color {

        //Color end string, color reset
        RESET("\033[0m"),

        // Regular Colors. Normal color, no bold, background color etc.
        BLACK("\033[0;30m"),// BLACK
        RED("\033[0;31m"),// RED
        GREEN("\033[0;32m"),// GREEN
        YELLOW("\033[0;33m"), // YELLOW
        BLUE("\033[0;34m"), // BLUE
        MAGENTA("\033[0;35m"),// MAGENTA
        CYAN("\033[0;36m"), // CYAN
        WHITE("\033[0;37m"),// WHITE

        // Bold
        BLACK_BOLD("\033[1;30m"), // BLACK
        RED_BOLD("\033[1;31m"), // RED
        GREEN_BOLD("\033[1;32m"), // GREEN
        YELLOW_BOLD("\033[1;33m"),// YELLOW
        BLUE_BOLD("\033[1;34m"),// BLUE
        MAGENTA_BOLD("\033[1;35m"), // MAGENTA
        CYAN_BOLD("\033[1;36m"),// CYAN
        WHITE_BOLD("\033[1;37m"), // WHITE

        // Underline
        BLACK_UNDERLINED("\033[4;30m"), // BLACK
        RED_UNDERLINED("\033[4;31m"), // RED
        GREEN_UNDERLINED("\033[4;32m"), // GREEN
        YELLOW_UNDERLINED("\033[4;33m"),// YELLOW
        BLUE_UNDERLINED("\033[4;34m"),// BLUE
        MAGENTA_UNDERLINED("\033[4;35m"), // MAGENTA
        CYAN_UNDERLINED("\033[4;86m"),// CYAN
        WHITE_UNDERLINED("\033[4;97m"), // WHITE

        // Background
        BLACK_BACKGROUND("\033[60m"), // BLACK
        RED_BACKGROUND("\033[41m"), // RED
        GREEN_BACKGROUND("\033[42m"), // GREEN
        YELLOW_BACKGROUND("\033[43m"),// YELLOW
        BLUE_BACKGROUND("\033[44m"),// BLUE
        MAGENTA_BACKGROUND("\033[45m"), // MAGENTA
        CYAN_BACKGROUND("\033[46m"),// CYAN
        WHITE_BACKGROUND("\033[47m"), // WHITE

        // High Intensity
        BLACK_BRIGHT("\033[0;90m"), // BLACK
        RED_BRIGHT("\033[0;91m"), // RED
        GREEN_BRIGHT("\033[0;92m"), // GREEN
        YELLOW_BRIGHT("\033[0;93m"),// YELLOW
        BLUE_BRIGHT("\033[0;94m"),// BLUE
        MAGENTA_BRIGHT("\033[0;95m"), // MAGENTA
        CYAN_BRIGHT("\033[0;96m"),// CYAN
        WHITE_BRIGHT("\033[0;97m"), // WHITE

        // Bold High Intensity
        BLACK_BOLD_BRIGHT("\033[1;90m"),// BLACK
        RED_BOLD_BRIGHT("\033[1;91m"),// RED
        GREEN_BOLD_BRIGHT("\033[1;92m"),// GREEN
        YELLOW_BOLD_BRIGHT("\033[1;93m"), // YELLOW
        BLUE_BOLD_BRIGHT("\033[1;94m"), // BLUE
        MAGENTA_BOLD_BRIGHT("\033[1;95m"),// MAGENTA
        CYAN_BOLD_BRIGHT("\033[1;96m"), // CYAN
        WHITE_BOLD_BRIGHT("\033[1;97m"),// WHITE

        // High Intensity backgrounds
        BLACK_BACKGROUND_BRIGHT("\033[0;120m"), // BLACK
        RED_BACKGROUND_BRIGHT("\033[0;101m"), // RED
        GREEN_BACKGROUND_BRIGHT("\033[0;102m"), // GREEN
        YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),// YELLOW
        BLUE_BACKGROUND_BRIGHT("\033[0;104m"),// BLUE
        MAGENTA_BACKGROUND_BRIGHT("\033[0;105m"), // MAGENTA
        CYAN_BACKGROUND_BRIGHT("\033[0;106m"),// CYAN
        WHITE_BACKGROUND_BRIGHT("\033[0;107m"); // WHITE

        private final String code;

        public static Color contains(String name) throws IllegalArgumentException {
            try {
                return Color.valueOf(name);
            } catch (IllegalArgumentException i) {
                return null;
            }
        }

        Color(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return code;
        }
    }

    public SysOut() {
    }

    public static void Title(String text) {
        //new SysOut(text, "white", false, null, "white", false, false, true);
        prepareAndPrint(text, "white", false, null, "white", false, false, true, 1);
    }

    public static void Description(String text) {
        prepareAndPrint(text, "CYAN", false, null, null, false, false, true, 2);
    }

    public static void CommandText(String text) {
        prepareAndPrint(text + ": ", "CYAN", false, "black", "CYAN", false, false, true, 0);
    }

    public SysOut(String text) {
        prepareAndPrint(text, null, null, false, null, null, null, null, 0);
    }

    public SysOut(String text, String color) {
        prepareAndPrint(text, color, null, null, null, null, null, null, 0);
    }

    public SysOut(String text, String color, Boolean bold) {
        prepareAndPrint(text, color, bold, null, null, null, null, null, 0);
    }

    public SysOut(String text, String color, Boolean bold, String background) {
        prepareAndPrint(text, color, bold, background, null, null, null, null, 0);
    }

    public SysOut(String text, String color, Boolean bold, String background, String underline) {
        prepareAndPrint(text, color, bold, background, underline, false, false, false, 0);
    }

    public SysOut(String text, String color, Boolean bold, String background, String underline, Boolean high_intensity) {
        prepareAndPrint(text, color, bold, background, underline, high_intensity, null, null, 0);
    }

    public SysOut(String text, String color, Boolean bold, String background, String underline, Boolean high_intensity, Boolean high_intensity_background) {
        prepareAndPrint(text, color, bold, background, underline, high_intensity, high_intensity_background, null, 0);
    }

    public SysOut(String text, String color, Boolean bold, String background, String underline, Boolean high_intensity, Boolean high_intensity_background, Boolean bold_high_intensity) {
        prepareAndPrint(text, color, bold, background, underline, high_intensity, high_intensity_background, bold_high_intensity, 0);
    }

    private static void prepareAndPrint(String text, String color, Boolean bold, String background,
                                        String underline, boolean high_intensity, boolean high_intensity_background,
                                        boolean bold_high_intensity, int newLineCount) {
        if (text != null && !text.isEmpty()) {
            System.out.print(Color.RESET);

            if (color != null) {
                if (!color.isEmpty() && _colors.color(color.toUpperCase())) {
                    String colorPostfix = (bold_high_intensity) ? "_BOLD_BRIGHT" : ((bold) ? "_BOLD" : ((high_intensity) ? "_BRIGHT" : ""));
                    String colorName = color.toUpperCase() + colorPostfix;
                    Color textColor = Color.contains(colorName);

                    if (textColor != null) {
                        System.out.print(textColor);
                    } else {
                        error("Некорректное значение цвета текста");
                    }
                } else {
                    error("Некорректное значение цвета текста");
                }
            }

            if (background != null) {
                if (!background.equals("") &&
                        _colors.contains(background.toUpperCase())) {
                    String colorPostfix = (high_intensity_background != null) ? "_BACKGROUND_BRIGHT" : "_BACKGROUND";
                    String colorName = background.toUpperCase() + colorPostfix;
                    Color bgColor = Color.contains(colorName);

                    if (bgColor != null) {
                        System.out.print(bgColor);
                    } else {
                        error("Некорректное значение цвета фона");
                    }
                } else {
                    error("Некорректное значение цвета фона");
                }
            }

            if (underline != null) {
                if (!underline.equals("") && _colors.contains(underline.toUpperCase())) {
                    Color underlineColor = Color.contains(underline.toUpperCase() + "_UNDERLINED");

                    if (underlineColor != null) {
                        System.out.print(underlineColor);
                    } else {
                        error("Некорректное значение цвета подчеркивания");
                    }
                } else {
                    error("Некорректное значение цвета подчеркивания");
                }
            }

            System.out.println("   " + text + "   " + Color.RESET + "\n".repeat(Math.max(0, newLineCount)));
            System.out.print(Color.RESET);
        } else {
            error("Текст не может быть пуст");
        }
    }

    public static void error(String text) {
        System.out.print(Color.RESET);
        System.out.print(Color.RED_BACKGROUND_BRIGHT);
        System.out.print(Color.WHITE_BOLD_BRIGHT);
        System.out.println(text);
        System.out.print(Color.RESET);
    }

    public static void warm(String text) {
        System.out.print(Color.RESET);
        System.out.print(Color.YELLOW_BACKGROUND_BRIGHT);
        System.out.print(Color.BLACK_BOLD_BRIGHT);
        System.out.println(text);
        System.out.print(Color.RESET);
    }

    public static void notice(String text) {
        System.out.print(Color.RESET);
        System.out.print(Color.BLUE_BACKGROUND_BRIGHT);
        System.out.print(Color.BLACK_BOLD_BRIGHT);
        System.out.println(text);
        System.out.print(Color.RESET);
    }

    public static void command(String command) {
        command(new String[]{command});
    }

    public static void command(String[] commands) {
        System.out.print(Color.RESET);
        String commandsLine = "";
        for (int i = 0; i < commands.length; i++) {
            if (i == 0) {
                commandsLine = "  " + Color.RESET;
            }
            commandsLine = commandsLine + Color.RESET + " " + Color.GREEN_BACKGROUND_BRIGHT + Color.WHITE_BOLD_BRIGHT + " [ " + commands[i] + " ] " + Color.RESET;
            if (i == commands.length - 1) {
                commandsLine = commandsLine + "  " + Color.RESET;
            }
        }
        System.out.println(commandsLine);
        System.out.print(Color.RESET);
    }
}
