package department_employee_aroundmapping;

import static lombok.AccessLevel.PROTECTED;

import department_employee_aroundmapping.MapStructMapper.CycleTracking;
import department_employee_aroundmapping.MapStructMapper.Default;
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
@NoArgsConstructor(access = PROTECTED) // generate no args constructor for jsonb, jaxb, mapstruct, ...
//@Accessors(fluent = true) // mapstruct does not seem to support fluent accessors
@Getter
@ToString
@EqualsAndHashCode
public class EmployeeDTO
{
	/** can not be modified from outside, not final because otherwise there has to be a constructor with setId-parameter */
	private Long id;

	/** mutable non-null */
	@NonNull @Setter private String name;

	/** mutable non-null */
	@NonNull @Setter private DepartmentDTO department;

	/**
	 * let this be used by mapstruct (@Default, @ObjectFactory) and make sure to manually call required args constructor
	 * @param employee incoming entity to be used for construction of instance
	 * @param context incoming context to properly handling cyclic dependencies
	 */
	@Default // necessary, seems to make sure mapstruct does not use no-args-constructor
	public EmployeeDTO(@NonNull EmployeeEntity employee, @NonNull CycleTracking context)
	{
		// call required args constructor
		this(employee.getName(), MapStructMapper.INSTANCE.map(employee.getDepartment(), context));
		setId(department.getId());
		log.debug("{}, context {}", this, context);
	}

	private void setId(@NonNull Long id) { this.id = id; }
}