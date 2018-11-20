package com.cldbiz.userportal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaExport.Action;
import org.hibernate.tool.schema.TargetType;
import org.reflections.Reflections;

public class HibernateDDLExporter {

	private static Properties props = new Properties();
	
    public HibernateDDLExporter schemaExport() throws Exception {

    	String fileName = (String) props.get("ddl.fileName");
    	String targetDirectory = (String) props.get("ddl.target.directory");
        File exportFile = createExportFileAndMakeDirectory(fileName, targetDirectory);
        
        if (exportFile.exists()) exportFile.delete();
        exportFile.createNewFile();
        
    	String dialect = props.getProperty("spring.jpa.database-platform");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySetting(AvailableSettings.DIALECT, dialect)
                .applySetting(AvailableSettings.PHYSICAL_NAMING_STRATEGY, com.cldbiz.userportal.hibernate.CustomPhysicalNamingStrategy.class)
                .build();
 
        MetadataImplementor metadata = (MetadataImplementor) mapAnnotatedClasses(serviceRegistry).buildMetadata();
        
        SchemaExport schemaExport = new SchemaExport();
        
        EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.SCRIPT);
        schemaExport.setOutputFile(exportFile.getAbsolutePath());
        schemaExport.setDelimiter(";");
        schemaExport.setFormat(true);
        
        schemaExport.create(targetTypes, metadata);
        
        ((StandardServiceRegistryImpl) serviceRegistry).destroy();
 
        System.out.println(exportFile.getAbsolutePath());
 
        return this;
 
    }
 
    private File createExportFileAndMakeDirectory(String fileName, String targetDirectory) {
        File exportFile;
        if (targetDirectory != null) {
            final File directory = new File(targetDirectory);
            directory.mkdirs();
            exportFile = new File(directory, fileName);
        } else {
            exportFile = new File(fileName);
        }
        return exportFile;
    }
 
    private MetadataSources mapAnnotatedClasses(ServiceRegistry serviceRegistry) {
        MetadataSources sources = new MetadataSources(serviceRegistry);
 
    	String ddlEntityPackages = (String) props.get("ddl.entity.packages");
		List<String> entityPackages = Arrays.asList(ddlEntityPackages.split("\\s*,\\s*"));
    	if (entityPackages == null || entityPackages.size() == 0) {
            System.out.println("Not packages selected");
            System.exit(0);
        }
    	
        final Reflections reflections = new Reflections((Object) entityPackages);
        for (final Class<?> mappedSuperClass : reflections.getTypesAnnotatedWith(MappedSuperclass.class)) {
        	sources.addAnnotatedClass(mappedSuperClass);
        	System.out.println("Mapped = " + mappedSuperClass.getName());
        }
        for (final Class<?> entityClass : reflections.getTypesAnnotatedWith(Entity.class)) {
        	sources.addAnnotatedClass(entityClass);
        	System.out.println("Mapped = " + entityClass.getName());
        }
        
        return sources;
    }
 
    private static void setProperties() {
    	try {
    		InputStream input = HibernateDDLExporter.class.getClassLoader().getResourceAsStream("application.properties");
    		props.load(input);
    		input.close();
    		
    		String activeProfiles = (String) props.get("spring.profiles.active");
    		List<String> profiles = Arrays.asList(activeProfiles.split("\\s*,\\s*"));
    		for(String profile: profiles) {
    			input = HibernateDDLExporter.class.getClassLoader().getResourceAsStream("application-" + profile + ".properties");
    			props.load(input);
        		input.close();
    		}
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } 
    }

    public static HibernateDDLExporter instance() {
        return new HibernateDDLExporter();
    }
    
    public static void main(String[] args) throws Exception {
    	setProperties();
    	
    	HibernateDDLExporter exporter = HibernateDDLExporter.instance();
    	exporter.schemaExport();
    }

}
