package me.rubataga.everyhunt.utils;

import com.google.common.primitives.Primitives;
import org.bukkit.plugin.java.JavaPlugin;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class EmbeddedYamlEditor {

    // constants
    private static final Yaml YAML = new Yaml();
    private final JavaPlugin PLUGIN;
    private final Class<?> OWNER_CLASS;
    private final Map<String, Field> FIELDS = new LinkedHashMap<>();
    private final String EMBEDDED_RESOURCE_NAME;
    private final Map<String, Object> EMBEDDED_VALUE_MAP;

    // variables
    private String loadedResourceName = "";

    // settings
    private final String valueFormat = "%s: %s";

    public EmbeddedYamlEditor(Class<?> ownerClass, JavaPlugin plugin, String EMBEDDED_RESOURCE_NAME, String DEFAULT_RESOURCE_NAME) {
        this.PLUGIN = plugin;
        this.OWNER_CLASS = ownerClass;
        for(Field f : ownerClass.getFields()){
            this.FIELDS.put(f.getName(),f);
        }
        this.EMBEDDED_RESOURCE_NAME = EMBEDDED_RESOURCE_NAME;
        this.EMBEDDED_VALUE_MAP = YAML.load(getEmbeddedResource());
        try{
            load(DEFAULT_RESOURCE_NAME);
        } catch (FileNotFoundException e){
            loadEmbed();
        }
    }

    public Map<String,Field> getKeyFields() {
        return FIELDS;
    }

//    public String getLoadedResourceName() {
//        return loadedResourceName;
//    }
//
//    public Map<String, Object> getEmbeddedValueMap() {
//        return EMBEDDED_VALUE_MAP;
//    }
//
//    public Map<String, Object> getDefaultValueMap() {
//        return defaultValueMap;
//    }
//
//    public Map<String, Object> getLoadedValueMap() {
//        return loadedValueMap;
//    }
//
//    public void setLoadDefaultBeforeEmbedded(boolean b){
//        loadDefaultBeforeEmbedded = b;
//    }
//
//    public void setValueFormat(String s){
//        valueFormat = s;
//    }

    public void load(String fileName) throws FileNotFoundException {
        if(fileName==null){
            throw new FileNotFoundException("Filename cannot be null!");
        }
        if (loadedResourceName.equalsIgnoreCase(fileName)) {
            return;
        }
        Map<String,Object> loadedValueMap = YAML.load(getInputStream(fileName));
        setFieldsToValues(loadedValueMap);
        loadedResourceName = fileName;
    }

    public void loadEmbed() {
        Map<String,Object> loadedValueMap = YAML.load(getEmbeddedResource());
        setFieldsToValues(loadedValueMap);
        loadedResourceName = EMBEDDED_RESOURCE_NAME;
}

    public InputStream getEmbeddedResource() {
        return PLUGIN.getResource(EMBEDDED_RESOURCE_NAME);
    }

    public InputStream getInputStream(String fileName) throws FileNotFoundException {
        InputStream inputStream = null;
        if (fileName.equalsIgnoreCase("$embedded") || fileName.equalsIgnoreCase(EMBEDDED_RESOURCE_NAME)) {
            inputStream = getEmbeddedResource();
        } else {
            File configFile = new File(PLUGIN.getDataFolder(), fileName);
            if (configFile.exists()) {
                try {
                    inputStream = configFile.toURI().toURL().openStream();
                } catch (IOException ignored) {
                }
            }
            if (inputStream == null) {
                throw new FileNotFoundException("Config file " + fileName + " not found!");
            }
        }
        loadedResourceName = fileName;
        return inputStream;
    }

    private void setFieldsToValues(Map<String,Object> loadedValueMap) {
        for(String key : FIELDS.keySet()){
            Field f = FIELDS.get(key);
            if(f!=null){
                Object obj = getSafeValueFromMap(key,f,loadedValueMap);
                Debugger.send(f.getName() + ": " + obj);
                try {
                    f.set(OWNER_CLASS, obj);
                } catch(IllegalAccessException ignored) {
                }
            }
        }
    }

    public void setFieldToValue(String key, Object value) throws IllegalAccessException {
        Field f = FIELDS.get(key);
        if(f!=null){
            f.set(OWNER_CLASS,value);
        }
    }

    private Object getSafeValueFromMap(String key, Field f, Map<String,Object> loadedValueMap){
        Object obj = loadedValueMap.get(key);
        if(obj==null || !Primitives.unwrap(f.getType()).isAssignableFrom(Primitives.unwrap(obj.getClass()))){
            obj = EMBEDDED_VALUE_MAP.get(key);
            //            if(obj!=null && f.getType().isPrimitive()){
//                if(!f.getType().isAssignableFrom(Primitives.unwrap(obj.getClass()))){
//                    obj = EMBEDDED_VALUE_MAP.get(key);
//                }
//            }
//            if(obj==null){
//                obj = EMBEDDED_VALUE_MAP.get(key);
//            }
//            if(f.getType().isPrimitive()){
//                if(!f.getType().isAssignableFrom(Primitives.unwrap(obj.getClass()))){
//                    obj = EMBEDDED_VALUE_MAP.get(key);
//                }
//            }
//
//            if(obj==null){
//                Debugger.send("Object is null: " + (true));
//            } else {
//                if(f.getType().isPrimitive()){
//                    if(!f.getType().isAssignableFrom(Primitives.unwrap(obj.getClass()))){
//                        Debugger.send("Object is null: " + (false));
//                        Debugger.send("Object class: " + obj.getClass().getName());
//                        Debugger.send("Field type: " + f.getType().getName());
//                        Debugger.send("Object class is field type: " + false);
//                        Debugger.send("Using embedded value!");
//                    }
//                }
//
//            }
//            obj = EMBEDDED_VALUE_MAP.get(key);
        }
        Debugger.send(f.getName() + ": " + obj);
        return obj;
    }

//    // returns the value of a GameCfg public field
//    public Object getValue(String key) throws NoSuchFieldException, IllegalAccessException {
//        Field field = OWNER_CLASS.getField(key);
//        return field.get(OWNER_CLASS);
//    }

//    public Object getaValue(String key) {
//        Object value;
//        if(FIELDS.containsKey(key)){
//            Field f = FIELDS.get(key);
//            try {
//                value = f.get(OWNER_CLASS);
//                return value;
//            }
//            catch(IllegalAccessException ignored){
//            }
//        }
//        return getSafeValueFromMap(key);
//    }
//
//    // returns a formatted string of a value accessible by getValue()
//    public String getFormattedValue(String key) throws NoSuchFieldException, IllegalAccessException {
//        return String.format(valueFormat,key,getValue(key));
//    }

    public void test(){
        System.out.println("test");
    }

}