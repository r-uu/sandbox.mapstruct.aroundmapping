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
@NoArgsConstructor(access = PROTECTED) // generate no args constructor for jpa, mapstruct, ...
//@Accessors(fluent = true) // mapstruct does not seem to support fluent accessors
@Getter()
@ToString
@EqualsAndHashCode
public class EmployeeEntity
{
	/**
	 * may not be modified from outside
	 * <p>not {@code final} or {@code @NonNull} because otherwise there has to be a constructor with {@code id}-parameter
	 */
	private Long id;

	/** mutable, but not nullable */
	@NonNull @Setter private String name;

	/** mutable, but not nullable */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@NonNull @Setter private DepartmentEntity department;

	void beforeMapping(@NonNull EmployeeDTO employee, CycleTracking context)
	{
		// set fields that can not be modified from outside
		if (!isNull(employee.getId())) setId(employee.getId());
	}

	void afterMapping(@NonNull EmployeeDTO employee, CycleTracking context) { }

	private void setId(@NonNull Long id) { this.id = id; }
}