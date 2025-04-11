package example;
import db.Trackable;
import db.*;

import java.time.LocalDate;
import java.util.*;

public class Document extends Entity implements Trackable {
    public String content;
    public static final int DOCUMENT_ENTITY_CODE = 2;
    private LocalDate creationDate;
    private LocalDate lastModificationDate;

    public Document(String content) {
        this.content = content;
    }

    @Override
    public Document copy() {
        Document copyDocument = new Document(content);
        copyDocument.id = this.id;
        copyDocument.creationDate = this.creationDate;
        copyDocument.lastModificationDate = this.lastModificationDate;

        return copyDocument;
    }

    @Override
    public int getEntityCode(){
        return DOCUMENT_ENTITY_CODE;
    }

    @Override
    public void setCreationDate(LocalDate date){
        this.creationDate = date;
    }

    @Override
    public LocalDate getCreationDate(){
        return this.creationDate;
    }

    @Override
    public void setLastModificationDate(LocalDate date){
        this.lastModificationDate = date;
    }

    @Override
    public LocalDate getLastModificationDate(){
        return this.lastModificationDate;
    }
}
