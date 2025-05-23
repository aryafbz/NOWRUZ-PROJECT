package todo.service;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;
import todo.validator.StepValidator;
import java.util.Scanner;

public class StepService {
    static int addFlag = 0;

    public static void updateStep(int taskRef) throws InvalidEntityException {
        for(Entity e: Database.getAll(Step.STEP_ENTITY_CODE)){
            Step step = (Step)e;
            if(step.getTaskRef() == taskRef){
                step.setStatus(Step.Status.COMPLETED);
                Database.update(step);
            }
        }
    }

    public static void deleteStep(int id) throws InvalidEntityException {
        for(Entity e : Database.getAll(Step.STEP_ENTITY_CODE)){
            if(e instanceof Step){
                if(((Step)e).getTaskRef() == id){
                    Database.delete(e.id);
                }
            }
        }
    }

    public static void deleteACertainStep(int userRef)  {
        Scanner sc = new Scanner(System.in);

        System.out.println("Task ID: ");
        int taskRef= sc.nextInt();
        System.out.println("Step ID: ");
        int id= sc.nextInt();
        try {
            Task task = (Task) Database.get(taskRef);
            if(task.getUserRef() == userRef){
                Step step = (Step) Database.get(id);
                if(step.getTaskRef() == taskRef){
                    Database.delete(id);
                }
                else {
                    throw new EntityNotFoundException();
                }
            }
            else{
                throw new EntityNotFoundException();
            }
        }
        catch(EntityNotFoundException e){
                System.out.println(e.getMessage());
            }
        catch(ClassCastException e){
                System.out.println("the entity is not a step");
            }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    public static void updateStep2(int userRef){
        Scanner input = new Scanner(System.in);

        System.out.println("ID: ");
        int id = input.nextInt();
        input.nextLine();
        try {
            Entity e = Database.get(id);
            Step step = (Step) e;
            if(((Task) Database.get(step.getTaskRef())).getUserRef() == userRef) {

                System.out.println("Field(title, status(NOT_STARTED , COMPLETED))): ");
                String field = input.nextLine().toLowerCase();
                System.out.println("New Value: ");
                String value = input.nextLine();


                String oldValue = "";
                int flag = 0;
                switch (field) {
                    case "title":
                        oldValue = step.getTitle();
                        step.setTitle(value);
                        flag = 1;
                        break;
                    case "status":
                        oldValue = step.getStatus().toString();
                        if (value.equals("COMPLETED") || value.equals("NOT_STARTED")) {
                            step.setStatus(Step.Status.valueOf(value));
                        } else {
                            throw new IllegalArgumentException("invalid status value");
                        }
                        flag = 1;
                        break;
                    default:
                        System.out.println("Invalid field");
                        break;
                }
                if (flag == 1) {
                    Database.update(e);
                    StepService.checkSteps(step.getTaskRef());
                    System.out.println("Step updated successfully.");
                    System.out.println("Field: " + field);
                    System.out.println("Old Value: " + oldValue);
                    System.out.println("New Value: " + value);
                    System.out.println("Modification date: " + step.getLastModificationDate());
                }
            }
            else{
                throw new IllegalArgumentException("you can't modify this step");
            }
        }
        catch (InvalidEntityException e){
            System.out.println(e.getMessage());
        }
        catch (EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(ClassCastException e){
            System.out.println("the entity is not a step");
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }


    public static void addStep(){
        Scanner input = new Scanner(System.in);
        if(addFlag == 0) {
            Database.registerValidator(Step.STEP_ENTITY_CODE, new StepValidator());
            addFlag = 1;
        }

        System.out.println("TaskID: ");
        int taskId = input.nextInt();
        input.nextLine();
        System.out.println("Title: ");
        String title = input.nextLine();

        try{
            Step step = new Step();
            step.setTaskRef(taskId);
            step.setTitle(title);
            step.setStatus(Step.Status.NOT_STARTED);

            Database.add(step);

            System.out.println("Step saved successfully.");
            System.out.println("ID: " + step.id);
            System.out.println("Creation Date: " + step.getCreationDate());
        }
        catch (EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(InvalidEntityException e){
            System.out.println(e.getMessage());
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    public static void checkSteps(int taskRef){
        int counter = 0;
        int counter2 = 0;
        for(Entity e: Database.getAll(Step.STEP_ENTITY_CODE)){
            counter++;
            Step step = (Step)e;
            if(step.getTaskRef() == taskRef) {
                if (step.getStatus() == Step.Status.COMPLETED) {
                    counter2++;
                }

                if (counter == counter2) {
                    try {
                        TaskService.setAsCompleted(taskRef);

                    } catch (InvalidEntityException e1) {
                        System.out.println(e1.getMessage());
                    }
                } else {
                    try {
                        TaskService.setAsInProgress(taskRef);

                    } catch (InvalidEntityException e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
        }
        if(counter2 == 0){
            try {
                TaskService.setAsNotStarted(taskRef);
            }
            catch (InvalidEntityException e2) {
                System.out.println(e2.getMessage());
            }
        }
    }

    public static void getStep(int taskRef){
        int flag = 0;
        for(Entity e: Database.getAll(Step.STEP_ENTITY_CODE)){
            Step step = (Step) e;
            if(step.getTaskRef() == taskRef){
                System.out.println("  + ID: " + step.id);
                System.out.println("    Title: " + step.getTitle());
                System.out.println("    Status: " + step.getStatus());
                flag = 1;
            }
        }
        if(flag == 0){
            System.out.println("this task doesn't have any steps");
        }
    }
}
