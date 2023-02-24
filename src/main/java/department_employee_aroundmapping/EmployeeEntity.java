package department_employee_aroundmapping;

import static department_employee_aroundmapping.MapStructMapper.*;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PROTECTED;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@NoArgsConstructor(access = PROTECTED) // generate no args constructor for jpa, mapstruct, ...
//@Accessors(fluent = true) // mapstruct does not seem to support fluent accessors
@Getter()
@ToString
@EqualsAndHashCode
public class EmployeeEntity
{
	/** can not be modified from outside, not final because otherwise there has to be a constructor with setId-parameter */
	private Long id;

	/** mutable, but not nullable */
	@NonNull @Setter private String name;

	/** mutable, but not nullable */
	@NonNull @Setter private DepartmentEntity department;

	/**
	 * let this be used by mapstruct (@Default, @ObjectFactory) and make sure to manually call required args constructor
	 * @param employee incoming entity to be used for construction of instance
	 * @param context incoming context to properly handling cyclic dependencies
	 */
//	@Default // necessary, seems to make sure mapstruct does not use no-args-constructor
	public EmployeeEntity(@NonNull EmployeeDTO employee, @NonNull CycleTracking context)
	{
		// call required args constructor
		this(employee.getName(), INSTANCE.map(employee.getDepartment(), context));
		setId(department.getId());
		log.debug("{}, context {}", this, context);
	}

	void beforeMapping(@NonNull EmployeeDTO employee)
	{
		log.debug("entity {}, dto {}", this, employee);
		// set fields that can not be modified from outside
		if (!isNull(employee.getId())) setId(employee.getId());
	}

	void afterMapping(@NonNull EmployeeDTO employee)
	{
		log.debug("entity {}, dto {}", this, employee);
	}

	private void setId(@NonNull Long id) { this.id = id; }
}