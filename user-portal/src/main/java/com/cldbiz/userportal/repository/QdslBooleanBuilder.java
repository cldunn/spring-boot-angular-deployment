package com.cldbiz.userportal.repository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.ParameterizedExpression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.TimePath;

public class QdslBooleanBuilder<E, D> {

	BooleanBuilder builder;
	
	public QdslBooleanBuilder(BooleanBuilder builder) {
		this.builder = builder;
	}

	private class DynPredicate<T> {
		String op = "eq";
	    Class[] types = { Object.class };
	    
	    public DynPredicate(String op) {
			this.op = op;
		}
	    
		public DynPredicate(String op, Class... types) {
			this.op = op;
			this.types = types;
		}
		
		private Field getField(D dto, String name) throws NoSuchFieldException {
			Field field = null;
			for (Class<?> c = dto.getClass(); c != null; c = c.getSuperclass()) {
				try {
					field = c.getDeclaredField(name);
					break;
				}
				catch (NoSuchFieldException nsfe) {}
		    }

			if (field != null) {
				return field;
			}
			else {
				throw new NoSuchFieldException();
			}
		}
		
		private Method getMethod(T entPath, String name, Class<?>... params) throws NoSuchMethodException {
			Method method = null;
			for (Class<?> c = entPath.getClass(); c != null; c = c.getSuperclass()) {
				try {
					// method = c.getDeclaredMethod(name, params[0]);
					method = c.getMethod(name, params[0]);
					break;
				}
				catch (NoSuchMethodException nsfe) {}
		    }

			if (method != null) {
				return method;
			}
			else {
				throw new NoSuchMethodException();
			}
		}

		@SuppressWarnings("unchecked")
		public void addPredicate(E entity, Field entFld, D dto) {
			try {
				String name = entFld.getName();
				T entPath = (T) entFld.get(entity);
				// check entPath not transient
				
				Field dtoFld = getField(dto, name);
				if(dtoFld.getType().equals(((Expression) entPath).getType())) {
					dtoFld.setAccessible(true);
					Object value = dtoFld.get(dto);
					
					if (value != null) {
						// Method method = getMethod(entPath, this.op, dtoFld.getType());
						// Method method = StringPath.class.getMethod(this.op, Object.class);
						Method method = entPath.getClass().getMethod(this.op, this.types);
						Predicate result = (Predicate) method.invoke(entPath, value);
						
						QdslBooleanBuilder.this.builder.and((Predicate) method.invoke(entPath, value));
					}
				}
			}
			catch(NoSuchMethodException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException | InvocationTargetException nsfe) {
			// catch(NoSuchFieldException | IllegalAccessException | IllegalArgumentException nsfe) {
				nsfe.printStackTrace();
			} 
		}

	}
	
	DynPredicate<StringPath> stringPathEqualsIgnoreCase = new DynPredicate<StringPath>("equalsIgnoreCase", String.class);
	DynPredicate<StringPath> stringPathContainsIgnoreCase = new DynPredicate<StringPath>("containsIgnoreCase", String.class);
	DynPredicate<NumberPath> numberPathEq = new DynPredicate<NumberPath>("eq");
	DynPredicate<BooleanPath> booleanPathEq = new DynPredicate<BooleanPath>("eq");
	DynPredicate<ComparablePath> comparablePathEq = new DynPredicate<ComparablePath>("eq");
	DynPredicate<DatePath> datePathEq = new DynPredicate<DatePath>("eq");
	DynPredicate<TimePath> timePathEq = new DynPredicate<TimePath>("eq");
	DynPredicate<DateTimePath> dateTimePathEq = new DynPredicate<DateTimePath>("eq");
	
	public Predicate findPredicate(E entity, D dto, Predicate... predicates) {
		List<Predicate> predicateLst = Arrays.asList(predicates).stream().filter(p -> p != null).collect(Collectors.toList());
		List<Field> entFields = Arrays.asList(entity.getClass().getDeclaredFields());
		
		for(Field entFld: entFields) {
			entFld.setAccessible(true);
			
			String entFldName = entFld.getName();
			Predicate entFldPred = predicateLst.stream().filter(p -> {
				Object pathName = ((Path) ((Operation) p).getArg(0)).getMetadata().getElement();
				return entFldName.equals(pathName.toString());
			}).findFirst().orElse(null);
			
			if (entFldPred != null) {
				this.builder.and(entFldPred);
			}
			else if (entFld.getType().equals(StringPath.class)) {
				stringPathEqualsIgnoreCase.addPredicate(entity, entFld, dto);
            }
			else if (entFld.getType().equals(NumberPath.class)) {
				numberPathEq.addPredicate(entity, entFld, dto);
            }
			else if (entFld.getType().equals(BooleanPath.class)) {
				booleanPathEq.addPredicate(entity, entFld, dto);
            }
			else if (entFld.getType().equals(ComparablePath.class)) {
				comparablePathEq.addPredicate(entity, entFld, dto);
            }
			else if (entFld.getType().equals(DatePath.class)) {
				datePathEq.addPredicate(entity, entFld, dto);
            }
			else if (entFld.getType().equals(TimePath.class)) {
				timePathEq.addPredicate(entity, entFld, dto);
            }
			else if (entFld.getType().equals(DateTimePath.class)) {
				dateTimePathEq.addPredicate(entity, entFld, dto);
            }
		}
		
		return builder;
	}
	
	/*
	private void asStringPath(E entity, Field entFld, D dto) {
		String name = entFld.getName();
		try {
			Field dtoFld = dto.getClass().getDeclaredField(name);
			if(dtoFld.getType().equals(String.class)){
				dtoFld.setAccessible(true);
				String strValue = (String) dtoFld.get(dto);
				StringPath entStrPath = (StringPath) entFld.get(entity);
				if (strValue != null) {
					this.builder.and(entStrPath.equalsIgnoreCase(strValue));
				}
			}
		}
		catch(NoSuchFieldException | IllegalAccessException nsfe) {}
	}
	
	private void asNumberPath(E entity, Field entFld, D dto) {
		try {
			String name = entFld.getName();
			NumberPath entNbrPath = (NumberPath) entFld.get(entity);
			
			Field dtoFld = dto.getClass().getDeclaredField(name);
			if(dtoFld.getType().equals(entNbrPath.getType())){
				dtoFld.setAccessible(true);
				Object value = dtoFld.get(dto);
				if (value != null) {
					this.builder.and(entNbrPath.eq(value));
				}
			}
		}
		catch(NoSuchFieldException | IllegalAccessException nsfe) {}
	}

	private void asComparablePath(E entity, Field entFld, D dto) {
		try {
			String name = entFld.getName();
			ComparablePath entCmpPath = (ComparablePath) entFld.get(entity);
			
			Field dtoFld = dto.getClass().getDeclaredField(name);
			if(dtoFld.getType().equals(entCmpPath.getType())){
				dtoFld.setAccessible(true);
				Object value = dtoFld.get(dto);
				if (value != null) {
					this.builder.and(entCmpPath.eq(value));
				}
			}
		}
		catch(NoSuchFieldException | IllegalAccessException nsfe) {}
	}
	*/
}
