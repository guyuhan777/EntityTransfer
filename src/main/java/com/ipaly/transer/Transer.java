package com.ipaly.transer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Transer {

	public <T> T trans(Object from, Class<T> clz) throws TransProcessException, IllegalArgumentException {
		checkNotNull(from);
		checkNotNull(clz);

		T to = null;

		getTrans(from);

		boolean isGlobalStrict = getTrans(clz).isStrict();

		try {
			to = clz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new TransProcessException(e.getMessage());
		}

		HashMap<String, Field> attrsMap = getAttrs(from, isGlobalStrict, true);
		process(attrsMap, from, to, isGlobalStrict);
		return to;
	}

	private void process(HashMap<String, Field> attrsMap, Object from, Object to, boolean isGlobalStrict)
			throws TransProcessException {
		List<Field> toAttrs = Arrays.asList(to.getClass().getDeclaredFields());
		for (Field toField : toAttrs) {
			String transferedName = getTransferedFieldName(toField, isGlobalStrict, false);
			Field fromField = attrsMap.get(transferedName);
			if (fromField != null) {
				if (fromField.getType().equals(toField.getType())) {
					toField.setAccessible(true);
					fromField.setAccessible(true);
					try {
						toField.set(to, fromField.get(from));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new TransProcessException(e.getMessage());
					}
				}
			}
		}
	}

	private String getTransferedFieldName(Field field, boolean isGlobalStrict, boolean isFrom) {
		String fieldName = field.getName();
		if (!isGlobalStrict) {
			FieldAlias fieldTrans = field.getAnnotation(FieldAlias.class);
			if (fieldTrans != null) {
				if (isFrom) {
					String fieldFromAliases = fieldTrans.from();
					fieldName = fieldFromAliases.equals("") ? fieldName : fieldFromAliases;
				} else {
					String fieldToAliases = fieldTrans.to();
					fieldName = fieldToAliases.equals("") ? fieldName : fieldToAliases;
				}
			}
		}
		return fieldName;
	}

	private Trans getTrans(Object src) throws TransProcessException {
		return getTrans(src.getClass());
	}

	private Trans getTrans(Class<? extends Object> clz) throws TransProcessException {
		Trans trans = clz.getAnnotation(Trans.class);
		if (trans == null) {
			throw new TransProcessException("There is no trans annotation in " + clz.getName());
		}
		return trans;
	}

	private void checkNotNull(Object arg) {
		if (arg == null) {
			throw new IllegalArgumentException("argument shouldn't be null");
		}
	}

	private HashMap<String, Field> getAttrs(Object obj, boolean isGlobalStrict, boolean isFrom)
			throws TransProcessException {
		HashMap<String, Field> attrsMap = new HashMap<>();
		Field fields[] = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Field oldField = attrsMap.putIfAbsent(getTransferedFieldName(field, isGlobalStrict, isFrom), field);
			if (oldField != null) {
				throw new TransProcessException("Same field name is not allowed");
			}
		}
		return attrsMap;
	}
}
