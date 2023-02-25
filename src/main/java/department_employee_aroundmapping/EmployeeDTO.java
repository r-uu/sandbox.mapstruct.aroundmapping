package department_employee_aroundmapping;

import static java.util.Objects.isNull;
import static lombok.AccessLevel.PROTECTED;

import department_employee_aroundmapping.MapStructMapper.CycleTracking;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@NonNull @Setter private DepartmentDTO department;

//	@Default // necessary, seems to make sure mapstruct does not use no-args-constructor
//	public EmployeeDTO(@NonNull EmployeeEntity employee, @NonNull CycleTracking context)
//	{
//		// call required args constructor
//		this(employee.getName(), MapStructMapper.INSTANCE.map(employee.getDepartment(), context));
//		setId(department.getId());
//		log.debug("{}, context {}", this, context);
//	}

	/**
	 * called by mapstruct (@BeforeMapping), sets fields that can not be modified from outside
	 * @param employee incoming entity to be used for construction of instance
	 * @param context incoming context to properly handling cyclic dependencies
	 */
	void beforeMapping(@NonNull EmployeeEntity employee, CycleTracking context)
	{
		log.debug("dto {}, entity {}", this, employee);
		// set fields that can not be modified from outside
		if (!isNull(employee.getId())) setId(employee.getId());
	}

	void afterMapping(@NonNull EmployeeEntity entity, CycleTracking context)
	{
		log.debug("dto {}, entity {}", this, entity);
	}

	private void setId(@NonNull Long id) { this.id = id; }
}