package department_employee_aroundmapping;

import static java.util.Objects.isNull;
import static lombok.AccessLevel.PROTECTED;

import java.util.HashSet;
import java.util.Set;

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
public class DepartmentDTO
{
	/** can not be modified from outside, not final because otherwise there has to be a constructor with setId-parameter */
	private Long id;

	/** mutable non-null */
	@NonNull @Setter private String name;

	/** mutable */
	@Setter private String description;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<EmployeeDTO> employees;

	/**
	 * let this be used by mapstruct (@Default, @ObjectFactory) and make sure to manually call required args constructor
	 * @param department incoming entity to be used for construction of instance
	 * @param context incoming context to properly handling cyclic dependencies
	 */
//	@Default // necessary, seems to make sure mapstruct does not use no-args-constructor
//	public DepartmentDTO(@NonNull DepartmentEntity department, @NonNull CycleTracking context)
//	{
//		// call required args constructor
//		this(department.getName());
//		setId(department.getId());
//		log.debug("{}, context {}", this, context);
//	}


	/** return unmodifiable */
	public Set<EmployeeDTO> getEmployees()
	{
		if (isNull(employees)) return null;
		return Set.copyOf(employees);
	}

	public boolean add(@NonNull EmployeeDTO employee)
	{
		if (employee.getDepartment() == this)
		{
			if (employeesContains(employee)) return true;
			return nonNullEmployees().add(employee);
		}
		else
		{
			// following check should never return true
			if (employeesContains(employee))
				log.error("employee with {} is already contained in {}", employee.getDepartment(), this);

			// assign this department as department of employee and update employees
			employee.setDepartment(this);
			return nonNullEmployees().add(employee);
		}
	}

	public boolean remove(@NonNull EmployeeDTO employee)
	{
		if (isNull(employees)) return false;
		return employees.remove(employee);
	}

	private Set<EmployeeDTO> nonNullEmployees()
	{
		if (isNull(employees)) employees = new HashSet<>();
		return employees;
	}

	private boolean employeesContains(EmployeeDTO employee)
	{
		if (isNull(employees)) return false;
		return employees.contains(employee);
	}

	void beforeMapping(@NonNull DepartmentEntity department)
	{
		log.debug("dto {}, entity {}", this, department);
		// set fields that can not be modified from outside
		if (!isNull(department.getId())) setId(department.getId());
	}

	void afterMapping(@NonNull DepartmentEntity department)
	{
		log.debug("dto {}, entity {}", this, department);
	}

	private void setId(@NonNull Long id) { this.id = id; }
}