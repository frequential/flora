package com.jasperls.flora.yaml;

import com.jasperls.flora.logger.Log;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;

public class Snakelet {
    String path;

    public Snakelet(String path) {
        this.path = path;
    }

    /**
     * @param clazz the class of the instance to be read
     * @param <T> the object the file is representing
     * @return parsed object
     */
    public <T> T read(Class<T> clazz) {
        Yaml yaml = new Yaml(new Constructor(clazz));

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            Log.error(this.getClass(), "Can't read file: \"" + path + "\" doesn't exist");
        }

        return yaml.load(inputStream);
    }

    /**
     * @param object the object to be written
     */
    public void write(Object object) {
        DumperOptions options = getDumperOptions();
        Yaml yaml = new Yaml(options);

        FileWriter writer = null;
        try {
            writer = new FileWriter(path);
        } catch (IOException e) {
            Log.error(this.getClass(), "Can't write file: \"" + path + "\"");
        }

        yaml.dump(object, writer);
    }

    private DumperOptions getDumperOptions() {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        return options;
    }
}