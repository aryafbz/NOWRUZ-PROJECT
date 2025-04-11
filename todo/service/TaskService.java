package todo.service;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Task;
import todo.entity.Users;
import todo.validator.TaskValidator;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import db.Validator;
import java.util.Scanner;

public class TaskService {
    static int addFlag = 0;

    public static void setAsCompleted(int taskId) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setStatus(Task.Status.COMPLETED);

        Database.update(task);
    }

    public static void setAsInProgress(int taskId) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setStatus(Task.Status.IN_PROGRESS);

        Database.update(task);
    }

    public static void setAsNotStarted(int taskId) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setStatus(Task.Status.NOT_STARTED);

        Database.update(task);
    }

    public static void addTask(int userRef) {
        Scanner input = new Scanner(System.in);
        if (addFlag == 0) {
            Database.registerValidator(Task.TASK_ENTITY_CODE, new TaskValidator());
            addFlag++;
        }
        try{
            Task task = new Task();
            System.out.println("Title: ");
            String title = input.nextLine();
            task.setTitle(title);
            Validator v = Database.getValidators(Task.TASK_ENTITY_CODE);
            v.validate(task);

            System.out.println("Description: ");
            String description = input.nextLine();
            task.setDescription(description);

            System.out.println("Type(UNIVERSITY, PERSONAL): ");
            String type = input.nextLine();
            if(type.equals("UNIVERSITY") || type.equals("PERSONAL")) {
                task.setType(Task.Type.valueOf(type));
            }
            else{
                throw new IllegalArgumentException("Invalid task type");
            }

            System.out.println("Due date(yyyy-mm-dd): ");
            String dueDate = input.nextLine();
            task.setDueDate(LocalDate.parse(dueDate));

            task.setUserRef(userRef);

            Database.add(task);
            TaskService.setAsNotStarted(task.id);
            System.out.println("Task saved successfully.");
            System.out.println("User ID: " + task.getUserRef());
            System.out.println("ID: " + task.id);
            System.out.println("Creation date: " + task.getCreationDate());
        } catch (InvalidEntityException e) {
            System.out.println(e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format.");
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    public static void deleteTask(int userRef) {
        Scanner input = new Scanner(System.in);

        System.out.println("ID: ");
        int id = input.nextInt();

        try {
            Task t = (Task) Database.get(id);
            if(t.getUserRef() == userRef) {
                Database.delete(id);
                StepService.deleteStep(id);
                System.out.println("Entity with ID=" + id + " successfully deleted.");
            }
            else{
                throw new IllegalArgumentException("Invalid user reference");
            }
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InvalidEntityException e) {
            System.out.println("Cannot delete entity with ID=" + id + ".");
        }
        catch (ClassCastException e){
            System.out.println("the entity is not a task.");
        }
    }

    public static void updateTask(int userRef) {
        Scanner input = new Scanner(System.in);

        try{
            System.out.println("ID: ");
            int id = input.nextInt();
            input.nextLine();
            Entity entity = Database.get(id);
            Task task = (Task) entity;
            if(task.getUserRef() == userRef) {
                System.out.println("Field(title, description, due date(yyyy-mm-dd), status(NOT_STARTED, COMPLETED, IN_PROGRESS)): , type(UNIVERSITY, PERSONAL): ");
                String field = input.nextLine().toLowerCase();

                System.out.println("New Value: ");
                String newValue = input.nextLine();

                String oldValue = "";
                int flag = 0;

                switch (field) {
                    case "title":
                        oldValue = task.getTitle();
                        task.setTitle(newValue);
                        flag = 1;
                        break;
                    case "description":
                        oldValue = task.getDescription();
                        task.setDescription(newValue);
                        flag = 1;
                        break;
                    case "due date":
                        oldValue = task.getDueDate().toString();
                        task.setDueDate(LocalDate.parse(newValue));
                        flag = 1;
                        break;
                    case "status":
                        oldValue = task.getStatus().toString();
                        if (newValue.equals("COMPLETED") || newValue.equals("IN_PROGRESS") || newValue.equals("NOT_STARTED")) {
                            task.setStatus(Task.Status.valueOf(newValue));
                        } else {
                            throw new IllegalArgumentException("Invalid status value.");
                        }
                        flag = 1;
                        break;
                    case "type":
                        oldValue = task.getType().toString();
                        if (newValue.equals("UNIVERSITY") || newValue.equals("PERSONAL")) {
                            task.setType(Task.Type.valueOf(newValue));
                        } else {
                            throw new IllegalArgumentException("Invalid type value.");
                        }
                        flag = 1;
                        break;
                    default:
                        System.out.println("Invalid field.");
                        break;
                }

                if (flag == 1) {
                    Database.update(task);
                    System.out.println("Task updated successfully.");
                    System.out.println("Field: " + field);
                    System.out.println("Old Value: " + oldValue);
                    System.out.println("New Value: " + newValue);
                    System.out.println("Modification date: " + task.getLastModificationDate());

                    if (task.getStatus() == Task.Status.COMPLETED) {
                        StepService.updateStep(task.id);
                    }
                }
            }
            else{
                throw new IllegalArgumentException("You can't update this task");
            }
        } catch (InvalidEntityException e) {
        System.out.println(e.getMessage());
        } catch (EntityNotFoundException e) {
        System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
        }
        catch (DateTimeParseException e) {
            System.out.println("Error: your entry is not valid for date field");
        }
        catch (ClassCastException e){
            System.out.println("the entity is not a task.");
        }
}

    public static void getTask(int userRef) {
        Scanner input = new Scanner(System.in);

        System.out.println("ID: ");
        int id = input.nextInt();
        try {
            Entity e = Database.get(id);
            Task task = (Task) e;
            if(task.getUserRef() == userRef) {
                System.out.println("ID: " + task.id);
                System.out.println("Title: " + task.getTitle());
                System.out.println("Description: " + task.getDescription());
                System.out.println("Due date: " + task.getDueDate().toString());
                System.out.println("Status: " + task.getStatus());
                System.out.println("Steps: ");

                StepService.getStep(task.id);
            }
            else{
                throw new IllegalArgumentException("Invalid user reference.");
            }
        }
        catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getAllTasks(int userRef) {
        ArrayList<Task> tasks = new  ArrayList<>();
        for(Entity e : Database.getAll(Task.TASK_ENTITY_CODE)){
            if(e instanceof Task){
                tasks.add((Task) e);
            }
        }
        int flag = 0;
        tasks.sort(Comparator.comparing(Task::getDueDate));
        for(Task e : tasks){
            if(e.getUserRef() == userRef){
                System.out.println("ID: " + e.id);
                System.out.println("Title: " + e.getTitle());
                System.out.println("Description: " + e.getDescription());
                System.out.println("Due date: " + e.getDueDate());
                System.out.println("Status: " + e.getStatus());
                System.out.println("Type: " + e.getType());
                System.out.println("Steps: ");
                StepService.getStep(e.id);
                System.out.println();
                flag = 1;
            }
        }
        if(flag == 0) {
            throw new IllegalArgumentException("No tasks found.");
        }
    }

    public static void getIncompleteTasks(int userRef) {
        ArrayList<Task> tasks = new  ArrayList<>();
        for(Entity e : Database.getAll(Task.TASK_ENTITY_CODE)){
            if(e instanceof Task){
                tasks.add((Task) e);
            }
        }
        int flag = 0;
        tasks.sort(Comparator.comparing(Task::getDueDate));
        for(Task e : tasks){
            if(e.getUserRef() == userRef) {
                if (e.getStatus() == Task.Status.IN_PROGRESS || e.getStatus() == Task.Status.NOT_STARTED) {
                    System.out.println("ID: " + e.id);
                    System.out.println("Title: " + e.getTitle());
                    System.out.println("Description: " + e.getDescription());
                    System.out.println("Due date: " + e.getDueDate());
                    System.out.println("Status: " + e.getStatus());
                    System.out.println("Steps: ");
                    StepService.getStep(e.id);
                    System.out.println();
                    flag = 1;
                }
            }
        }
        if(flag == 0) {
            throw new IllegalArgumentException("No tasks found.");
        }
    }
    public static void getCompletedTasks(int userRef) {
        ArrayList<Task> tasks = new  ArrayList<>();
        for(Entity e : Database.getAll(Task.TASK_ENTITY_CODE)){
            if(e instanceof Task){
                tasks.add((Task) e);
            }
        }
        int flag = 0;
        tasks.sort(Comparator.comparing(Task::getDueDate));
        for(Task e : tasks){
            if(e.getUserRef() == userRef) {
                if (e.getStatus() == Task.Status.COMPLETED) {
                    System.out.println("ID: " + e.id);
                    System.out.println("Title: " + e.getTitle());
                    System.out.println("Description: " + e.getDescription());
                    System.out.println("Due date: " + e.getDueDate());
                    System.out.println("Status: " + e.getStatus());
                    System.out.println("Steps: ");
                    StepService.getStep(e.id);
                    System.out.println();
                    flag = 1;
                }
            }
        }
        if(flag == 0) {
            throw new IllegalArgumentException("No tasks found.");
        }
    }

    public static void getUniversityTasks(int userRef) {
        ArrayList<Task> tasks = new  ArrayList<>();
        for(Entity e : Database.getAll(Task.TASK_ENTITY_CODE)){
            if(e instanceof Task){
                tasks.add((Task) e);
            }
        }
        int flag = 0;
        tasks.sort(Comparator.comparing(Task::getDueDate));
        for(Task e : tasks){
            if(e.getUserRef() == userRef) {
                if (e.getType() == Task.Type.UNIVERSITY) {
                    System.out.println("ID: " + e.id);
                    System.out.println("Title: " + e.getTitle());
                    System.out.println("Description: " + e.getDescription());
                    System.out.println("Due date: " + e.getDueDate());
                    System.out.println("Status: " + e.getStatus());
                    System.out.println("Steps: ");
                    StepService.getStep(e.id);
                    System.out.println();
                    flag = 1;
                }
            }
        }
        if(flag == 0) {
            throw new IllegalArgumentException("No tasks found.");
        }
    }

    public static void getPersonalTasks(int userRef) {
        ArrayList<Task> tasks = new  ArrayList<>();
        for(Entity e : Database.getAll(Task.TASK_ENTITY_CODE)){
            if(e instanceof Task){
                tasks.add((Task) e);
            }
        }
        int flag = 0;
        tasks.sort(Comparator.comparing(Task::getDueDate));
        for(Task e : tasks){
            if(e.getUserRef() == userRef) {
                if (e.getType() == Task.Type.PERSONAL) {
                    System.out.println("ID: " + e.id);
                    System.out.println("Title: " + e.getTitle());
                    System.out.println("Description: " + e.getDescription());
                    System.out.println("Due date: " + e.getDueDate());
                    System.out.println("Status: " + e.getStatus());
                    System.out.println("Steps: ");
                    StepService.getStep(e.id);
                    System.out.println();
                    flag = 1;
                }
            }
        }
        if(flag == 0) {
            throw new IllegalArgumentException("No tasks found.");
        }
    }
}


