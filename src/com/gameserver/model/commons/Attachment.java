package com.gameserver.model.commons;

import com.gameserver.model.instances.ItemInstance;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "item_attachments")
public class Attachment {

    @Id
    private String id;

    @DBRef
    private ArrayList<ItemInstance> cargos;

    @DBRef
    private ArrayList<ItemInstance> engines;

    @DBRef
    private ArrayList<ItemInstance> modules;

    @DBRef
    private ArrayList<ItemInstance> weapons;

    // TODO: private ArrayList<ItemInstance> technologies;

    public Attachment(){
        setCargos(new ArrayList<>());
        setEngines(new ArrayList<>());
        setModules(new ArrayList<>());
        setWeapons(new ArrayList<>());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<ItemInstance> getCargos() {
        return cargos;
    }

    private void setCargos(ArrayList<ItemInstance> cargos) {
        this.cargos = cargos;
    }

    public ArrayList<ItemInstance> getEngines() {
        return engines;
    }

    private void setEngines(ArrayList<ItemInstance> engines) {
        this.engines = engines;
    }

    public ArrayList<ItemInstance> getModules() {
        return modules;
    }

    private void setModules(ArrayList<ItemInstance> modules) {
        this.modules = modules;
    }

    public ArrayList<ItemInstance> getWeapons() {
        return weapons;
    }

    private void setWeapons(ArrayList<ItemInstance> weapons) {
        this.weapons = weapons;
    }
}
