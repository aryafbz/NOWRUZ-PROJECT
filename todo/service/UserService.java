package todo.service;

import db.Database;
import db.Entity;
import db.exception.InvalidEntityException;
import todo.entity.Users;

import java.util.Scanner;

public class UserService {

    public static void addUser(){
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter username: ");
        String username = sc.nextLine();
        System.out.println("Enter password: ");
        String password = sc.nextLine();

        Users user = new Users();
        user.setUsername(username);
        user.setPassword(password);
        try{
            for(Entity e : Database.getAll(Users.USERS_ENTITY_CODE)){
                Users u = (Users) e;
                if(u.getUsername().equals(username) && u.getPassword().equals(password)){
                    throw new IllegalArgumentException("a user with the same username and password already exists");
                }
            }
            Database.add(user);
            System.out.println("User added");
        }
        catch(InvalidEntityException e){
            System.out.println(e.getMessage());
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    public static void checkUser(){
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter username: ");
        String username = sc.nextLine();
        System.out.println("Enter password: ");
        String password = sc.nextLine();

        int flag = 0;
        for(Entity e : Database.getAll(Users.USERS_ENTITY_CODE)){
            Users user = (Users) e;
            if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                System.out.println("User " + username + " is logged in");
                program(user.id);
                flag = 1;
            }
        }
        if(flag == 0){
            System.out.println("User not found");
        }
    }

    private static void program(int userRef){
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome\nhere is the list of things you can do\n1.add task\n2.add step\n3.delete task\n4.update task\n5.delete step\n6.update step\n7.get task-by-id\n8.get all-tasks\n9.get incomplete-tasks\n10.get complete-tasks\n11.get university-tasks\n12.get personal-tasks\n13.exit");
        System.out.println();
        String command;
        while (true) {
            System.out.println("Enter your command: ");
            command = input.nextLine().toLowerCase();
            if(command.equals("exit")) {
                System.out.println("Logged out");
                break;
            }
            try {
                switch (command) {
                    case "add task":
                        TaskService.addTask(userRef);
                        break;
                    case "add step":
                        StepService.addStep();
                        break;
                    case "delete task":
                        TaskService.deleteTask(userRef);
                        break;
                    case "update task":
                        TaskService.updateTask(userRef);
                        break;
                    case "delete step":
                        StepService.deleteACertainStep(userRef);
                        break;
                    case "update step":
                        StepService.updateStep2(userRef);
                        break;
                    case "get task-by-id":
                        TaskService.getTask(userRef);
                        break;
                    case "get all-tasks":
                        TaskService.getAllTasks(userRef);
                        break;
                    case "get incomplete-tasks":
                        TaskService.getIncompleteTasks(userRef);
                        break;
                    case "get complete-tasks":
                        TaskService.getCompletedTasks(userRef);
                        break;
                    case "get university-tasks":
                        TaskService.getUniversityTasks(userRef);
                        break;
                    case "get personal-tasks":
                        TaskService.getPersonalTasks(userRef);
                        break;
                    default:
                        System.out.println("Invalid command");

                }
            }
            catch(IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
