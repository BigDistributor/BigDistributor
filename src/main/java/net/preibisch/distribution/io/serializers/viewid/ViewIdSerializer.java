package net.preibisch.distribution.io.serializers.viewid;

import java.lang.reflect.Type;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import mpicbg.spim.data.sequence.ViewId;

public class ViewIdSerializer implements JsonSerializer<ViewId> {

	@Override
	public JsonElement serialize(ViewId src, Type type, JsonSerializationContext context) {
		ViewIdSerializable vs = new ViewIdSerializable(src);
		return new GsonBuilder().create().toJsonTree(vs);
	}

}
