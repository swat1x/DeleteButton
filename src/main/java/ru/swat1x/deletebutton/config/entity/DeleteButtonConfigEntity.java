package ru.swat1x.deletebutton.config.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteButtonConfigEntity {

  boolean enabled = true;

  String material = "minecraft:barrier";

}
