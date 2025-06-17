package dev.mikan.altairkit.api.file;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OutputStreamUtil {

    private static final LinkOption[] EMPTY_LINK_OPTION_ARRAY = new LinkOption[0];
    private static final OpenOption[] OPEN_OPTIONS_APPEND = {StandardOpenOption.CREATE, StandardOpenOption.APPEND};
    private static final OpenOption[] OPEN_OPTIONS_TRUNCATE = {StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};

    public static OutputStream newOutputStream(File file, boolean append) throws IOException {
        return newOutputStream(Objects.requireNonNull(file, "file").toPath(), append);
    }

    public static OutputStream newOutputStream(Path path, boolean append) throws IOException {
        return newOutputStream(path, EMPTY_LINK_OPTION_ARRAY, append ? OPEN_OPTIONS_APPEND : OPEN_OPTIONS_TRUNCATE);
    }

    private static OutputStream newOutputStream(Path path, LinkOption[] linkOptions, OpenOption... openOptions) throws IOException {
        if (!Files.exists(path, linkOptions)) {
            createParentDirectories(path);
        }

        List<OpenOption> optionsList = new ArrayList<>(Arrays.asList(openOptions));
        return Files.newOutputStream(path, optionsList.toArray(new OpenOption[0]));
    }

    private static void createParentDirectories(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }
}

