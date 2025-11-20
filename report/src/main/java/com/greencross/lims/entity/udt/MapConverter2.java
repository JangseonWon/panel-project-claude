package com.greencross.lims.entity.udt;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class MapConverter2 implements UserType {
	protected static final ObjectMapper OM = Jackson2ObjectMapperBuilder.json()
																		.visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
																		.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
																		.modules(new JavaTimeModule())
																		.propertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
																		.build();
	@SuppressWarnings("unchecked")
	@Override
	public Class<Map<String, Object>> returnedClass() {
		return (Class<Map<String, Object>>)(Class<?>)(Map.class);
	}

	@Override
	public int[] sqlTypes() {
		return new int[]{Types.JAVA_OBJECT};
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return Objects.equals(x, y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
		String json = rs.getString(names[0]);
		if(json == null) return new HashMap<>();
		else try {
			HashMap<String, Object> tmp = OM.readValue(json.getBytes(StandardCharsets.UTF_8), HashMap.class);
			return tmp.entrySet().stream()
					  .filter(entry->entry.getKey()!=null && entry.getValue()!=null)
					  .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
		if(value == null) {
			st.setNull(index, Types.OTHER);
			return;
		} else try {
			String json = OM.writeValueAsString(value);
			st.setObject(index, json, Types.OTHER);
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(value);
			oos.flush();
			oos.close();
			bos.close();
			ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
			return new ObjectInputStream(bais).readObject();
		} catch (ClassNotFoundException | IOException ex) {
			throw new HibernateException(ex);
		}
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return ( Serializable ) value;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}
}