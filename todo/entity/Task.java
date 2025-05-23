package todo.entity;

import db.*;

import java.time.LocalDate;

public class Task extends Entity implements Trackable {
    public enum Type{
        UNIVERSITY, PERSONAL
    }
    public enum Status {
        COMPLETED, IN_PROGRESS, NOT_STARTED
    }

    protected String title;
    protected String description;
    protected LocalDate dueDate;
    protected Status status;
    protected Type type;
    protected int userRef;

    public static final int TASK_ENTITY_CODE = 13;
    private LocalDate creationDate;
    private LocalDate LastModificationDate;

    @Override
    public Task copy()
    {
        Task copyTask = new Task();
        copyTask.id = this.id;
        copyTask.title = this.title;
        copyTask.description = this.description;
        copyTask.dueDate = this.dueDate;
        copyTask.status = this.status;
        copyTask.type = this.type;
        copyTask.creationDate = this.creationDate;
        copyTask.LastModificationDate = this.LastModificationDate;
        copyTask.userRef = this.userRef;


        return copyTask;
    }

    @Override
    public int getEntityCode() {
        return TASK_ENTITY_CODE;
    }

    @Override
    public void setCreationDate(LocalDate date) {
        this.creationDate = date;
    }

    @Override
    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    @Override
    public void setLastModificationDate(LocalDate date) {
        this.LastModificationDate = date;
    }

    @Override
    public LocalDate getLastModificationDate() {
        return this.LastModificationDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public void setUserRef(int userRef) {
        this.userRef = userRef;
    }
    public int getUserRef() {
        return this.userRef;
    }

}
