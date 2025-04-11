package example;
import db.*;
import db.exception.InvalidEntityException;

public class HumanValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException{
        if (!(entity instanceof Human)){
           throw new IllegalArgumentException("Entity must be of type Human");
        }
        else {
            if(((Human) entity).name.isEmpty())
            {
                throw new InvalidEntityException("name cannot be empty");
            }
            if(((Human) entity).age < 0){
                throw new InvalidEntityException("age cannot be negative");
            }
        }
    }
}
