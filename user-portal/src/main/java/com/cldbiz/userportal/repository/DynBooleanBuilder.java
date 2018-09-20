package com.cldbiz.userportal.repository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.TimePath;

public class DynBooleanBuilder<E, D> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynBooleanBuilder.class);
    
    private BooleanBuilder builder;
    
    private class DtoFld {
        private Class<?> type;
        private Object value;
        
        public DtoFld(Class<?> type, Object value) {
            this.type = type;
            this.value = value;
        }
        
        public Class<?> getType() {
            return this.type;
        }

        public Object getValue() {
            return this.value;
        }
    }

    private class DynPredicate<T> {
        String op = "eq";
        Class[] types = { Object.class };
        
        public DynPredicate() {
        }
        
        public DynPredicate(String op) {
            this.op = op;
        }
        
        public DynPredicate(String op, Class... types) {
            this.op = op;
            this.types = types;
        }

        public void addPredicate(E entity, Field entFld, DtoFld dtoFld) {
            try {
                T entPath = (T) entFld.get(entity);
                if(dtoFld.getType().equals(((Expression) entPath).getType())) {
                    Method method = entPath.getClass().getMethod(this.op, this.types);
                    DynBooleanBuilder.this.builder.and((Predicate) method.invoke(entPath, dtoFld.getValue()));
                }
            } 
            catch(NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException nsme) {
                LOGGER.debug(nsme.getMessage());
                LOGGER.debug(ExceptionUtils.getStackTrace(nsme));
            }           
        }
    }
    
    private Object getValue(Field f, D dto) {
        Object value = null;
        
        try {
            value = FieldUtils.readField(f, dto, true);
        }
        catch(IllegalArgumentException | IllegalAccessException ex) {}
        
        return value;
    }
    
    private DynPredicate<StringPath> stringPathEqualsIgnoreCase = new DynPredicate<StringPath>("equalsIgnoreCase", String.class);
    private DynPredicate<StringPath> stringPathContainsIgnoreCase = new DynPredicate<StringPath>("containsIgnoreCase", String.class);
    private DynPredicate<NumberPath> numberPathEq = new DynPredicate<NumberPath>("eq");
    private DynPredicate<BooleanPath> booleanPathEq = new DynPredicate<BooleanPath>("eq");
    private DynPredicate<ComparablePath> comparablePathEq = new DynPredicate<ComparablePath>("eq");
    private DynPredicate<DatePath> datePathEq = new DynPredicate<DatePath>("eq");
    private DynPredicate<TimePath> timePathEq = new DynPredicate<TimePath>("eq");
    private DynPredicate<DateTimePath> dateTimePathEq = new DynPredicate<DateTimePath>("eq");
    
    public DynBooleanBuilder(BooleanBuilder builder) {
        this.builder = builder;
    }

    public Predicate findPredicate(E entity, D dto, Predicate... predicates) {
        Map<String, Predicate> predicateMap = new HashMap<String, Predicate>();
        for(Predicate predicate: predicates) {
            Object pathName = ((Path) ((Operation) predicate).getArg(0)).getMetadata().getElement();
            predicateMap.put(pathName.toString(), predicate);
        }

        List<Field> dtoFields = FieldUtils.getAllFieldsList(dto.getClass());
        Map<String, DtoFld> dtoFldMap = new HashMap<String, DtoFld>();
        for(Field dtoField: dtoFields) {
            Object dtoVal = getValue(dtoField, dto);
            if (dtoVal != null) {
                dtoFldMap.put(dtoField.getName(), new DtoFld(dtoField.getType(), dtoVal));
            }
        }
        
        List<Field> entFlds = FieldUtils.getAllFieldsList(entity.getClass());
        for(Field entFld: entFlds) {
            String name = entFld.getName();
            Predicate predicate = predicateMap.get(name);
            if (predicate != null) {
                this.builder.and(predicate);
            }
            else if (dtoFldMap.containsKey(name)) {
                DtoFld dtoFld =  dtoFldMap.get(name);
                if (entFld.getType().equals(StringPath.class)) {
                    stringPathEqualsIgnoreCase.addPredicate(entity, entFld, dtoFld);
                }
                else if (entFld.getType().equals(NumberPath.class)) {
                    numberPathEq.addPredicate(entity, entFld, dtoFld);
                }
                else if (entFld.getType().equals(BooleanPath.class)) {
                    booleanPathEq.addPredicate(entity, entFld, dtoFld);
                }
                else if (entFld.getType().equals(ComparablePath.class)) {
                    comparablePathEq.addPredicate(entity, entFld, dtoFld);
                }
                else if (entFld.getType().equals(DatePath.class)) {
                    datePathEq.addPredicate(entity, entFld, dtoFld);
                }
                else if (entFld.getType().equals(TimePath.class)) {
                    timePathEq.addPredicate(entity, entFld, dtoFld);
                }
                else if (entFld.getType().equals(DateTimePath.class)) {
                    dateTimePathEq.addPredicate(entity, entFld, dtoFld);
                }
            }
        }
        
        return builder;
    }
}
