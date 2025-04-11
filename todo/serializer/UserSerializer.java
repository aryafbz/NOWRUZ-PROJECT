package todo.serializer;

import db.Entity;
import db.Serializer;
import todo.entity.Users;

public class UserSerializer implements Serializer {

    @Override
    public String serialize(Entity e){
        if(e instanceof Users){
            Users u = (Users)e;
            return "User" + "/" + u.id + "/" + u.getUsername() + "/" + u.getPassword();
        }
        throw new IllegalArgumentException("you should provide a user");
    }

    @Override
    public Entity deserialize(String s){
        String[] split = s.split("/");
        Users u = new Users();
        u.id = Integer.parseInt(split[1]);
        u.setUsername(split[2]);
        u.setPassword(split[3]);

        return u;
    }
}
