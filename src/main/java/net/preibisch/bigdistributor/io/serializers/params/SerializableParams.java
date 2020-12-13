package net.preibisch.bigdistributor.io.serializers.params;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.preibisch.bigdistributor.algorithm.errorhandler.logmanager.MyLogger;
import net.preibisch.bigdistributor.io.IOFunctions;
import net.preibisch.bigdistributor.io.serializers.JsonSerializerDeserializer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SerializableParams<T> implements Serializable {
    private static final long serialVersionUID = -3611993522419283535L;

    private final Class<T> type;

    protected static Map<Class, JsonSerializerDeserializer> serializers;
    protected GsonBuilder builder;

    public SerializableParams( Class<T> type) {
        this.type = type;
        init();
		this.builder = new GsonBuilder().serializeSpecialFloatingPointValues().enableComplexMapKeySerialization();
		for (Map.Entry<Class, JsonSerializerDeserializer> entry : serializers.entrySet()) {
			builder.registerTypeHierarchyAdapter(entry.getKey(), entry.getValue());
		}
    }

	protected void init() {
    	serializers = new HashMap<>();
	}

	public Gson getGson() {
        return this.builder.create();
    }

    public void toJson(File file) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
        try (PrintWriter out = new PrintWriter(file)) {
            String json = getGson().toJson(this);
            out.print(json);
            out.flush();
            out.close();
            IOFunctions.println("File saved: " + file.getAbsolutePath() + " | Size: " + file.length());
        } catch (IOException e) {
            MyLogger.log().error(e);
        }
        if (!checkSerialization(file)) {
            throw new JsonIOException("Objects are not equals!");
        }
        ;
    }

    private Boolean checkSerialization(File file) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
        T serializedClass = fromJson(file);
        if (this == serializedClass) {
            return true;
        }
        return false;

    }

    public T fromJson(File file) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
        return getGson().fromJson(new FileReader(file), type);
    }

}
