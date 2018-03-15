package models;

import org.joda.time.DateTime;

/**
 * Created by akashbhatia on 3/16/18.
 */
public class Task {
    public String id;
    public String name;
    public Long createdAt;
    public Long updatedAt;
    public TaskType type;
    public TaskStatus status;

    public Task(String id, String name, Long createdAt, TaskType type) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.type = type;
        this.status = TaskStatus.PENDING;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Long getTimestamp() {
        return this.createdAt;
    }

    public TaskType getType() {
        return this.type;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setCurrentUpdatedTime() {
        this.updatedAt = new DateTime().getMillis();
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
