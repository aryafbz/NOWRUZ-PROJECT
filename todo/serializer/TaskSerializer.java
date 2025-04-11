package todo.serializer;

import db.Entity;
import db.Serializer;
import todo.entity.Task;
import java.time.LocalDate;

public class TaskSerializer implements Serializer {
    @Override
    public String serialize(Entity e){
        if(e instanceof Task){
            Task task = (Task) e;
            return "Task" + "/" + task.id + "/" + task.getUserRef() + "/" + task.getTitle() + "/" + task.getDescription() + "/" + task.getDueDate() + "/" + task.getStatus() + "/" + task.getType() + "/" + task.getCreationDate() + "/" + task.getLastModificationDate();
        }
        throw new IllegalArgumentException("You should provide a Task");
    }

    @Override
    public Entity deserialize(String s){
            String[] split = s.split("/");
            Task task = new Task();
            task.id = Integer.parseInt(split[1]);
            task.setUserRef(Integer.parseInt(split[2]));
            task.setTitle(split[3]);
            task.setDescription(split[4]);
            task.setDueDate(LocalDate.parse(split[5]));
            switch(split[6]){
                case"NOT_STARTED":
                    task.setStatus(Task.Status.NOT_STARTED);
                    break;
                case "COMPLETED":
                    task.setStatus(Task.Status.COMPLETED);
                    break;
                case "IN_PROGRESS":
                    task.setStatus(Task.Status.IN_PROGRESS);
                    break;
            }
            switch(split[7]){
                case "UNIVERSITY":
                    task.setType(Task.Type.UNIVERSITY);
                    break;
                case "PERSONAL":
                    task.setType(Task.Type.PERSONAL);
                    break;
            }
            task.setCreationDate(LocalDate.parse(split[8]));
            task.setLastModificationDate(LocalDate.parse(split[9]));
            return task;
    }
}
