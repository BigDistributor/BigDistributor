package net.preibisch.distribution.io.serializers.viewid;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import mpicbg.spim.data.sequence.ViewId;

public class ViewIdDeserializer implements JsonDeserializer<ViewId>{

	@Override
	public ViewId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return new Gson().fromJson(json, ViewIdSerializable.class).toViewId();
	}

}
