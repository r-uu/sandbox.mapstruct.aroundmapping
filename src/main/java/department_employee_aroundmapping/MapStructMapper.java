package department_employee_aroundmapping;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.IdentityHashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import lombok.ToString;

@Mapper
@Slf4j
public abstract class MapStructMapper
{
	public static MapStructMapper INSTANCE = Mappers.getMapper(MapStructMapper.class);

	abstract DepartmentEntity map(DepartmentDTO    department, MapStructCycleTrackingContext context);
	abstract DepartmentDTO    map(DepartmentEntity department, MapStructCycleTrackingContext context);

	abstract EmployeeEntity map(EmployeeDTO    employee, MapStructCycleTrackingContext context);
	abstract EmployeeDTO    map(EmployeeEntity employee, MapStructCycleTrackingContext context);

	@BeforeMapping
	protected void beforeMapping(
			               DepartmentDTO                 dto,
			@MappingTarget DepartmentEntity              entity,
			@Context       MapStructCycleTrackingContext context)
	{
		log.debug("dto: {} entity: {}, context; {}", dto, entity, context);
	}

	@AfterMapping
	protected void afterMapping(
			               DepartmentDTO                 dto,
			@MappingTarget DepartmentEntity              entity,
			@Context       MapStructCycleTrackingContext context)
	{
		log.debug("dto: {} entity: {}, context; {}", dto, entity, context);
	}

	/** used to handle cyclic dependencies in mapstruct mappings */
	@ToString
	public static class MapStructCycleTrackingContext
	{
		private Map<Object, Object> knownInstances = new IdentityHashMap<Object, Object>();

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