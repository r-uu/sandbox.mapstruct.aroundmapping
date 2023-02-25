package department_employee_aroundmapping;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.IdentityHashMap;
import java.util.Map;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.TargetType;
import org.mapstruct.factory.Mappers;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Mapper
@Slf4j
public abstract class MapStructMapper
{
	public static final MapStructMapper INSTANCE = Mappers.getMapper(MapStructMapper.class);

	abstract DepartmentEntity map(DepartmentDTO    department, @Context CycleTracking context);
	abstract DepartmentDTO    map(DepartmentEntity department, @Context CycleTracking context);

	abstract EmployeeEntity map(EmployeeDTO    employee, @Context CycleTracking context);
	abstract EmployeeDTO    map(EmployeeEntity employee, @Context CycleTracking context);

	@BeforeMapping protected void beforeMapping(
			DepartmentDTO source, @MappingTarget DepartmentEntity target, @Context CycleTracking context)
	{
		target.beforeMapping(source, context);
	}

	@AfterMapping protected void afterMapping(
			DepartmentDTO source, @MappingTarget DepartmentEntity target, @Context CycleTracking context)
	{
		target.afterMapping(source, context);
	}

	@BeforeMapping protected void beforeMapping(
			DepartmentEntity source, @MappingTarget DepartmentDTO target, @Context CycleTracking context)
	{
		target.beforeMapping(source, context);
	}

	@AfterMapping protected void afterMapping(
			DepartmentEntity source, @MappingTarget DepartmentDTO target, @Context CycleTracking context)
	{
		target.afterMapping(source, context);
	}

	/** used to handle cyclic dependencies in mapstruct mappings */
	@ToString public static class CycleTracking
	{
		private final Map<Object, Object> knownInstances = new IdentityHashMap<>();

		@BeforeMapping
		public <T> T getMappedInstance(Object source, @TargetType Class<T> targetType)
		{
			return targetType.cast(knownInstances.get(source));
		}

		@BeforeMapping
		public void storeMappedInstance(Object source, @MappingTarget Object target)
		{
			knownInstances.put(source, target);
		}
	}

	@Target({CONSTRUCTOR})
	@Retention(CLASS)
	@interface Default { }
}