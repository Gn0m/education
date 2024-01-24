package com.example.demo.blocking;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
public class Task {

    public Task() {
        this.uuid = UUID.randomUUID();
        this.name = "Task Id: ".concat(uuid.toString());
    }

    private UUID uuid;
    private String name;

    @Override
    public String toString() {
        return "Task{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(uuid, task.uuid) && Objects.equals(name, task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name);
    }
}
