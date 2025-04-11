import db.Database;
import todo.entity.Step;
import todo.entity.Task;
import todo.entity.Users;
import todo.serializer.StepSerializer;
import todo.serializer.TaskSerializer;
import todo.serializer.UserSerializer;
import todo.service.UserService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String command;
        Database.registerSerializer(Task.TASK_ENTITY_CODE , new TaskSerializer());
        Database.registerSerializer(Step.STEP_ENTITY_CODE , new StepSerializer());
        Database.registerSerializer(Users.USERS_ENTITY_CODE , new UserSerializer());

        Database.load();
        while(true) {
            System.out.println("1.Login/Sign up\n2.exit");
            System.out.print("> ");
            command = input.nextLine();
            if(command.equals("exit")) {
                Database.save();
                break;
            }
            switch (command) {
                case "login":
                    UserService.checkUser();
                    break;
                case "sign up":
                    UserService.addUser();
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
    }
}