package department_employee_aroundmapping;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.IdentityHashMap;
import java.util.Map;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.TargetType;
import org.mapstruct.factory.Mappers;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Mapper
//(
//		collectionMappingStrategy = ADDER_PREFERRED,
//    nullValueCheckStrategy = ALWAYS
//)
@Slf4j
public abstract class MapStructMapper
{
	public static MapStructMapper INSTANCE = Mappers.getMapper(MapStructMapper.class);

	protected CycleTracking context = new CycleTracking();

	abstract DepartmentEntity map(DepartmentDTO    department/*, @Context CycleTracking context*/);
	abstract DepartmentDTO    map(DepartmentEntity department/*, @Context CycleTracking context*/);

	abstract EmployeeEntity map(EmployeeDTO    employee/*, @Context CycleTracking context*/);
	abstract EmployeeDTO    map(EmployeeEntity employee/*, @Context CycleTracking context*/);

	@BeforeMapping protected void beforeMapping(DepartmentDTO dto, @MappingTarget DepartmentEntity entity)
	{
		log.debug("on enter - dto: {} entity: {}", dto, entity);
		entity.beforeMapping(dto, null);
		log.debug("on leave - dto: {} entity: {}", dto, entity);
	}

	@AfterMapping protected void afterMapping(DepartmentDTO dto, @MappingTarget DepartmentEntity entity)
	{
		log.debug("on enter - dto: {} entity: {}", dto, entity);
		entity.afterMapping(dto, this);
		log.debug("on leave - dto: {} entity: {}", dto, entity);
	}

	@BeforeMapping protected void beforeMapping(DepartmentEntity entity, @MappingTarget DepartmentDTO dto)
	{
		log.debug("on enter - dto: {} entity: {}", dto, entity);
		dto.beforeMapping(entity, null);
		log.debug("on leave - dto: {} entity: {}", dto, entity);
	}

	@AfterMapping protected void afterMapping(DepartmentEntity entity, @MappingTarget DepartmentDTO dto)
	{
		log.debug("on enter - dto: {} entity: {}", dto, entity);
		dto.afterMapping(entity, this);
		log.debug("on leave - dto: {} entity: {}", dto, entity);
	}

	/** used to handle cyclic dependencies in mapstruct mappings */
	@ToString public static class CycleTracking
	{
		private Map<Object, Object> knownInstances = new IdentityHashMap<Object, Object>();

		@SuppressWarnings("unchecked")
		@BeforeMapping
		public <T> T getMappedInstance(Object source, @TargetType Class<T> targetType)
		{
			return (T) knownInstances.get( source );
		}

		@BeforeMapping
		public void storeMappedInstance(Object source, @MappingTarget Object target)
		{
			knownInstances.put( source, target );
		}
	}

	@Target({CONSTRUCTOR})
	@Retention(CLASS)
	@interface Default { }
}