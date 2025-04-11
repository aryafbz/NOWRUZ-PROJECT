package todo.entity;

import db.Entity;

public class Users extends Entity {

    private String username;
    private String password;

    public static final int USERS_ENTITY_CODE = 20;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public Users copy(){
        Users copyUser = new Users();
        copyUser.id = this.id;
        copyUser.setUsername(this.getUsername());
        copyUser.setPassword(this.getPassword());

        return copyUser;
    }

    @Override
    public int getEntityCode() {
        return USERS_ENTITY_CODE;
    }
}
