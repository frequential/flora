package me.jasperedits.flora.docs.db;

import lombok.Getter;
import org.mongojack.Id;

public abstract class SimpleDocument implements Model {

    @Id
    @Getter private String id;

}
